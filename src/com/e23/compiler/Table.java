package com.e23.compiler;

import java.util.ArrayList;

import com.e23.compiler.Item.ObjectKind;

public class Table implements  Cloneable{

	/**
	 * 最大嵌套层数
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
	 * 添加一个Item对象至table
	 *	@param item 欲添加的Item对象
	 */
	public void enter(Item item) throws CompilerException{
		items.add(item);
		if (item.getKind() == ObjectKind.PROC) {
			level++;
			
			//TODO 嵌套层数检查
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
	 * 查找指定ID是否是table内的一个对象
	 *	@param ID 欲查找的ID字符串
	 *	@return 匹配到的对象引用，不匹配将返回空
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
			System.out.println("ID:"+ID+" 无法查到");
			listTable();
			throw new CompilerException(11);
		}
		
		return resItem.clone();
	}
	
	
//	/**
//	 * 该函数将用于获得table尾巴的对象
//	 *	@return 端尾的对象或空
//	 *	@throws NullPointerException Table内容为空时
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
	 * 保存当前状态副本
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
	 * 设置
	 *	@param items
	 */
	protected void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	
	/**
	 * 深克隆方法
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

