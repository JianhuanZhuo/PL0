package com.e23.compiler;

public class CompilerException extends Exception {

	private int errorNum;

	private static String[] err_msg = {
	/* 0 */"",
	/* 1 */"Found ':=' when expecting '='.",
	/* 2 */"There must be a number to follow '='.",
	/* 3 */"There must be an '=' to follow the identifier.",
	/* 4 */"There must be an identifier to follow 'const', 'var', or 'procedure'.",
	/* 5 */"Missing ',' or ';'.",
	/* 6 */"Incorrect procedure name.",
	/* 7 */"Statement expected.",
	/* 8 */"Follow the statement is an incorrect symbol.",
	/* 9 */"'.' expected.",
	/* 10 */"';' expected.",
	/* 11 */"Undeclared identifier.",
	/* 12 */"Illegal assignment.",
	/* 13 */"':=' expected.",
	/* 14 */"There must be an identifier to follow the 'call'.",
	/* 15 */"A constant or variable can not be called.",
	/* 16 */"'then' expected.",
	/* 17 */"';' or 'end' expected.",
	/* 18 */"'do' expected.",
	/* 19 */"Incorrect symbol.",
	/* 20 */"Relative operators expected.",
	/* 21 */"Procedure identifier can not be in an expression.",
	/* 22 */"Missing ')'.",
	/* 23 */"The symbol can not be followed by a factor.",
	/* 24 */"The symbol can not be as the beginning of an expression.",
	/* 25 */"There must be an identifier to follow the 'write'.",
	/* 26 */"",
	/* 27 */"",
	/* 28 */"",
	/* 29 */"",
	/* 30 */"",
	/* 31 */"The number is too great.",
	/* 32 */"There are too many levels." };

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CompilerException(int errorNum) {
		super();
		this.errorNum = errorNum;
	}

	/**
	 * 获得该异常的详细错误信息
	 * 
	 * @return 详细错误信息
	 */
	public String ErrorMessage() {
		return "EEROR " + errorNum + ":  " + err_msg[errorNum];
	}

	/**
	 * 获得异常的错误号
	 * 
	 * @return 错误号
	 */
	public int getErrorNum() {
		return errorNum;
	}

	/**
	 * 获得指定错误号的具体错误信息
	 * 
	 * @param errorNum
	 * @return
	 */
	static public String getErrorString(int errorNum) {
		return err_msg[errorNum];
	}

}
