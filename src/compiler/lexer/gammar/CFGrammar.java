package compiler.lexer.gammar;


/**
 * <h1>Context-Free Grammars</h1> <br>
 * 上下文无关文法，即2型文法，使用形式化的符号表示为：VN->VT V*
 * <hr>
 * <h2>标识</h2><br>
 * 此处的CFG文法中，非终结符使用尖括号将括起表示，使用单箭头->表示产生式<br>
 * 
 * <hr>
 * 该文法在此工程中用于词法单元Token的识别，
 * 
 * @author keepf
 *
 */
public class CFGrammar extends Grammar {

	public static final String Grammar_MatchExpression = "^\\s*\\<(?<ident>[\\w\\d]+)\\>\\s*-\\>\\s*(?<rightPart>[^\\n]+)\\s*$";


	/**
	 * 该文法将
	 */
	@Override
	public String getMatchExpression() {
		return Grammar_MatchExpression;
	}


	public static void main(String[] args) {

//		String[] grammars = { "<AD1>->s<AD1>s<AD  " };
//		for (int i = 0; i < grammars.length; i++) {
//			System.out.println(new CFGrammar(null).getPart(grammars[i], "ident"));
//			System.out.println(new CFGrammar(null).getPart(grammars[i], "rightPart"));
//		}
//
//		String[] res = CFGrammar.formalizeOr("<A>->sa\\|[a-c\\]");
//		for (int i = 0; i < res.length; i++) {
//			System.out.println(res[i]);
//		}
//		String[] res2 = CFGrammar.formalizeBrackets("<A>->a");
//		for (int i = 0; i < res2.length; i++) {
//			System.out.println(res2[i]);
//		}
		
	}
}
