package compiler.lexer.accidence;

import compiler.lexer.automata.SDPDARule;
import compiler.lexer.automata.SDPDARule_LL1;
import compiler.lexer.automata.SDPDA_LL1;
import compiler.lexer.automata.Symbol;

public class Classifier extends SDPDA_LL1 {
	/**
	 * 替换下推栈状态
	 */
	@Override
	public void replaceStack(SDPDARule r, Symbol input) {
		// TODO 下推栈状态替换
		pdStack.pop();
		int t = r.getNextStackTop().size();
		for (int i = 1; i <= t; i++) {
			Symbol e = r.getNextStackTop().get(t - i);
			pdStack.push(e);
		}
		if (((SDPDARule_LL1) r).isNullAccept()) {
			step(input);
		}
	}
}
