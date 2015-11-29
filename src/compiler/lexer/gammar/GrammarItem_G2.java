package compiler.lexer.gammar;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.regexp.internal.recompile;

import compiler.lexer.automata.Symbol;

/**
 * 2型文法context-free grammar的产生式Production表示类<br>
 * 
 * 
 * @author keepf
 *
 */
public class GrammarItem_G2 {

	private Symbol left;
	private Vector<Symbol> right;

	public GrammarItem_G2(Symbol left) {
		this.left = left;
		right = new Vector<>();
	}

	public GrammarItem_G2(Symbol left, Vector<Symbol> right) {
		this.left = left;
		this.right = right;
	}

	public Symbol getLeft() {
		return left;
	}

	/**
	 * 获得文法右部的第一个符号<br />
	 * 
	 * @return 文法项右部的第一个符号，若当前文法项右部为空，则返回null
	 */
	public Symbol getRightFirstSymbol() {
		if (0 == right.size()) {
			return null;
		} else {
			return right.firstElement();
		}
	}

	/**
	 * 取出文法项右部的第一个符号<br />
	 * 该项操作将删除返回的符号
	 * 
	 * @return 文法项右部的第一个符号，若当前文法项右部为空，则返回null
	 */
	public Symbol takeRightFirstSymbol() {
		if (0 == right.size()) {
			return null;
		} else {
			return right.remove(0);
		}
	}

	/**
	 * 获得右部表
	 * 
	 * @return 右部表为空，返回null，否则返回该表
	 */
	public List<Symbol> getRightList() {
		if (0 != right.size()) {
			return right.subList(0, right.size());
		} else {
			return null;
		}
	}

	/**
	 * 在右部添加符号r
	 * 
	 * @param r
	 *            欲添加的符号
	 */
	public void addRight(Symbol r) {
		right.add(r);
	}

	/**
	 * 解析一段字符串添加到文法条目的右部
	 * 
	 * @param s
	 *            欲解析的字符串
	 * @return 解析失败返回false，否则返回true
	 */
	public boolean parserRight(String s) {
		boolean res = false;
		Matcher m = CFGrammar.CFGRightPattern.matcher(s);
		while (m.find()) {
			if (null == m.group("VN")) {
				System.out.println(m.group("VT"));
				addRight(new Symbol(m.group("VT")));
			} else {
				System.out.println(m.group("VN"));
				addRight(new Symbol(m.group("VN"), true));
			}
			res = true;
		}
		if (!res) {
			addRight(new Symbol("\\N"));
		}
		return true;
	}

	@Override
	public String toString() {
		String res = "";
		res += "left(" + left.getName() + ")->right( ";
		for (Symbol s : right) {
			if (s.getIsVN()) {
				res += "<"+s.getName() + "> ";
			}else {
				res += s.getName() + " ";
			}
		}
		res += ")\n";
		return res;
	}
}
