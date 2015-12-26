package compiler.parser.syntaxTree;

import compiler.lexer.automata.SDPDARule;
import compiler.lexer.automata.SDPDA_LL1;
import compiler.lexer.automata.Symbol;

/**
 * <h1>语法分析类</h1><br />
 * 
 * 在下推自动机的下推过程中分析语法，并生成语法树
 * 
 * @author keepf
 *
 */
public class SyntaxAnalyse extends SDPDA_LL1 {

	/**
	 * 替换下推栈状态
	 */
	@Override
	public void replaceStack(SDPDARule r, Symbol input) {
		// TODO 下推栈状态替换
		Symbol stackTop = r.getNowStackTop();
		super.replaceStack(r, input);
	}
}
