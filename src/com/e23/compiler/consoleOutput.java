package com.e23.compiler;

public interface consoleOutput {
	
	/**
	 * 输出指定字符串append至控制输出
	 */
	public void outString(String string);
	
	/**
	 * 將指定字符串append追加至控制输出
	 *	@param append
	 */
	public void append(String append);
}
