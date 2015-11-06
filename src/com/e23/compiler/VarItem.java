package com.e23.compiler;


public class VarItem extends Item {

	private int			lev;	//层数
	private int 		addr;	//地址
	

	/**
	 * 指定类型、标识符、层级、地址构造一个VarItem
	 *	@param kind
	 *	@param name
	 *	@param lev
	 *	@param addr
	 */
	public VarItem(ObjectKind kind, String name, int lev, int addr) {
		super(kind, name);
		this.lev	= lev;
		this.addr	= addr;
	}
	
	
	
	
	public int getLev() {
		return lev;
	}




	public void setLev(int lev) {
		this.lev = lev;
	}




	public int getAddr() {
		return addr;
	}




	public void setAddr(int addr) {
		this.addr = addr;
	}




	/**
	 * 实现VarItem的深复制
	 * @Override
	 */
	protected Item clone() {
		return new VarItem(kind, name, lev, addr);
	}




	@Override
	public String toString() {
		// TODO 
		return "Kind:"+this.kind+"\tName:"+this.name+"\tLev:"+this.lev+"\tAddr:"+addr;
	}

}
