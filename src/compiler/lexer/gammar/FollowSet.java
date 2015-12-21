package compiler.lexer.gammar;

import java.util.HashSet;
import java.util.Set;

import compiler.lexer.automata.Symbol;

/**
 * <h1>Follow集类</h1><br />
 * 指向First集合Follow集。<br />
 * 只有非终结符才需要Follow集
 * 
 * @author keepf
 *
 */
public class FollowSet {

	/**
	 * 对应的符号
	 */
	Symbol s;

	/**
	 * 当前的搜索者，用于刷新Follow集时算法用到的，避免死循环更新
	 */
	Symbol currentSearcher;

	// 指向了Follow集
	Set<FollowSet> followSet;

	// 指向了First集
	Set<FirstSet> firstSet;

	// 具体符号集缓存
	Set<Symbol> followSetCache;

	public FollowSet(Symbol s) {
		if (s.getIsVN()) {
			firstSet = new HashSet<FirstSet>();
			followSet = new HashSet<FollowSet>();
			this.s = s;
		} else {
			throw new RuntimeException("Unable to create a Follow Set by a terminal symbol");
		}
	}

	/**
	 * 获得Follow集所对应的符号
	 * 
	 * @return Follow集所对应的符号
	 */
	public Symbol getSymbol() {
		return s;
	}

	/**
	 * 获得符号对应的Follow集
	 * 
	 * @return 刷新后的Follow集
	 */
	public Set<Symbol> getFollowSet() {
		if (followSetCache == null) {
			return refleshFollowSetCache();
		} else {
			return followSetCache;
		}
	}

	/**
	 * 强制图搜索，
	 * 
	 * @param searcher
	 *            当前搜索者
	 * @return 图搜索的符号集
	 */
	public Set<Symbol> getFollowSet(Symbol searcher) {
		// 访问标识
		if (searcher.equals(currentSearcher)) {
			return null;
		}
		currentSearcher = searcher;
		Set<Symbol> cache = new HashSet<>();
		for (FirstSet f : firstSet) {
			cache.addAll(f.getFirstSetNoNull());
		}
		for (FollowSet f : followSet) {
			// TODO 递归强制搜索
			Set<Symbol> rf = f.getFollowSet(searcher);
			// 不是访问过的那个
			if (rf != null) {
				cache.addAll(rf);
			}
		}
		return cache;
	}

	/**
	 * 刷新该符号的Follow集，
	 * 
	 * @return 刷新后的Follow集
	 */
	private Set<Symbol> refleshFollowSetCache() {
		// TODO 更新
		followSetCache = getFollowSet(this.s);
		return followSetCache;
	}

	/**
	 * 添加一个First集
	 * 
	 * @param f
	 *            欲添加的First集
	 */
	public void add(FirstSet f) {
		firstSet.add(f);
	}

	/**
	 * 添加一个Follow集
	 * 
	 * @param f
	 *            欲添加的Follow集
	 */
	public void add(FollowSet f) {
		followSet.add(f);
	}

	@Override
	public String toString() {
		String res = s + " :\n";
		// for (FirstSet f : firstSet) {
		// res += "\tFirst : " + f.getSymbol() + "\n";
		// }
		// for (FollowSet f : followSet) {
		// res += "\tFollow: " + f.getSymbol() + "\n";
		// }
		refleshFollowSetCache();
		for (Symbol s : followSetCache) {
			res += "\t" + s + "\n";
		}
		return res;
	}

	@Override
	public int hashCode() {
		return s.hashCode() * 3;
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass().equals(obj.getClass())) {
			return s.equals(((FirstSet) obj).getSymbol());
		}
		return false;

	}
}
