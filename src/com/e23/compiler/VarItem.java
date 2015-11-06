package com.e23.compiler;


public class VarItem extends Item {

	private int			lev;	//����
	private int 		addr;	//��ַ
	

	/**
	 * ָ�����͡���ʶ�����㼶����ַ����һ��VarItem
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
	 * ʵ��VarItem�����
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
