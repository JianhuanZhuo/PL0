package compiler.lexer.automata;

import java.util.List;

/**
 * <h1>确定的下推规则类</h1><br>
 * 该类规则使用与确定型的下推自动机
 * 
 * @author keepf
 *
 */
public class DPDARule implements Rule, Comparable<DPDARule> {

	private State nowState;
	private Symbol nowStackTop;
	private Symbol matchSymbol;
	State nextState;
	List<Symbol> nextStackTop;

	/**
	 * 按当前状态、当前下推栈栈顶符号、输入符号进行比较
	 */
	@Override
	public int compareTo(DPDARule o) {
		if (nowState.compareTo(o.nowState) != 0) {
			return nowState.compareTo(o.nowState);
		} else if (nowStackTop.compareTo(o.nowStackTop) != 0) {
			return nowStackTop.compareTo(o.nowStackTop);
		} else {
			return matchSymbol.compareTo(o.matchSymbol);
		}
	}
}
