package com.e23.compiler;

public class ProcItem extends Item {

	private int		lev;	//����

	private int		addr;	//��ַ
	
	public ProcItem(ObjectKind kind, String name, int lev, int addr) {
		super(kind, name);
		this.lev	= lev;
		this.addr	= addr;
	}

	/**
	 * ʵ��ProcItem�����
	 * @Override
	 */
	protected Item clone() {
		return new ProcItem(kind, name, lev, addr);
	}

	/**
	 * addr����������
	 *	@param addr addr�����õ���ֵ
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
