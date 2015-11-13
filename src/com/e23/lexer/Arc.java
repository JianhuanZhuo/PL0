package com.e23.lexer;

public class Arc {

	Character jumpChar;
	Statement target;
	
	/**
	 * 构造一个转变弧，其转变条件可为空，即null
	 * @param target 目标状态
	 * @param jumpChar 转变条件字符
	 */
	public Arc(Statement target, Character jumpChar) {
		if (null == target) {
			throw new RuntimeException("弧构造错误，目标状态不可为空");
		}
		this.target = target;
		this.jumpChar = jumpChar;
	}
	
	
}