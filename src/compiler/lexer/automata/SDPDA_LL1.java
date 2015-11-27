package compiler.lexer.automata;

import java.util.Iterator;

/**
 * <h1>使用于LL(1)文法的单态确定型下推自动机子类</h1><br>
 * 该类实现了语法树生产业务、LL1文法转换
 * 
 * @author keepf
 *
 */
public class SDPDA_LL1 extends SDPDA {

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
