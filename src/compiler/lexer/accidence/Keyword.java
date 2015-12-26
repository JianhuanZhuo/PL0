package compiler.lexer.accidence;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 处理关键字的
 * 
 * @author keepf
 *
 */
public class Keyword {

	Set<String> keyword = new HashSet<>();
	boolean defaultFlag;

	public Keyword() {
		loadDefault();
	}
	
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
		if (defaultFlag) {
			unloadDefault();
		}
		keyword.add(k);
	}

	public void addAll(String[] ks) {
		if (defaultFlag) {
			unloadDefault();
		}
		for (String k : ks) {
			keyword.add(k);
		}
	}

	public void addAll(Collection<? extends String> c) {
		if (defaultFlag) {
			unloadDefault();
		}
		keyword.addAll(c);
	}

	public void unloadDefault() {
		defaultFlag = false;
		keyword = new HashSet<>();
	}

	public void loadDefault() {
		defaultFlag = true;
		add("var");
		add("const");
		add("int");
		add("procedure");
		add("call");
		add("begin");
		add("end");
		add("if");
		add("read");
		add("write");
		add("odd");
		add("for");
		add("else");
		add("step");
		add("until");
		add("return");
	}
}
