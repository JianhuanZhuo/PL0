package com.e23.compiler;

import java.util.ArrayList;

public class ErrorMessage {


	class ErrorItem{
		int 	line;
		String 	errorMes;
		
		ErrorItem(int line, String errorMes){
			this.line 		= line;
			this.errorMes	= errorMes;
			
		}
		
		public String toString(){
			return "Error in line "+line+": "+errorMes;
		}
	
		
	}
	
	/**
	 * ���ڴ洢�����б�����ݽṹ
	 */
	private ArrayList<ErrorItem> errorTable;
	
	
	public ErrorMessage() {
		this.errorTable = new ArrayList<ErrorItem>();
	}
	
	public void add(int line, String errorMes) {
		errorTable.add(new ErrorItem(line, errorMes));
	}
	
	/**
	 * �Ƿ��޴����һ�μ�飬
	 *	@return ����������д��󣬷���true�����򷵻�false
	 */
	public boolean noError() {
		return errorTable.isEmpty();
	}
	
	public String listError() {
		String errorListString = "";
		
		for (ErrorItem item : errorTable) {
			errorListString	+= item.toString()+"\n";
		}
		
		return errorListString;
	}
}