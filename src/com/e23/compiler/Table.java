package com.e23.compiler;

import java.util.ArrayList;

import com.e23.compiler.Item.ObjectKind;

public class Table implements  Cloneable{

	/**
	 * ���Ƕ�ײ���
	 */
	public static final int LEVEL_MAX = 3;		
		
	private ArrayList<Item> items;
	private int level = 0;
	private	int dx;
	
	
	public Table() {
		items = new ArrayList<Item>();
	}
	
	
	public int getDx() {
		return dx;
	}

	public void setDx(int dx){
		this.dx = dx;
	}
	
	
	/**
	 * ���һ��Item������table
	 *	@param item ����ӵ�Item����
	 */
	public void enter(Item item) throws CompilerException{
		items.add(item);
		if (item.getKind() == ObjectKind.PROC) {
			level++;
			
			//TODO Ƕ�ײ������
			if (level > Table.LEVEL_MAX) {
				throw new CompilerException(32);
			}
		}else if (item.getKind() == ObjectKind.VAR) {
			dx++;
		}
	}
	
	
	public int getLevel() {
		return level;
	}

	/**
	 * ����ָ��ID�Ƿ���table�ڵ�һ������
	 *	@param ID �����ҵ�ID�ַ���
	 *	@return ƥ�䵽�Ķ������ã���ƥ�佫���ؿ�
	 *	@throws NullPointerException
	 */
	public Item position(String ID)throws CompilerException{
		
		Item resItem = null;
		
		for (Item itemElem : items) {
			if (itemElem.matchID(ID)) {
				resItem = itemElem;
				break;
			}
		}//end of for(:)
		
		if (null == resItem) {
			System.out.println("ID:"+ID+" �޷��鵽");
			listTable();
			throw new CompilerException(11);
		}
		
		return resItem.clone();
	}
	
	
//	/**
//	 * �ú��������ڻ��tableβ�͵Ķ���
//	 *	@return ��β�Ķ�����
//	 *	@throws NullPointerException Table����Ϊ��ʱ
//	 */
//	Item getLastItem()throws NullPointerException{
//		
//		items.
//		if (null == lastItem) {
//			throw new NullPointerException();
//		}
//		return lastItem;
//	}
	
	/**
	 * ���浱ǰ״̬����
	 *	@return
	 */
	public Table getTable() {
		try {
			return (Table)this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void reduceLevel() {
		this.level--;
	}
	
	
	/**
	 * ����
	 *	@param items
	 */
	protected void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	
	/**
	 * ���¡����
	 */
	protected Object clone() throws CloneNotSupportedException {
		// TODO 
		Table cloneTable = (Table)super.clone();
		cloneTable.setItems((ArrayList<Item>)this.items.clone());
		return cloneTable;
	}
	
	
	public void listTable() {
		for (int i = 0; i < items.size(); i++) {
			System.out.println(items.get(i).toString());
		}
	}
}

