package compiler.lexer.gammar;

import java.util.HashSet;
import java.util.Set;

import compiler.lexer.automata.Symbol;

/**
 * <h1>产生式的选择集类</h1> <br />
 * 该选择集用于表示某一产生式的select集，
 * <hr>
 * 虽然产生式是可以确定的，但是select集的生成还是依赖于Fitst集和Follow集，<br />
 * 所以该集合的表示只有在特定文法下才有意义的
 * 
 * @author keepf
 *
 */
public class SelectSet {

	// 对应的2型文法产生式
	GrammarItem_G2 g;

	Set<Symbol> select = new HashSet<>();

	public SelectSet(GrammarItem_G2 g) {
		this.g = g;
	}

	/**
	 * 添加Select的符号集合
	 * 
	 * @param ss
	 *            欲添加的符号集
	 */
	public void add(Set<Symbol> ss) {
		for (Symbol s : ss) {
			select.add(s);
		}
	}

	/**
	 * 获得该产生式的选择集
	 * 
	 * @return 产生式的选择集
	 */
	public Set<Symbol> getSelectSet() {
		return select;
	}

	public GrammarItem_G2 getGrammarItem() {
		return g;
	}

	
	@Override
	public String toString() {
		String res = g + " : \n\t{ ";
		for (Symbol s : select) {
			res += s + " ";
		}
		res += "}";
		return res;
	}

	@Override
	public int hashCode() {
		return g.hashCode() * 2;
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass().equals(obj.getClass())) {
			return g.equals(((SelectSet) obj).getGrammarItem());
		}
		return false;

	}
}
