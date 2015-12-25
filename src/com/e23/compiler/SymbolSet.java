package com.e23.compiler;

import java.util.ArrayList;

public class SymbolSet<Object> extends ArrayList<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8686869950852253417L;

	
	
	public SymbolSet(int i) {
		super(i);
	}



	/**
	 * 返回一个添加指定元素的集合
	 *	@param addSymbol 欲添加的元素
	 *	@return 返回改变后的集合
	 */
	@SuppressWarnings("unchecked")
	public SymbolSet<Object> with(Integer addSymbol) {
		SymbolSet<Object> withSet = (SymbolSet<Object>)this.clone();
		withSet.add((Object) addSymbol);
		return withSet;
	}
	


	/**
	 * 返回一个添加指定元素的集合
	 *	@param addSymbol 欲添加的元素
	 *	@return 返回改变后的集合
	 */
	@SuppressWarnings("unchecked")
	public SymbolSet<Object> with(SymbolSet<Object> addSymbols) {
		SymbolSet<Object> withSet = (SymbolSet<Object>)this.clone();
		withSet.addAll(addSymbols);
		return withSet;
	}
	
	/**
	 * 检查集合内是否存在指定符号，这是一个不安全的方案，后期需要进行优化
	 *	@param checkSymbol 欲检查的符号
	 *	@return 存在返回true，否则返回false
	 */
	@SuppressWarnings("unchecked")
	public boolean inInteger(int checkSymbol) {
		for (Integer integer : (SymbolSet<Integer>)this) {
			if (integer.intValue() == checkSymbol) {
				return true;
			}
		}
		return false;
	}
}
