package compiler.lexer.automata;

import java.util.List;

/**
 * <h1>单态的确定型下推规则类</h1><br>
 * 该类规则使用与单态的确定型下推自动机
 * 
 * @author keepf
 *
 */
public class SDPDARule implements Comparable<SDPDARule> {

	protected Symbol nowStackTop;
	protected Symbol matchSymbol;
	private List<Symbol> nextStackTop;

	/**
	 * 按当前下推栈栈顶符号、输入符号进行比较
	 */
	@Override
	public final int compareTo(SDPDARule o) {
		if (nowStackTop.compareTo(o.nowStackTop) != 0) {
			return nowStackTop.compareTo(o.nowStackTop);
		} else {
			return matchSymbol.compareTo(o.matchSymbol);
		}
	}
	
	@Override
	public String toString() {
		String res = "\tstackTop: "+nowStackTop+"\n\tmatchSymbol: "+matchSymbol+"\n\tnextStackTop: ";
		for (Symbol s : getNextStackTop()) {
			res += " "+s;
		}
		return res+"\n";
	}

	public List<Symbol> getNextStackTop() {
		return nextStackTop;
	}

	public void setNextStackTop(List<Symbol> nextStackTop) {
		this.nextStackTop = nextStackTop;
	}
	
	public Symbol getNowStackTop() {
		return nowStackTop;
	}
}
