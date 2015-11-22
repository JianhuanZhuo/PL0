package com.e23.lexer;

import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文法类，用于对文法规则进行建模
 * 
 * @author keepf
 *
 */
public class Grammar {

	private Vector<String> grammars = new Vector<String>();
	private Boolean isRG = null;

	public Grammar() {
	}

	/**
	 * 使用一组文法字符串集初始化文法类对象
	 * 
	 * @param grammars
	 *            用于初始化文法类对象的文法字符串集
	 */
	public Grammar(String[] grammars) {
		for (String grammar : grammars) {
			if (null != grammar) {
				this.grammars.add(grammar);
			}
		}
	}

	/**
	 * 获得该文法的一个迭代器
	 * 
	 * @return 该文法的迭代器
	 */
	public Iterator<String> iterator() {
		return grammars.iterator();
	}

	/**
	 * 添加文法，将可能改变文法的类型，如在3型文法中加入非正规文法将导致文法类型改变为2型文法
	 * 
	 * @param grammar
	 *            欲添加的文法
	 */
	public void add(String grammar) {
		if (null == isRG || false == isRG.booleanValue()) {
			grammars.add(grammar);
		} else {
			Pattern pattern = Pattern.compile("^\\s*\\<([\\w\\d]*)\\>\\s*-\\>\\s*([\\W\\w])(\\<([\\w\\d]*)\\>\\s*)?");
			Matcher matcher = pattern.matcher(grammar);
			if (!matcher.matches()) {
				isRG = new Boolean(false);
			}
			grammars.add(grammar);
		}
	}

	/**
	 * 检查 是否为正则文法
	 * 
	 * @return
	 */
	public Boolean isRG() {
		if (null == isRG) {
			isRG = new Boolean(true);
			Pattern pattern = Pattern
					.compile(LexerDescriptor.Grammar2_MatchExpression);
			for (Iterator<String> iterator = grammars.iterator(); iterator.hasNext();) {
				Matcher matcher = pattern.matcher((String) iterator.next());
				if (!matcher.matches()) {
					isRG = new Boolean(false);
					break;
				}
			}
		}
		return isRG;
	}

	/**
	 * 获取指定索引的文法字符串
	 * 
	 * @param index
	 *            指定索引
	 * @return 文法字符串
	 */
	public String getgrammar(int index) {
		return grammars.get(index);
	}
	
	public int getGrammarLen() {
		return grammars.size();
	}

	public String toString() {
		String res = "this is ";
		if (!isRG) {
			res += "no ";
		}
		res += "a regular grammar. \n";

		for (Iterator<String> iterator = grammars.iterator(); iterator.hasNext();) {
			res += (String) iterator.next() + "\n";
		}
		return res;
	}
}
