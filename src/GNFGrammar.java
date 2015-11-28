

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compiler.lexer.automata.Symbol;

/**
 * <b>GNF文法类</b><br>
 * 格雷巴赫标准式文法(Greibach Grammar)，格式如下所示：<br >
 * 
 * <A>->a[a<B>]*;<br>
 * </b> html 格式显示为：&lt;A&gt;-&lt;a[&lt;B&gt;]*
 * 
 * <br>
 * 该文法类型将不包含产生式左递归，如A->Aa，但可能存在左共同因子<br>
 * 对于下推自动机而言，应该只有确定性的下推自动机才需要进行左共同因子提取工作，使文法类型转化为LL(1)文法<br>
 * 使用该文法生产下推自动机为 单态自动机？
 * 
 * <br>
 * 该文不允许A->A|B的或型选择，即一个文法内不允许带有或的产生式<br>
 * 一个可选的后路是将或型语句拆分为两个语句
 * 
 * @author keepf
 *
 */
public class GNFGrammar extends CFGrammar {

	public static final String Grammar_MatchExpression = "^\\s*\\<(?<ident>[\\w\\d]+)\\>\\s*-\\>\\s*(?<VT>(\\\\)?[\\S])(?<VN>[\\S]+)?";

	/**
	 * 
	 */
	@Override
	public String getMatchExpression() {
		return Grammar_MatchExpression;
	}
	/**
	 * 以字符串表的形式存储文法
	 */
	private GrammarItemList_G2 grammarItemList;
	private boolean isGNF = true;

	/**
	 * 使用一组文法字符串集初始化文法类对象<br>
	 * 该构造函数将对该文法字符串集进行模式匹配（GNF文法检查），若字符串集符合GNF文法类型的规范，则将置属性isGNF为true
	 * 
	 * @param grammars
	 *            用于初始化文法类对象的文法字符串集
	 */
	public GNFGrammar(String[] grammars) {
		super(grammars);
	}

	/**
	 * 添加文法字符串，将可能改变文法的类型
	 * 
	 * @param grammar
	 *            欲添加的文法
	 */
	@Override
	public void add(String grammar) {
		if (true == isGNF) {
			Pattern pattern = getGrammarPattern();
			Matcher matcher = pattern.matcher(grammar);
			if (!matcher.matches()) {
				isGNF = false;
			}
		}
		super.add(grammar);
	}

	/**
	 * 检查是否为GNF文法
	 * 
	 * @return 若是GNF文法，返回true，否则返回false
	 */
	public boolean checkGNF() {

		isGNF = true;
		Pattern pattern = getGrammarPattern();
		for (Iterator<String> iterator = iterator(); iterator.hasNext();) {
			Matcher matcher = pattern.matcher((String) iterator.next());
			if (!matcher.matches()) {
				isGNF = false;
				break;
			}
		}
		return isGNF;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		String res = "this is ";
		if (!isGNF) {
			res += "no ";
		}
		res += "a GNF grammar. \n";
		res += super.toString();
		return res;
	}

	/**
	 * 该函数将返回文法项表，若当前文法不存在文法项表，则进行解析，并返回
	 * 
	 * @return 文法项表
	 */
	public GrammarItemList_G2 getItemList() {
		if (!isGNF) {
			return null;
		}
		if (null == grammarItemList) {
			grammarItemList = new GrammarItemList_G2();
			Pattern pattern = getGrammarPattern();
			for (int i = 0; i < getGrammarLen(); i++) {
				String gString = getgrammar(i);
				Matcher matcher = pattern.matcher(gString);
				if (matcher.matches()) {
					System.out.println("ident = " + matcher.group("ident"));
					if (0 == i) {
						grammarItemList.setStartSymbol(new Symbol(matcher.group("ident"), true));
					}
					System.out.println("VT    = " + matcher.group("VT"));
					System.out.println("VN    = " + matcher.group("VN"));
					GrammarItem_G2 g2 = new GrammarItem_G2(new Symbol(matcher.group("ident"), true));
					g2.addRight(new Symbol(matcher.group("VT")));
					if (null != matcher.group("VN")) {
						g2.parserRight(matcher.group("VN"));
					}
					grammarItemList.add(g2);
				}
			}
		}
		return grammarItemList;
	}

	public boolean isGNF() {
		return isGNF;
	}

	public static void main(String[] args) {
		// TODO GNF文法正则式检查
//		Pattern pattern = new GNFGrammar(null).getGrammarPattern();
//		Matcher matcher = pattern.matcher("<A>->a<V> ASD");
//		if (matcher.matches()) {
//			System.out.println("ss");
//			System.out.println("ident= " + matcher.group("ident"));
//			System.out.println("VT= " + matcher.group("VT"));
//			System.out.println("VN= " + matcher.group("VN"));
//		}

		String[] grammars = { "<As>->s<sss>" };
		for (int i = 0; i < grammars.length; i++) {
			System.out.println(new GNFGrammar(null).getPart(grammars[i], "ident"));
			System.out.println(new GNFGrammar(null).getPart(grammars[i], "VN"));
		}

//		String[] grammars = { " <FOR>-> for(<3B>)", "<C>-> a <B1>a", "<B>->b" };
//		GNFGrammar gnf = new GNFGrammar(grammars);
//		System.out.println(gnf.checkGNF());
//		System.out.println(gnf.getItemList());
//		System.out.println("OK");
	}
}