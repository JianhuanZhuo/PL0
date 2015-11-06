package com.e23.compiler;

import java.util.ArrayList;

public class Code {

	private Table table;
	private static final long CODE_MAX = 2000;	//指令最大长度
	
	private ArrayList<Instruction> items;
	
	public Code() {
	}
	
	public Code(Table table) {
		items = new ArrayList<Instruction>();
		this.table = table;
	}

	/**
	 * 将指令item代码加入到指令表中
	 *	@param item 新加入的指令
	 */
	public void gen(Instruction item) throws CompilerException{
		if (items.size() > CODE_MAX) {
			throw new CompilerException(1);
		}
		item.setLineNum(Lexer.lineNum);
		items.add(item);
	}
	
	public int getIndex() {
		return items.size();
	}
	
	public void listCode() {
		for (int i = 0; i < items.size(); i++) {
			System.out.println(i+"\t"+items.get(i).toString());
		}
	}
	
	/**
	 * 获得指定索引的指令
	 *	@return 指令
	 */
	public Instruction getInstruction(int index){
		return items.get(index);
	}
}
