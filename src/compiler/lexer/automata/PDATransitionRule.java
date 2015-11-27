package compiler.lexer.automata;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PDATransitionRule implements Comparator<PDATransitionRule> {

	/**
	 * 自动机状态
	 */
	State NowState;

	/**
	 * 输入符号
	 */
	Symbol MatchSymbol;

	/**
	 * 栈顶符号
	 */
	Symbol NowStackTop;

	/**
	 * 不确定的映射像
	 * 
	 * @author keepf
	 *
	 */
	class ActionImage {
		State NextState;
		List<Symbol> NextPushStackTop;
	}

	List<ActionImage> images = new ArrayList<>();

	public void addActionImage(State NextState, List<Symbol> NextPushStackTop) {
		ActionImage i = new ActionImage();
		i.NextState = NextState;
		i.NextPushStackTop = NextPushStackTop;
		images.add(i);
	}

	/**
	 * 比较实现
	 */
	@Override
	public int compare(PDATransitionRule o1, PDATransitionRule o2) {
		if (o1.NowState.hashCode() > o2.NowState.hashCode()) {
			return 1;
		} else if (o1.NowState.hashCode() < o2.NowState.hashCode()) {
			return -1;
		} else {
			if (o1.MatchSymbol.hashCode() > o2.MatchSymbol.hashCode()) {
				return 1;
			} else if (o1.MatchSymbol.hashCode() < o2.MatchSymbol.hashCode()) {
				return -1;
			} else {
				if (o1.NowStackTop.hashCode() > o2.NowStackTop.hashCode()) {
					return 1;
				} else if (o1.NowStackTop.hashCode() < o2.NowStackTop.hashCode()) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

}
