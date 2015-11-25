package compiler.lexer.automata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import compiler.lexer.gammar.GNFGrammar;
import compiler.lexer.gammar.GrammarItem_G2;

/**
 * <b>确定型下推自动机</b><br>
 * 确定型的下推自动机比起下推自动机存在两个特殊性：
 * <ol>
 * <li>下推自动机知道下一步是进行空转移还是读取一个字符</li>
 * </ol>
 * 接收LL(1)文法，即自动机的每一步转化都是确定，且不存在空转移的情况。
 * 
 * @author keepf
 *
 */
public class DPDA extends PDA {
	/**
	 * 初始化它的转换规则<br />
	 * <NowState, MatchSymbol, NowPushStackTop, NextState, NextPushStackTop>
	 * <br />
	 * 翻译GNF文法作为下推机的转换规则<br />
	 * 构造一个单态下推机
	 */
	public boolean translateGNF(GNFGrammar grammars) {
		boolean res = true;
		// TODO 获得文法项列表
		List<GrammarItem_G2> grammarsList = grammars.getItemList();
		// TODO 建立临时PDA自动机
		PDA grammarPAD = new PDA();
		State singleState = new State(true, "SingleState");
		grammarPAD.add(singleState);
		for (Iterator<GrammarItem_G2> iterator = grammarsList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 item = (GrammarItem_G2) iterator.next();
			Rule r = new Rule();
			r.NowState = singleState;
			r.NextState = singleState;
			r.NowPushStackTop = item.getLeft();
			Symbol rightFirst = item.takeRightFirstSymbol();

			// TODO 下一个状态的栈顶
			// TODO 产生式规则1：S->\N
			// TODO 产生式规则2：A->b
			// TODO 产生式规则3：A->bW
			r.MatchSymbol = rightFirst;
			List<Symbol> nextPushStackTop;
			if (null == (nextPushStackTop = item.getRightList())) {
				nextPushStackTop = new ArrayList<>();
				nextPushStackTop.add(new Symbol(Symbol.Symbol_EscapeCharacter_NULL));
			}
			r.NextPushStackTop = nextPushStackTop;

			// TODO 添加该规则
			grammarPAD.rules.add(r);
		}

		// TODO GNF文法转PDA无异常，则将临时PDA自动机更新入this对象的属性中
		if (res) {
			states = grammarPAD.states;
			rules = grammarPAD.rules;
		}

		return res;
	}
}
