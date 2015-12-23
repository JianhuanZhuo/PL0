package compiler.lexer.automata;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import compiler.lexer.gammar.GNFGrammar;
import compiler.lexer.gammar.GrammarItem_G2;
import compiler.paser.syntaxTree.SyntaxNode;
import compiler.paser.syntaxTree.SyntaxTree;

/**
 * PDA, PushDown Automata <br />
 * <b>下推自动机</b><br>
 * 使用该类时，应该先使用translateGNF()函数将GNF文法(A->aB*)训练进规则集中<br>
 * 使用restart()函数初始化自动机，并返回一个只含有根节点的语法树的对象<br >
 * 使用step()函数进行步进转化，转化的结果将存储在restart()函数返回的语法树中
 * 
 * @author keepf
 *
 */
public class PDA extends StateAutomata {

	/**
	 * 下推符号栈<br/>
	 * 
	 */
	Stack<Symbol> pushStack = new Stack<Symbol>();

	/**
	 * 初始栈顶符号
	 */
	Symbol startStackTop;

	/**
	 * 动作转换映射集
	 */
	RuleSet_NONONONONO rules = new RuleSet_NONONONONO();

	/**
	 * 构造下推自动机
	 */

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
			MultiRule r = new MultiRule();
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

	/**
	 * 识别
	 */

	/**
	 * 重置该PDA自动机 <br />
	 * 将清空下推栈为初始栈顶符号，将状态置为起始状态
	 */
	public SyntaxTree restart() {
		pushStack.removeAllElements();
		Symbol startSymbol = (Symbol) startStackTop.clone();
		pushStack.add(startSymbol);
		currentStm = startStm;
		// TODO 设置相关节点
		return new SyntaxTree(new SyntaxNode(startSymbol));
	}

	// syntaxTree;

	/**
	 * 
	 * 单步识别<br />
	 * 无法做一个比较好的识别？<br />
	 * 在非终结符替换时，进行输出？ <br>
	 * 输出的形式将生成一个语法树
	 * 
	 * @param s
	 *            当前输入符号
	 * @return
	 */
	public boolean step(Symbol s) {
		boolean res = true;
		MultiRule r = new MultiRule();
		try {
			r.NowState = currentStm;
			r.NowPushStackTop = pushStack.peek();
			r.MatchSymbol = s;
			// TODO 查询该条件是否
			//
			//
			//
			//
			r = (MultiRule) rules.indexRule(r);
			//
			//
			//
			//
			if (r == null) {
				// TODO 无规则匹配，错误！
				throw new RuntimeException("无规则匹配，错误！");
			}
			// TODO 生成语法树
			SyntaxNode nodeNow = r.NowPushStackTop.getRelateNode();
			nodeNow.addChild(new SyntaxNode(s));
			// TODO 状态跳转
			currentStm = r.NextState;
			// TODO 下推栈状态替换
			pushStack.pop();
			for (Iterator<Symbol> iterator = r.NextPushStackTop.iterator(); iterator.hasNext();) {
				Symbol symbolInList = (Symbol) iterator.next();
				pushStack.push(symbolInList);
				nodeNow.addChild(new SyntaxNode(symbolInList));
			}
		} catch (EmptyStackException e) {
			e.printStackTrace();
			res = false;
		}
		return res;
	}

	public static void main(String[] args) {

	}
}
