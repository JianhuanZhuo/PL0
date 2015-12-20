package compiler.lexer.gammar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import compiler.lexer.automata.Symbol;

/**
 * <h1>First集合类</h1> <br/>
 * 该集合类必须在特定文法下才有意义，即此类的意义为，在某一文法下的指定符号的first集<br />
 * 该类的内容应该与符号的产生式分离，即该集合类不处理符号的产生式的事务<br />
 * 符号的产生式改编后，该类为被动更新
 * 
 * @author keepf
 *
 */
public class FirstSet {

	/**
	 * 该First集合对应的符号<br />
	 * 该成员应该是一个引用，而非副本，以便于访问该集合对应的那个符号
	 */
	Symbol s;

	/**
	 * 此类的First集合实现方式使用链接的方式<br />
	 * 即使用了书本上的画图法，虽然是一种费时费力的方法
	 */
	Set<FirstSet> firstSet;

	/**
	 * First缓存，需谨慎食用
	 */
	Set<Symbol> firstSetCache;

	public FirstSet(Symbol s) {
		this.s = s;
		if (s.getIsVN()) {
			firstSet = new HashSet<FirstSet>();
		}
	}

	/**
	 * 获得符号对应的first集
	 * 
	 * @return
	 */
	public Set<Symbol> getFirstSet() {
		if (s.getIsVN()) {
			if (firstSetCache == null) {
				return refleshFirstSetCache();
			} else {
				return firstSetCache;
			}
		} else {
			// 将自身返回
			return new HashSet<>(Arrays.asList(s));
		}
	}

	/**
	 * 获得除去空符号的first集合
	 * 
	 * @return 除去空符号的first集合
	 */
	public Set<Symbol> getFirstSetNoNull() {
		if (s.getIsVN()) {
			if (firstSetCache == null) {
				refleshFirstSetCache();
			}
			Set<Symbol> noNull = new HashSet<>();
			noNull.addAll(firstSetCache);
			noNull.remove(new Symbol("\\N"));
			return noNull;
		} else {
			// 将自身返回
			return new HashSet<>(Arrays.asList(s));
		}
	}

	/**
	 * 获得first集，该返回值将以链接的方式，若该符号是一个终结符，则返回null
	 * 
	 * @return first集，是一个指向其他FirstSet的列表，或者null
	 */
	public Set<FirstSet> getSubFirstSet() {
		if (s.getIsVN()) {
			return firstSet;
		} else {
			return null;
		}
	}

	/**
	 * 刷新该符号的First集，
	 * 
	 * @return 刷新后的First集
	 */
	public Set<Symbol> refleshFirstSetCache() {
		// TODO 递归的刷新过程。
		Set<Symbol> cache = new HashSet<>();
		for (FirstSet s : firstSet) {
			cache.addAll(s.getFirstSetNoNull());
		}
		// TODO 更新
		firstSetCache = cache;
		return cache;
	}

	/**
	 * 添加一个first集，表示一个依赖的关系
	 * 
	 * @param f
	 *            欲添加的first集
	 */
	public void add(FirstSet f) {
		firstSet.add(f);
	}

	/**
	 * 获得First集所对应的符号
	 * 
	 * @return First集所对应的符号
	 */
	public Symbol getSymbol() {
		return s;
	}

	@Override
	public String toString() {
		String res = s+" :\n";
		if (s.getIsVN()) {
			for (FirstSet f : firstSet) {
				res += "\t" + f.getSymbol() + "\n";
			}
		}
		return res;
	}
	
	@Override
	public int hashCode() {
		return s.hashCode()*2;
	}
	
	@Override
	public boolean equals(Object obj) {
		return s.equals(((FirstSet)obj).getSymbol());
	}
}
