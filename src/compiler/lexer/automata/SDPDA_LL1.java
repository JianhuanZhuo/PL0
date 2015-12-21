package compiler.lexer.automata;

import java.util.Iterator;

import compiler.lexer.gammar.GrammarItemList_LL1;

/**
 * <h1>使用于LL(1)文法的单态确定型下推自动机子类</h1><br>
 * 该类实现了语法树生产业务、LL1文法转换
 * 
 * @author keepf
 *
 */
public class SDPDA_LL1 extends SDPDA {

	/**
	 * 初始化它的转换规则<br />
	 * 
	 * @return
	 */
	public boolean translateLL1(GrammarItemList_LL1 gLL1) {

		boolean res = true;
		// TODO 建立临时规则表
		RuleSet<SDPDARule_LL1> rules = new RuleSet<SDPDARule_LL1>();

		// TODO GNF文法转PDA无异常，则将临时PDA自动机更新入this对象的属性中
		if (res) {
			ruleSet = rules;
			pdStack = new PushDownStack(grammarsList.getStartSymbol());
		}

		return res;
	}

	/**
	 * 替换下推栈状态
	 */
	@Override
	public void replaceStack(SDPDARule r, Symbol input) {
		// TODO 下推栈状态替换
		pdStack.pop();
		for (Iterator<Symbol> iterator = r.nextStackTop.iterator(); iterator.hasNext();) {
			Symbol symbolInList = (Symbol) iterator.next();
			pdStack.push(symbolInList);
		}
	}

	@Override
	public void restart() {
		super.restart();
	}
}
