package com.e23.compiler;

import com.e23.compiler.Item.ObjectKind;

public class Compiler {
	
	/**
	 * 代码解析器
	 */
	private Parser parser;
	
	/**
	 * 汇编代码表
	 */
	private Code code;
	
	/**
	 * 对象信息表
	 */
	private Table table;
	
	/**
	 * 错误信息表
	 */
	private ErrorMessage errorMessage;
	
	/**
	 * 
	 */
	private consoleOutput consoleOutput;
	
	/**
	 * 执行解析器
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
	 * 构建并生成汇编代码
	 */
	public String build() {
		parser.blockPaser(Symbol.BLOCK_END_LIST, new ProcItem(ObjectKind.PROC, null, 0, 0));
		return errorMessage.listError();
	}
	
	/**
	 * 执行
	 */
	public void exec() {
		if (errorMessage.noError()) {
			interpret.configConsole(consoleOutput);
			interpret.exec();
		}else {
			consoleOutput.append("编译过程中有错误！请检查。");
			consoleOutput.append(errorMessage.listError());
		}
	}
	
	
	
	/**
	 * 调试
	 */
	public void debug() {
		
	}
}
