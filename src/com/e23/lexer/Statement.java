package com.e23.lexer;

import java.util.List;
import java.util.Vector;

public class Statement{

	private static int count;
	
	/**
	 * 开始状态标志
	 */
	private boolean start;

	/**
	 * 可满足状态标志
	 */
	private boolean end;
	
	/**
	 * 状态名
	 */
	String name;
	
	
	/**
	 * 节点向量，表示状态及其弧
	 */
	public List<Arc> stms = new Vector<Arc>();
	
	/**
	 * 默认构造函数，使用默认分配的状态名构造一个非起始状态
	 */
	public Statement() {
		this(false);
	}
	
	public Statement(boolean start) {
		this(start, "STM"+(++count));
	}
	
	public Statement(boolean start, String name) {
		this.start = start;
		if (null != name) {
			this.name = name;
		}
	}
	
	/**
	 * 添加一个弧
	 * @return 添加成功返回true，添加失败返回false
	 */
	public boolean addArc() {
		return false;
	}
	
	public boolean isStart() {
		return start;
	}
	
	public boolean isEnd() {
		return end;
	}
}
