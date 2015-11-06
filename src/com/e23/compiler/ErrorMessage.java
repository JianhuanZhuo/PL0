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
	 * 用于存储错误列表的数据结构
	 */
	private ArrayList<ErrorItem> errorTable;
	
	
	public ErrorMessage() {
		this.errorTable = new ArrayList<ErrorItem>();
	}
	
	public void add(int line, String errorMes) {
		errorTable.add(new ErrorItem(line, errorMes));
	}
	
	/**
	 * 是否无错误的一次检查，
	 *	@return 编译过程中有错误，返回true，否则返回false
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