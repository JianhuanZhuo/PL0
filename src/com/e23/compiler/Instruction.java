package com.e23.compiler;



public class Instruction {

	private FuncCode func;
	private int l;
	private int a;
	private int lineNum;
	
	
	
	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public FuncCode getFunc() {
		return func;
	}

	public int getL() {
		return l;
	}

	public int getA() {
		return a;
	}

	public enum FuncCode{
		lit, opr, lod, sto, cal, Int, jmp, jpc
	}
	
	public Instruction(FuncCode func, int l, int a) {
		this.func	= func;
		this.l		= l;
		this.a		= a;
	}
	
	public void seta(int a) {
		this.a	= a;
	}
	
	@Override
	public String toString() {
		return "  "+lineNum+"  "+func+"  "+l+"  "+a;
	}
}
