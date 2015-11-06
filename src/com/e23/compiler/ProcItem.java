package com.e23.compiler;

public class ProcItem extends Item {

	private int		lev;	//层数

	private int		addr;	//地址
	
	public ProcItem(ObjectKind kind, String name, int lev, int addr) {
		super(kind, name);
		this.lev	= lev;
		this.addr	= addr;
	}

	/**
	 * 实现ProcItem的深复制
	 * @Override
	 */
	protected Item clone() {
		return new ProcItem(kind, name, lev, addr);
	}

	/**
	 * addr属性设置器
	 *	@param addr addr欲设置的新值
	 */
	public void setAddr(int addr) {
		this.addr	= addr;
	}

	@Override
	public String toString() { 
		return "Kind:"+this.kind+"\tName:"+this.name+"\tLev:"+this.lev+"\tAddr:"+addr;
	}
	

	public int getLev() {
		return lev;
	}

	public int getAddr() {
		return addr;
	}
}
