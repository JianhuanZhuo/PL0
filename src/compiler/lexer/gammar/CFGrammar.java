package compiler.lexer.gammar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compiler.lexer.automata.Symbol;

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
public class CFGrammar  extends Grammar {

	public static final String Grammar_PartName_Ident = "ident";
	public static final String Grammar_PartName_RightPart = "rightPart";
	public static final String Grammar_MatchExpression = "^\\s*\\<(?<" + Grammar_PartName_Ident
			+ ">[\\S]+?)\\>\\s*-\\>\\s*(?<" + Grammar_PartName_RightPart + ">[^\\n]+?)\\s*$";

	private GrammarItemList_G2 grammarItemList;

//	public static Pattern CFGRightPattern = Pattern.compile(
//			"(\\<(?<VN>[\\w-]+?)\\>)|(?<VT>(\\\\[N\\(\\)\\[\\]\\*\\+\\|\\<\\>])|([\\S&&[^\\(\\)\\[\\]\\*\\+\\|\\<\\>]]))");
	public static Pattern CFGRightPattern = Pattern.compile(
			"(\\<(?<VN>[\\S]+?)\\>)|(?<VT>(\\\\[N\\(\\)\\[\\]\\*\\+\\|\\<\\>])|([\\S&&[^\\(\\)\\[\\]\\*\\+\\|\\<\\>]]))");

	public CFGrammar() {
	}

	public CFGrammar(String start) {
		super(start);
	}

	
	/**
	 * 获得文法项列表，可能用于自动机的规则初始化
	 * 
	 * @return 文法项列表，可能转换失败则返回null
	 */
	public GrammarItemList_G2 getItemList() {
		if (null == grammarItemList) {
			grammarItemList = transform();
		}
		return grammarItemList;
	}

	/**
	 * 将文法字符串转换成文法项（规范的）<br />
	 * 即该过程是一个识别文法字符串的过程
	 * 
	 * @return
	 */
	protected GrammarItemList_G2 transform() {
		GrammarItemList_G2 tempList = new GrammarItemList_G2();
		for (int i = 0; i < getGrammarLen(); i++) {
			String s = getgrammar(i);
			String ident = getPart(s, Grammar_PartName_Ident);
			String rightPart = getPart(s, Grammar_PartName_RightPart);
			GrammarItem_G2 gItem = new GrammarItem_G2(new Symbol(ident, true));
			Matcher m = CFGRightPattern.matcher(rightPart);
			while (m.find()) {
				if (null == m.group("VN")) {
					//System.out.println(m.group("VT"));
					gItem.addRight(new Symbol(m.group("VT")));
				} else {
					//System.out.println(m.group("VN"));
					gItem.addRight(new Symbol(m.group("VN"), true));
				}
			}
			if (null == gItem.getRightFirstSymbol()) {
				gItem.addRight(new Symbol("\\N"));
			}
			tempList.add(gItem);
		}
		tempList.setStartSymbol(new Symbol(getStart(), true));
		return tempList;
	}

	/**
	 * 该文法将
	 */
	@Override
	public String getMatchExpression() {
		return Grammar_MatchExpression;
	}

	public static void main(String[] args) {

		String[] s = { "	   <constant> 				-> \\+\\<ss<integer-constant><ss>",
				"	   <constant> 				-><floating-constant>" };
		CFGrammar gm = new CFGrammar("constant");
		gm.add(s);
		System.out.println(gm.transform().toString());
	}
}
