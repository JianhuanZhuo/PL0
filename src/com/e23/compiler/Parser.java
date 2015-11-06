package com.e23.compiler;

import com.e23.compiler.Instruction.FuncCode;
import com.e23.compiler.Item.ObjectKind;

public class Parser {

	/**
	 * �����������
	 */
	private Code code;

	/**
	 * ������Ϣ������
	 */
	private Table table;

	/**
	 * �ʷ�����
	 */
	private Lexer lexer;

	/**
	 * �����¼��������
	 */
	private ErrorMessage errorMessage;

	/**
	 * ����������Ĺ��췽��
	 * 
	 * @param filePath
	 *            �������ļ���·��
	 * @param code
	 *            �����������
	 * @param table
	 *            ������Ϣ������
	 * @param errorMessage
	 */
	public Parser(String filePath, Code code, Table table, ErrorMessage errorMessage) {
		lexer = new Lexer(filePath);
		this.code = code;
		this.table = table;
		this.errorMessage = errorMessage;
		lexer.getSymbol();
	}

	/**
	 * �ֳ������
	 */
	public void blockPaser(SymbolSet<Integer> endSybolsList, ProcItem procBase) {

		Instruction jmpBase = new Instruction(FuncCode.jmp, 0, 0);
		try {
			// TODO ����һ����ת��ַ
			code.gen(jmpBase);
		} catch (CompilerException e) {
			e.printStackTrace();
		}

		table.setDx(3);
		// TODO ������

		// TODO �ֳ������
		do {
			// TODO ����˵�����ֽ���
			if (Symbol.CONST_SYMBOL == lexer.lastSymbol().getSymbolType()) {
				lexer.getSymbol();
				do {
					constdeclearation();

					// TODO ��ѡ�Ķ�������
					while (Symbol.COMMA == lexer.lastSymbol().getSymbolType()) {
						lexer.getSymbol();
						constdeclearation();
					}

					// TODO �ֺŽ������
					if (Symbol.SEMICOLON == lexer.lastSymbol().getSymbolType()) {
						lexer.getSymbol();
					} else {
						error(5);
					}
				} while (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType());
			}// end of if(sym==constsym)

			// TODO ����˵�����ֽ���
			if (Symbol.VAR_SYMBOL == lexer.lastSymbol().getSymbolType()) {
				lexer.getSymbol();
				do {
					vardeclearation();

					// TODO ��ѡ�Ķ�������
					while (Symbol.COMMA == lexer.lastSymbol().getSymbolType()) {
						lexer.getSymbol();
						vardeclearation();
					}

					// TODO �ֺŽ������
					if (Symbol.SEMICOLON == lexer.lastSymbol().getSymbolType()) {
						lexer.getSymbol();
					} else {
						error(5);
					}
				} while (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType());
			}

			table.listTable();

			// TODO ����˵�����ֽ���
			while (Symbol.PROCEDURE_SYMBOL == lexer.lastSymbol().getSymbolType()) {

				lexer.getSymbol();
				ProcItem procItem = null;

				// 2.3.1. �Ǽǹ�����
				if (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType()) {
					// TODO ����������ø���
					try {
						procItem = new ProcItem(ObjectKind.PROC, lexer.getLastName(), table.getLevel(), 0);
						table.enter(procItem);
					} catch (CompilerException e) {
						error(e.getErrorNum());
					}
					lexer.getSymbol();
				} else {
					error(4);
				}

				if (Symbol.SEMICOLON == lexer.lastSymbol().getSymbolType()) {
					lexer.getSymbol();
				} else {
					error(5);
				}

				// 2.3.2. �ݹ���̣���ǰ��Ҫ���浱ǰ��tx��dx
				Table tableCopy = table.getTable();
				@SuppressWarnings("unchecked")
				SymbolSet<Integer> blockList = (SymbolSet<Integer>) endSybolsList.clone();
				blockList.add(Symbol.SEMICOLON);
				blockPaser(blockList, procItem);
				// 2.3.3. �ݹ��������ԭtx��dx
				table = tableCopy;
				table.reduceLevel();

				// TODO ������������
				if (Symbol.SEMICOLON == lexer.lastSymbol().getSymbolType()) {
					lexer.getSymbol();
					test(Symbol.PROC_DECL_END_List, endSybolsList, 6);
				} else {
					error(5);
				}
			}

			test(Symbol.PROC_ALL_END_List, Symbol.DECL_BEG_LIST, 7);
		} while (lexer.lastSymbol().isDeclBegSys());

		// TODO ������̵�ַ
		jmpBase.seta(code.getIndex());
		procBase.setAddr(code.getIndex());

		// TODO ��䲿�ַ���
		try {
			code.gen(new Instruction(FuncCode.Int, 0, table.getDx()));
			statePaser(endSybolsList.with(Symbol.STAT_BEG_LIST).with(Symbol.END_SYMBOL).with(Symbol.SEMICOLON));
			code.gen(new Instruction(FuncCode.opr, 0, 0));
			test(endSybolsList, Symbol.NULL_List, 8);
			// TODO ��ӡĿ�����
			code.listCode();
			table.listTable();
		} catch (CompilerException e) {
			error(e.getErrorNum());
		}

	}

