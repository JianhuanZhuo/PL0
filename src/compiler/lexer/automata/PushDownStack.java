package compiler.lexer.automata;

import java.util.Stack;

/**
 * <h>下推符号栈类</h><br>
 * 
 * @author keepf
 *
 */
public class PushDownStack {

	/**
	 * 下推符号栈
	 */
	Stack<Symbol> symbolStack;

	Symbol startSymbol;

	/**
	 * 构造一个下推栈，并压入一个起始符号作为下推自动机的起始符号
	 * 
	 * @param startSymbol
	 */
	public PushDownStack(Symbol startSymbol) {
		this.startSymbol = startSymbol;
		restart(startSymbol);
	}

	/**
	 * 重置该下推符号栈，将清空该栈，并压入一个起始符号以便下推自动机的运行
	 * 
	 * @param startSymbol
	 *            起始符号
	 */
	public void restart(Symbol startSymbol) {
		symbolStack = new Stack<>();
		symbolStack.push(startSymbol);
	}

	/**
	 * 重置该下推符号栈，将清空该栈，并压入构造函数的起始符号以便下推自动机的运行
	 */
	public void restart() {
		symbolStack = new Stack<>();
		symbolStack.push(startSymbol);
	}

	/**
	 * 获得下推栈的栈顶元素
	 * <hr>
	 * 在栈空的时候调用peek()将会抛出一个栈空异常
	 * 
	 * @return 栈顶元素
	 */
	public Symbol getStackTop() {
		return symbolStack.peek();
	}
	
	public Symbol pop() {
		return symbolStack.pop();
	}
	
	public Symbol push(Symbol item) {
		return symbolStack.push(item);
	}
}
