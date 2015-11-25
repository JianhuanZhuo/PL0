package compiler.lexer.automata;

import java.util.List;

public class Rule {
	State NowState;
	Symbol MatchSymbol;
	Symbol NowPushStackTop;
	// TODO 当前是一个状态，即使确定的下推自动机，。此处可做修改
	State NextState;
	//
	List<Symbol> NextPushStackTop;

	public boolean similar(Rule s) {
		if (s.NowState.name.equals(NowState.name) && s.NowPushStackTop.equals(NowPushStackTop)
				&& s.MatchSymbol.equals(MatchSymbol)) {
			return true;
		} else {
			return false;
		}
	}
}