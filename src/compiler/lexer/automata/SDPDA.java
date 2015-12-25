package compiler.lexer.automata;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;

import compiler.lexer.gammar.CFGrammar;
import compiler.lexer.gammar.GrammarItemList_G2;
import compiler.lexer.gammar.GrammarItem_G2;

/**
 * <h1>单态的确定型下推自动机</h1><br>
 * 映射规则：(a,T1)->(T2)<br>
 * 
 * @author keepf
 *
 */
public class SDPDA extends Automata {

	/**
	 * 确定下推规则集
	 */
	RuleSet ruleSet;

	/**
	 * 下推栈
	 */
	protected PushDownStack pdStack;

	/**
	 * 初始化它的转换规则<br />
	 * <matchSymbol, nowStackTop, nextStackTop> <br />
	 * 翻译GNF文法作为下推机的转换规则<br />
	 * 构造一个单态下推机
	 */
	public boolean translateGNF(CFGrammar grammars) {
		boolean res = true;
		// TODO 获得文法项列表
		GrammarItemList_G2 grammarsList = grammars.getItemList();
		// TODO 建立临时规则表
		RuleSet rules = new RuleSet();
		for (Iterator<GrammarItem_G2> iterator = grammarsList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 item = (GrammarItem_G2) iterator.next();
			SDPDARule r = new SDPDARule();
			r.nowStackTop = item.getLeft();
			Symbol rightFirst = item.takeRightFirstSymbol();

			// TODO 下一个状态的栈顶
			// <matchSymbol, nowStackTop, nextStackTop>
			// TODO 产生式规则1：S->\N == <\N,S,\N>
			// TODO 产生式规则2：A->b == <b ,A,\N>
			// TODO 产生式规则3：A->bW == <b ,A, W>
			r.matchSymbol = rightFirst;
			List<Symbol> nextPushStackTop;
			if (null == (nextPushStackTop = item.getRightList())) {
				nextPushStackTop = new ArrayList<>();
				nextPushStackTop.add(new Symbol("\\#"));
			}
			r.setNextStackTop(nextPushStackTop);
			// TODO 添加该规则
			rules.add(r);
		}

		// TODO GNF文法转PDA无异常，则将临时PDA自动机更新入this对象的属性中
		if (res) {
			ruleSet = rules;
			pdStack = new PushDownStack(grammarsList.getStartSymbol());
		}

		return res;
	}

	/**
	 * 
	 * 单步识别<br />
	 * 无法做一个比较好的识别？<br />
	 * 在非终结符替换时，进行输出？ <br>
	 * 输出的形式将生成一个语法树
	 * 
	 * @param input
	 *            当前输入符号
	 * @return
	 */
	@Override
	public boolean step(Symbol input) {
		SDPDARule r = new SDPDARule();

		if (isEmpty()) {
			return true;
		}

		try {
			r.nowStackTop = pdStack.getStackTop();
			r.matchSymbol = input;
			// TODO 查询该条件是否
			if (null == (r = ruleSet.indexRule(r))) {
				// 无规则，先尝试一下是否有空匹配
				r = new SDPDARule();
				r.nowStackTop = pdStack.getStackTop();
				r.matchSymbol = new Symbol("\\#");
				if (null == (r = ruleSet.indexRule(r))) {
					// TODO 无规则匹配，错误！
					throw new RuntimeException("无规则匹配，错误！");
				}else {
					return true;
				}
			}
			// TODO 替换下推栈状态
			replaceStack(r, input);
		} catch (EmptyStackException e) {
			throw new RuntimeException("error for empty stack!");
		}
		return false;
	}

	/**
	 * 检查栈是否为空
	 * 
	 * @return 空返回true，非空返回false
	 */
	public boolean isEmpty() {
		return pdStack.isEmpty();
	}

	/**
	 * 下推栈状态替换
	 * 
	 * @param r
	 *            用于此次替换的单态确定型下推规则
	 * @param input
	 *            在此次步进中的输入符号
	 */
	public void replaceStack(SDPDARule r, Symbol input) {
		// TODO 下推栈状态替换
		pdStack.pop();
		for (Iterator<Symbol> iterator = r.getNextStackTop().iterator(); iterator.hasNext();) {
			Symbol symbolInList = (Symbol) iterator.next();
			pdStack.push(symbolInList);
		}
	}

	/**
	 * 重置单态的确定型自动机，主要为重置下推栈的状态
	 */
	@Override
	public void restart() {
		pdStack.restart();
	}
	
	public static void main(String[] args) {
		
	}
}
