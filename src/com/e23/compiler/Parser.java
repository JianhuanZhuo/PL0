package com.e23.compiler;

import com.e23.compiler.Instruction.FuncCode;
import com.e23.compiler.Item.ObjectKind;

public class Parser {

	/**
	 * 汇编代码表引用
	 */
	private Code code;

	/**
	 * 对象信息表引用
	 */
	private Table table;

	/**
	 * 词法分析
	 */
	private Lexer lexer;

	/**
	 * 错误记录对象引用
	 */
	private ErrorMessage errorMessage;

	/**
	 * 代码解析器的构造方法
	 * 
	 * @param filePath
	 *            欲解析文件的路径
	 * @param code
	 *            代码对象引用
	 * @param table
	 *            对象信息表引用
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
	 * 分程序解析
	 */
	public void blockPaser(SymbolSet<Integer> endSybolsList, ProcItem procBase) {

		Instruction jmpBase = new Instruction(FuncCode.jmp, 0, 0);
		try {
			// TODO 生成一个跳转地址
			code.gen(jmpBase);
		} catch (CompilerException e) {
			e.printStackTrace();
		}

		table.setDx(3);
		// TODO 检查层数

		// TODO 分程序解析
		do {
			// TODO 常量说明部分解析
			if (Symbol.CONST_SYMBOL == lexer.lastSymbol().getSymbolType()) {
				lexer.getSymbol();
				do {
					constdeclearation();

					// TODO 可选的额外声明
					while (Symbol.COMMA == lexer.lastSymbol().getSymbolType()) {
						lexer.getSymbol();
						constdeclearation();
					}

					// TODO 分号结束检查
					if (Symbol.SEMICOLON == lexer.lastSymbol().getSymbolType()) {
						lexer.getSymbol();
					} else {
						error(5);
					}
				} while (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType());
			}// end of if(sym==constsym)

			// TODO 变量说明部分解析
			if (Symbol.VAR_SYMBOL == lexer.lastSymbol().getSymbolType()) {
				lexer.getSymbol();
				do {
					vardeclearation();

					// TODO 可选的额外声明
					while (Symbol.COMMA == lexer.lastSymbol().getSymbolType()) {
						lexer.getSymbol();
						vardeclearation();
					}

					// TODO 分号结束检查
					if (Symbol.SEMICOLON == lexer.lastSymbol().getSymbolType()) {
						lexer.getSymbol();
					} else {
						error(5);
					}
				} while (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType());
			}

			table.listTable();

			// TODO 过程说明部分解析
			while (Symbol.PROCEDURE_SYMBOL == lexer.lastSymbol().getSymbolType()) {

				lexer.getSymbol();
				ProcItem procItem = null;

				// 2.3.1. 登记过程名
				if (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType()) {
					// TODO 这里可以做得更好
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

				// 2.3.2. 递归过程，其前需要保存当前的tx与dx
				Table tableCopy = table.getTable();
				@SuppressWarnings("unchecked")
				SymbolSet<Integer> blockList = (SymbolSet<Integer>) endSybolsList.clone();
				blockList.add(Symbol.SEMICOLON);
				blockPaser(blockList, procItem);
				// 2.3.3. 递归结束，复原tx与dx
				table = tableCopy;
				table.reduceLevel();

				// TODO 语句结束错误检查
				if (Symbol.SEMICOLON == lexer.lastSymbol().getSymbolType()) {
					lexer.getSymbol();
					test(Symbol.PROC_DECL_END_List, endSybolsList, 6);
				} else {
					error(5);
				}
			}

			test(Symbol.PROC_ALL_END_List, Symbol.DECL_BEG_LIST, 7);
		} while (lexer.lastSymbol().isDeclBegSys());

		// TODO 回填过程地址
		jmpBase.seta(code.getIndex());
		procBase.setAddr(code.getIndex());

		// TODO 语句部分分析
		try {
			code.gen(new Instruction(FuncCode.Int, 0, table.getDx()));
			statePaser(endSybolsList.with(Symbol.STAT_BEG_LIST).with(Symbol.END_SYMBOL).with(Symbol.SEMICOLON));
			code.gen(new Instruction(FuncCode.opr, 0, 0));
			test(endSybolsList, Symbol.NULL_List, 8);
			// TODO 打印目标代码
			code.listCode();
			table.listTable();
		} catch (CompilerException e) {
			error(e.getErrorNum());
		}

	}

	/**
	 * 语句解析
	 */
	void statePaser(SymbolSet<Integer> endSybolsList) {
		Item targetItem = null;

		// TODO 赋值语句解析
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

			// TODO 赋值符号后的表达式解析
			expressionPaser(endSybolsList);

			// TODO 插入赋值语句
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
		// TODO 过程调用语句
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
		// TODO if语句解析
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
		

//		// TODO if语句解析
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
		
		
		// TODO 复合语句解析
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
		// TODO while语句解析
		else if (lexer.checkSymbolType(Symbol.WHILE_SYMBOL)) {

			Instruction jcpConditInstruction; // 条件跳转指令
			Instruction jmpReturnInstruction; // 循环跳转指令

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

		// TODO write语句解析
		else if (lexer.checkSymbolType(Symbol.WRITE_SYMBOL)) {
			lexer.getSymbol();
			if (lexer.checkSymbolType(Symbol.LEFT_PARENTHESIS)) {
				lexer.getSymbol();
				if (lexer.checkSymbolType(Symbol.IDENTIFIER)) {

					// TODO 打印指令生成
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
	 * 表达式解析函数
	 * 
	 * @param endSybolsList
	 */
	private void expressionPaser(SymbolSet<Integer> endSybolsList) {

		int addop; // 前缀符

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
	 * 条件表达式解析函数
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
	 * 项解析函数
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
	 * 因子解析函数
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
	 * 常量说明部分解析
	 */
	private void constdeclearation() {

		if (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType()) {
			lexer.getSymbol();
			// TODO 此处的becomes表示获得的是一个:=，这个一个容错？但又说这个是错误的？
			// TODO 还是说这个是一个错误检查，为了得出1号类型错误
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
	 * 变量声明部分解析处理函数
	 */
	private void vardeclearation() {

		if (Symbol.IDENTIFIER == lexer.lastSymbol().getSymbolType()) {
			// TODO 将变量对象加入Table表
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
	 * 语句尾检查
	 */
	private void test(SymbolSet<Integer> endSybolsList, SymbolSet<Integer> aidSybolsList, int errorNum) {

		// TODO 结束符号检查
		for (Integer integer : endSybolsList) {
			if (integer.intValue() == lexer.lastSymbol().getSymbolType()) {
				return;
			}
		}

		error(errorNum);

		// TODO 缩小错误范围
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
	 * 报告错误，将被收集至错误管理器
	 * 
	 * @param errorNum
	 *            错误号
	 */
	private void error(int errorNum) {
		errorMessage.add(lexer.getLineNum(), CompilerException.getErrorString(errorNum));
	}
}
