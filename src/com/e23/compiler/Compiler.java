package com.e23.compiler;

import com.e23.compiler.Item.ObjectKind;

public class Compiler {
	
	/**
	 * ���������
	 */
	private Parser parser;
	
	/**
	 * �������
	 */
	private Code code;
	
	/**
	 * ������Ϣ��
	 */
	private Table table;
	
	/**
	 * ������Ϣ��
	 */
	private ErrorMessage errorMessage;
	
	/**
	 * 
	 */
	private consoleOutput consoleOutput;
	
	/**
	 * ִ�н�����
	 */
	private Interpret interpret;
	
	
	public Compiler(consoleOutput consoleOutput, String filePath) {
		table			= new Table();
		code			= new Code(table);
		errorMessage	= new ErrorMessage();
		parser 			= new Parser(filePath, code, table, errorMessage);
		interpret		= new Interpret(code);
		this.consoleOutput = consoleOutput;
	}
	
	
	
	
	/**
	 * ���������ɻ�����
	 */
	public String build() {
		parser.blockPaser(Symbol.BLOCK_END_LIST, new ProcItem(ObjectKind.PROC, null, 0, 0));
		return errorMessage.listError();
	}
	
	/**
	 * ִ��
	 */
	public void exec() {
		if (errorMessage.noError()) {
			interpret.configConsole(consoleOutput);
			interpret.exec();
		}else {
			consoleOutput.append("����������д������顣");
			consoleOutput.append(errorMessage.listError());
		}
	}
	
	
	
	/**
	 * ����
	 */
	public void debug() {
		
	}
}
