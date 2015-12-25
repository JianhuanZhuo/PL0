package compiler.lexer.accidence;

import java.util.Collection;
import java.util.Set;

/**
 * 处理关键字的
 * 
 * @author keepf
 *
 */
public class Keyword {

	Set<String> keyword;

	/**
	 * 检查该字符串是否为关键字
	 * 
	 * @param k
	 *            欲检查的关键字
	 * @return 是关键字，返回true，否则返回false
	 */
	public boolean isKeyword(String k) {
		return keyword.contains(k);
	}

	public void add(String k) {
		keyword.add(k);
	}

	public void addAll(String[] ks) {
		for (String k : ks) {
			keyword.add(k);
		}
	}

	public void addAll(Collection<? extends String> c) {
		keyword.addAll(c);
	}
}