	/**
	 * ������
	 */
	void statePaser(SymbolSet<Integer> endSybolsList) {
		Item targetItem = null;

		// TODO ��ֵ������
		if (lexer.checkSymbolType(Symbol.IDENTIFIER)) {
			try {
				targetItem = (VarItem) table.position(lexer.lastSymbol().getName());
			} catch (CompilerException e) {
				error(11);
			}
			if (Item.ObjectKind.VAR != targetItem.getKind()) {
				error(12);
			}
			lexer.getSymbol();

			if (lexer.checkSymbolType(Symbol.BECOMES)) {
				lexer.getSymbol();
			} else {
				error(13);
			}

			// TODO ��ֵ���ź�ı��ʽ����
			expressionPaser(endSybolsList);

			// TODO ���븳ֵ���
			if (null != targetItem) {
				try {
					code.gen(new Instruction(FuncCode.sto, table.getLevel() - ((VarItem) targetItem).getLev(),
							((VarItem) targetItem).getAddr()));
				} catch (CompilerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		// TODO ���̵������
		else if (lexer.checkSymbolType(Symbol.CALL_SYMBOL)) {
			lexer.getSymbol();
			if (!lexer.checkSymbolType(Symbol.IDENTIFIER)) {
				error(14);
			} else {
				try {
					targetItem = (ProcItem) table.position(lexer.lastSymbol().getName());
				} catch (CompilerException e) {
					e.printStackTrace();
				}
				if (null == targetItem) {
					error(11);
				} else if (Item.ObjectKind.PROC == ((ProcItem) targetItem).getKind()) {
					try {
						code.gen(new Instruction(FuncCode.cal, table.getLevel() - ((ProcItem) targetItem).getLev(),
								((ProcItem) targetItem).getAddr()));
					} catch (CompilerException e) {
						e.printStackTrace();
					}
				} else {
					error(15);
				}

				lexer.getSymbol();
			}
		}
		// TODO if������
		else if (lexer.checkSymbolType(Symbol.IF_SYMBOL)) {
			lexer.getSymbol();
			conditionPaser(endSybolsList.with(Symbol.THEN_SYMBOL).with(Symbol.DO_SYMBOL));

			if (lexer.checkSymbolType(Symbol.THEN_SYMBOL)) {
				lexer.getSymbol();
			} else {
				error(16);
			}
			Instruction jpcInst = new Instruction(FuncCode.jpc, 0, 0);
			try {
				code.gen(jpcInst);
			} catch (CompilerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			statePaser(endSybolsList);
			jpcInst.seta(code.getIndex());
		}
		

//		// TODO if������
//		else if (lexer.checkSymbolType(Symbol.IF_SYMBOL)) {
//			lexer.getSymbol();
//			conditionPaser(endSybolsList.with(Symbol.THEN_SYMBOL).with(Symbol.DO_SYMBOL));
//
//			if (lexer.checkSymbolType(Symbol.THEN_SYMBOL)) {
//				lexer.getSymbol();
//			} else {
//				error(16);
//			}
//			Instruction jpcInst = new Instruction(FuncCode.jpc, 0, 0);
//			try {
//				code.gen(jpcInst);
//			} catch (CompilerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			statePaser(endSybolsList);
//			jpcInst.seta(code.getIndex());
//		}
		
		
		// TODO ����������
		else if (lexer.checkSymbolType(Symbol.BEGIN_SYMBOL)) {
			lexer.getSymbol();
			statePaser(endSybolsList.with(Symbol.SEMICOLON).with(Symbol.END_SYMBOL));
			while (lexer.checkSymbolType(Symbol.SEMICOLON)
					|| (Symbol.STAT_BEG_LIST.inInteger(lexer.lastSymbol().getSymbolType()))) {
				if (lexer.checkSymbolType(Symbol.SEMICOLON)) {
					lexer.getSymbol();
				} else {
					error(10);

				}
				statePaser(endSybolsList.with(Symbol.SEMICOLON).with(Symbol.END_SYMBOL));
			}
			if (lexer.checkSymbolType(Symbol.END_SYMBOL)) {
				lexer.getSymbol();
			} else {
				error(17);
			}
		}
		// TODO while������
		else if (lexer.checkSymbolType(Symbol.WHILE_SYMBOL)) {

			Instruction jcpConditInstruction; // ������תָ��
			Instruction jmpReturnInstruction; // ѭ����תָ��

			jmpReturnInstruction = new Instruction(FuncCode.jmp, 0, code.getIndex());
			jcpConditInstruction = new Instruction(FuncCode.jpc, 0, 0);
			lexer.getSymbol();
			try {
				conditionPaser(endSybolsList.with(Symbol.DO_SYMBOL));
				code.gen(jcpConditInstruction);

				if (lexer.checkSymbolType(Symbol.DO_SYMBOL)) {
					lexer.getSymbol();
				} else {
					error(18);
				}

				statePaser(endSybolsList);
				code.gen(jmpReturnInstruction);
				jcpConditInstruction.seta(code.getIndex());
			} catch (CompilerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// TODO write������
		else if (lexer.checkSymbolType(Symbol.WRITE_SYMBOL)) {
			lexer.getSymbol();
			if (lexer.checkSymbolType(Symbol.LEFT_PARENTHESIS)) {
				lexer.getSymbol();
				if (lexer.checkSymbolType(Symbol.IDENTIFIER)) {

					// TODO ��ӡָ������
					VarItem item = null;
					try {
						item = (VarItem) table.position(lexer.getLastName());
					} catch (CompilerException e1) {
						e1.printStackTrace();
					}
					if (null != item) {
						Instruction lodInstruction = new Instruction(FuncCode.lod, table.getLevel() - item.getLev(),
								item.getAddr());
						try {
							code.gen(lodInstruction);
							code.gen(new Instruction(FuncCode.opr, 0, 14));
						} catch (CompilerException e) {
							e.printStackTrace();
						}

					} else {
						error(11);
					}

					lexer.getSymbol();
					if (lexer.checkSymbolType(Symbol.RIGHT_PARENTHESIS)) {
						lexer.getSymbol();
					} else {
						error(22);
					}
				} else {
					error(25);
				}
			}

		}

		test(endSybolsList, Symbol.NULL_List, 19);
	}

	/**
	 * ���ʽ��������
	 * 
	 * @param endSybolsList
	 */
	private void expressionPaser(SymbolSet<Integer> endSybolsList) {

		int addop; // ǰ׺��

		if (lexer.checkSymbolType(Symbol.PLUS) || lexer.checkSymbolType(Symbol.MINUS)) {
			addop = lexer.lastSymbol().getSymbolType();
			lexer.getSymbol();

			termPaser(endSybolsList.with(Symbol.PLUS).with(Symbol.MINUS));

			try {
				if (addop == Symbol.MINUS) {
					code.gen(new Instruction(FuncCode.opr, 0, 1));
				}
			} catch (CompilerException e) {
				e.printStackTrace();
			}
		} else {
			termPaser(endSybolsList.with(Symbol.PLUS).with(Symbol.MINUS));
		}

		while (lexer.checkSymbolType(Symbol.PLUS) || lexer.checkSymbolType(Symbol.MINUS)) {
			addop = lexer.lastSymbol().getSymbolType();
			lexer.getSymbol();

			termPaser(endSybolsList.with(Symbol.PLUS).with(Symbol.MINUS));

			try {
				if (addop == Symbol.PLUS) {
					code.gen(new Instruction(FuncCode.opr, 0, 2));
				} else {
					code.gen(new Instruction(FuncCode.opr, 0, 3));
				}
			} catch (CompilerException e) {
			}
		}
	}

	/**
	 * �������ʽ��������
	 * 
	 * @param endSybolsList
	 */
	private void conditionPaser(SymbolSet<Integer> endSybolsList) {

		int relop;

		if (lexer.checkSymbolType(Symbol.ODD_SYMBOL)) {
			lexer.getSymbol();
			expressionPaser(endSybolsList);
			try {
				code.gen(new Instruction(FuncCode.opr, 0, 6));
			} catch (CompilerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			expressionPaser(endSybolsList.with(Symbol.EXPRE_List));

			if (!(Symbol.EXPRE_List.inInteger(lexer.lastSymbol().getSymbolType()))) {
				error(20);
			} else {
				relop = lexer.lastSymbol().getSymbolType();
				lexer.getSymbol();

				expressionPaser(endSybolsList);

				if (relop >= 5 && relop <= 10) {
					try {
						code.gen(new Instruction(FuncCode.opr, 0, relop + 3));
					} catch (CompilerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// switch (relop) {
				// case Symbol.EQUAL:
				// gen(opr, 0, 8);
				// break;
				//
				// case neq:
				// gen(opr, 0, 9);
				// break;
				//
				// case lss:
				// gen(opr, 0, 10);
				// break;
				//
				// case geq:
				// gen(opr, 0, 11);
				// break;
				//
				// case gtr:
				// gen(opr, 0, 12);
				// break;
				//
				// case leq:
				// gen(opr, 0, 13);
				// break;
				// }
			}
		}
	}

	/**
	 * ���������
	 * 
	 * @param endSybolsList
	 */
	private void termPaser(SymbolSet<Integer> endSybolsList) {
		long mulop;

		factorPaser(endSybolsList.with(Symbol.MULTIPLY).with(Symbol.DIVIDE));

		while (lexer.checkSymbolType(Symbol.MULTIPLY) || lexer.checkSymbolType(Symbol.DIVIDE)) {
			mulop = lexer.lastSymbol().getSymbolType();
			lexer.getSymbol();

			factorPaser(endSybolsList.with(Symbol.MULTIPLY).with(Symbol.DIVIDE));

			try {
				if (mulop == Symbol.MULTIPLY) {
					code.gen(new Instruction(FuncCode.opr, 0, 4));
				} else {
					code.gen(new Instruction(FuncCode.opr, 0, 5));
				}
			} catch (CompilerException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���ӽ�������
	 * 
	 * @param endSybolsList
	 */
	void factorPaser(SymbolSet<Integer> endSybolsList) {

		Item targetItem = null;

		test(Symbol.FACT_BEG_LIST, endSybolsList, 24);

		while (Symbol.FACT_BEG_LIST.inInteger(lexer.lastSymbol().getSymbolType())) {
			if (lexer.checkSymbolType(Symbol.IDENTIFIER)) {
				try {
					targetItem = table.position(lexer.lastSymbol().getName());
				} catch (CompilerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (null == targetItem) {
					error(11);
				} else {
					try {
						switch (targetItem.getKind()) {
							case CONST:
								code.gen(new Instruction(FuncCode.lit, 0, ((ConstItem) targetItem).getVal()));
								break;

							case VAR:
								code.gen(new Instruction(FuncCode.lod, table.getLevel()
										- ((VarItem) targetItem).getLev(), ((VarItem) targetItem).getAddr()));

								break;

							case PROC:
								error(21);
								break;
						}
					} catch (CompilerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				lexer.getSymbol();
			} else if (lexer.checkSymbolType(Symbol.NUMBER)) {
				if (lexer.lastSymbol().getNumber() > Symbol.NUMBER_MAX) {
					error(31);
					lexer.lastSymbol().setNumber(0);
				}

				try {
					code.gen(new Instruction(FuncCode.lit, 0, lexer.lastSymbol().getNumber()));
				} catch (CompilerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lexer.getSymbol();
			} else if (lexer.checkSymbolType(Symbol.LEFT_PARENTHESIS)) {
				lexer.getSymbol();
				expressionPaser(endSybolsList.with(Symbol.RIGHT_PARENTHESIS));

				if (lexer.checkSymbolType(Symbol.RIGHT_PARENTHESIS)) {
					lexer.getSymbol();
				} else {
					error(22);
				}
			}

			test(endSybolsList, Symbol.NULL_List.with(Symbol.LEFT_PARENTHESIS), 23);
		}
	}

	/**
	 * ����˵�����ֽ���
	 */
	private void constdeclearation() {

		if (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType()) {
			lexer.getSymbol();
			// TODO �˴���becomes��ʾ��õ���һ��:=�����һ���ݴ�����˵����Ǵ���ģ�
			// TODO ����˵�����һ�������飬Ϊ�˵ó�1�����ʹ���
			if (Symbol.EQUAL == lexer.lastSymbol().getSymbolType()
					|| Symbol.BECOMES == lexer.lastSymbol().getSymbolType()) {
//				if (Symbol.BECOMES == lexer.lastSymbol().getSymbolType()) {
//					error(1);
//				}
				lexer.getSymbol();

				if (Symbol.NUMBER == lexer.lastSymbol().getSymbolType()) {
					try {
						table.enter(new ConstItem(ObjectKind.CONST, lexer.getLastName(), lexer.lastSymbol().getNumber()));
					} catch (CompilerException e) {
						e.ErrorMessage();
					}
					lexer.getSymbol();
				} else {
					error(2);
				}
			} else {
				error(3);
			}
		} else {
			error(4);
		}

		table.listTable();
	}

	/**
	 * �����������ֽ���������
	 */
	private void vardeclearation() {

		if (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType()) {
			// TODO �������������Table��
			try {
				table.enter(new VarItem(ObjectKind.VAR, lexer.lastSymbol().getName(), table.getLevel(), table.getDx()));
			} catch (CompilerException e) {
				e.printStackTrace();
			}
			// ????????????????????????????????????????????????????????????????????????????????
			lexer.getSymbol();
		} else {
			error(4);
		}
	}

	/**
	 * ���β���
	 */
	private void test(SymbolSet<Integer> endSybolsList, SymbolSet<Integer> aidSybolsList, int errorNum) {

		// TODO �������ż��
		for (Integer integer : endSybolsList) {
			if (integer.intValue() == lexer.lastSymbol().getSymbolType()) {
				return;
			}
		}

		error(errorNum);

		// TODO ��С����Χ
		@SuppressWarnings("unchecked")
		SymbolSet<Integer> tempList = ((SymbolSet<Integer>) endSybolsList.clone());
		tempList.addAll(aidSybolsList);

		while (true) {
			for (Integer integer : tempList) {
				if (integer.intValue() == lexer.lastSymbol().getSymbolType()) {
					return;
				}
			}
			lexer.getSymbol();
		}
	}

	/***
	 * ������󣬽����ռ������������
	 * 
	 * @param errorNum
	 *            �����
	 */
	private void error(int errorNum) {
		errorMessage.add(lexer.getLineNum(), CompilerException.getErrorString(errorNum));
	}
}
