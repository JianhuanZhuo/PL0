package com.e23.compiler;

public class ConstItem extends Item {

	private int 		val;		//const 数值
	

	public ConstItem(ObjectKind kind, String name, int val) throws CompilerException{
		super(kind, name);
		this.val	= val;
	}
	
	
	public int getVal() {
		return val;
	}


	public void setVal(int val) {
		this.val = val;
	}


	/**
	 * 实现ConstItem的深复制
	 * @Override
	 */
	protected Item clone() {
		
		ConstItem cloneConstItem = null;
		
		try {
			cloneConstItem = new ConstItem(kind, name, val);
		} catch (CompilerException e) {
		}
		return cloneConstItem;
	}


	@Override
	public String toString() {
		return "Kind:"+this.kind+"\tName:"+this.name+"\tVar:"+this.val;
	}

}
