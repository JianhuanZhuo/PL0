package compiler.lexer.accidence;

import java.util.Set;

import compiler.lexer.automata.SDPDARule;
import compiler.lexer.automata.SDPDARule_LL1;
import compiler.lexer.automata.SDPDA_LL1;
import compiler.lexer.automata.Symbol;

/**
 * <h1>分类器</h1><br />
 * 
 * <ol>
 * <li>keyword</li>
 * <li>identifier</li>
 * <li>constant</li>
 * <li>string-literal</li>
 * <li>punctuator</li>
 * </ol>
 * 
 * @author keepf
 *
 */
public class Classifier extends SDPDA_LL1 {

	Symbol t = null;
	public static final int I_DO_KNOW = 0;
	public static final int TOKEN_IDENT = 1;
	public static final int TOKEN_CONSTANT = 2;
	public static final int TOKEN_PUNCTUATOR = 3;
	public static final int TOKEN_KEYWORD = 4;
	public static final int GRY = 5;

	Keyword kw;

	public Classifier(Keyword kw) {
		this.kw = kw;
	}

	/**
	 * 替换下推栈状态
	 */
	@Override
	public void replaceStack(SDPDARule r, Symbol input) {
		// TODO 下推栈状态替换
		Symbol stackTop = r.getNowStackTop();
		if (stackTop.equals(pdStack.getStartSymbol())) {
			t = r.getNextStackTop().get(0);
		}
		super.replaceStack(r, input);
	}

	public int getTokenCategory(String content) {
		if (content.trim().equals("")) {
			return GRY;
		}
		if (t.equals(new Symbol("constant", true))) {
			return TOKEN_CONSTANT;
		} else if (t.equals(new Symbol("punctuator", true))) {
			return TOKEN_PUNCTUATOR;
		} else if (t.equals(new Symbol("identifier", true))) {
			// TODO 从标识符中分离出关键字
			if (kw.isKeyword(content)) {
				return TOKEN_KEYWORD;
			} else {
				return TOKEN_IDENT;
			}
		}
		return I_DO_KNOW;
	}

	public static String getCategoryName(int i) {
		switch (i) {
		case 0:
			return "I_DO_KNOW";
		case 1:
			return "TOKEN_IDENT";
		case 2:
			return "TOKEN_CONSTANT";
		case 3:
			return "TOKEN_PUNCTUATOR";
		case 4:
			return "TOKEN_KEYWORD";
		case 5:
			return "GRY";
		default:
			break;
		}
		return null;
	}
}
