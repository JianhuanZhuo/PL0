package compiler.lexer.gammar;

import java.util.List;
import java.util.Vector;

import com.sun.org.apache.regexp.internal.recompile;

import compiler.lexer.automata.Symbol;

public class GrammarItem_G2 {

	private Symbol left;
	private Vector<Symbol> right;

	public GrammarItem_G2() {
		right = new Vector<>();
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
}
