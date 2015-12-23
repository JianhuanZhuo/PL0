package com.tomKit.printOut;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg {

	/**
	 * 查找并返回一个符合正则表达式的字符段，用以简化正则表达式
	 * 
	 * @param content
	 * @param regExpress
	 * @param group
	 * @return
	 */
	public static String getFirstReg(String content, String regExpress, int group) {
		String result = null;
		Matcher matcher = Pattern.compile(regExpress).matcher(content);
		if (matcher.find()) {
			result = matcher.group(group);
		}
		return result;
	}

	public static String getFirstReg(String content, String regExpress) {
		return getFirstReg(content, regExpress, 0);
	}
	
	public static void name() {
		PO.d();
	}
	
}
