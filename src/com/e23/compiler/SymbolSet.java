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
	 * ����һ�����ָ��Ԫ�صļ���
	 *	@param addSymbol ����ӵ�Ԫ��
	 *	@return ���ظı��ļ���
	 */
	@SuppressWarnings("unchecked")
	public SymbolSet<Object> with(Integer addSymbol) {
		SymbolSet<Object> withSet = (SymbolSet<Object>)this.clone();
		withSet.add((Object) addSymbol);
		return withSet;
	}
	


	/**
	 * ����һ�����ָ��Ԫ�صļ���
	 *	@param addSymbol ����ӵ�Ԫ��
	 *	@return ���ظı��ļ���
	 */
	@SuppressWarnings("unchecked")
	public SymbolSet<Object> with(SymbolSet<Object> addSymbols) {
		SymbolSet<Object> withSet = (SymbolSet<Object>)this.clone();
		withSet.addAll(addSymbols);
		return withSet;
	}
	
	/**
	 * ��鼯�����Ƿ����ָ�����ţ�����һ������ȫ�ķ�����������Ҫ�����Ż�
	 *	@param checkSymbol �����ķ���
	 *	@return ���ڷ���true�����򷵻�false
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
