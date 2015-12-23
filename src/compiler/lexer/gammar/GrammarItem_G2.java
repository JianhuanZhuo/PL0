package compiler.lexer.gammar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;

import com.sun.prism.Texture;

import compiler.lexer.automata.Symbol;

/**
 * 2型文法context-free grammar的产生式Production表示类<br>
 * 
 * 
 * @author keepf
 *
 */
public class GrammarItem_G2{

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
			return new ArrayList<>(right.subList(0, right.size()));
		} else {
			return null;
		}
	}
	
	public List<Symbol> getRightList_Forss() {
		List<Symbol> res = new ArrayList<>();
		if (!rightNull()) {
			res = new ArrayList<>(right);
		}
		return res;
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

	/**
	 * 检索该文法项中是否存在指定的符号
	 * 
	 * @param target
	 *            指定的匹配符号
	 * @return 存在返回true，否则返回false
	 */
	public boolean contain(Symbol target) {
		for (Symbol s : right) {
			if (0 == s.compareTo(target)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查该文法项中是否存在自引用
	 * 
	 * @return 存在自引用返回true，否则返回false
	 */
	public boolean selfRefer() {
		Symbol target = getLeft();
		for (Symbol s : right) {
			if (0 == s.compareTo(target)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查该文法项中的右部是否存在终结符
	 * 
	 * @return 若存在则返回true，否则返回false
	 */
	public boolean hasTermial() {
		for (Iterator<Symbol> iterator = right.iterator(); iterator.hasNext();) {
			Symbol symbol = (Symbol) iterator.next();
			if (!symbol.getIsVN()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 文法项右部为空的检查
	 * 
	 * @return 为空返回true，否则返回false
	 */
	public boolean rightNull() {
		if (right.size()>0 && right.firstElement().compareTo(new Symbol("\\N")) == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 检查该产生式右部是否符合GNF范式
	 * @return 符合返回true，否则返回false
	 */
	public boolean meetGNF() {
		if (rightNull()) {
			return true;
		}else {
			if (getRightFirstSymbol().getIsVN()) {
				return false;
			}else {
				return true;
			}
		}
	}

	/**
	 * 使用指定的符号串替换文法项右部的指定符号
	 * 
	 * @param target
	 *            目标符号
	 * @param replacement
	 *            用于替换的符号串，传入null表示删除目标符号
	 * @return 若目标符号不存在于文法项右部，则返回false，否则替换符号并返回true
	 */
	public boolean replace(Symbol target, List<Symbol> replacement) {

		for (int i = 0; i < right.size(); i++) {
			if (0 == right.get(i).compareTo(target)) {
				right.remove(i);
				if (null != replacement) {
					return right.addAll(i, replacement);
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String res = "";
		res += "left(" + left.getName() + ")->right( ";
		for (Symbol s : right) {
			if (s.getIsVN()) {
				res += "<" + s.getName() + "> ";
			} else {
				res += s.getName() + " ";
			}
		}
		res += ")";
		return res;
	}
	
	public GrammarItem_G2 copy() {
		GrammarItem_G2 c = new GrammarItem_G2(this.left);
		c.right = new Vector<Symbol>(right);
		return c;
	}

	@Override
	public int hashCode() {
		int c = left.hashCode() * 2;
		for (Symbol s : right) {
			c += s.hashCode();
		}
		return c;
	}

	@Override
	public boolean equals(Object obj) {
		GrammarItem_G2 g = (GrammarItem_G2)obj;
		if (!g.left.equals(left)) {
			return false;
		}
		List<Symbol> gRight = g.getRightList();
		List<Symbol> tRight = getRightList();
		for (int i = 0; i < gRight.size(); i++) {
			if(!gRight.get(i).equals(tRight.get(i))) {
				return false;
			}
		}
		return true;

	}
}
