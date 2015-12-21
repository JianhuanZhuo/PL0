package compiler.lexer.automata;

import java.util.List;

public class SDPDARule_LL1 implements Comparable<SDPDARule_LL1> {

	protected Symbol nowStackTop;
	protected Symbol matchSymbol;
	protected List<Symbol> nextStackTop;
	protected boolean isNullAccept;			//不接受
	
	
	/**
	 * 按当前下推栈栈顶符号、输入符号进行比较
	 */
	@Override
	public final int compareTo(SDPDARule_LL1 o) {
		if (nowStackTop.compareTo(o.nowStackTop) != 0) {
			return nowStackTop.compareTo(o.nowStackTop);
		} else {
			return matchSymbol.compareTo(o.matchSymbol);
		}
	}
}
