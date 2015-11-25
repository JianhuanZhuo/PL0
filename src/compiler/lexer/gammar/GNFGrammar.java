package compiler.lexer.gammar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class GNFGrammar {

	public static final String Grammar_MatchExpression = "^\\s*\\<(?<ident>[\\w\\d]*)\\>\\s*-\\>\\s*(?<VT>(\\\\)?[\\d\\w])(?<VN>[(\\<([\\w\\d]*)\\>)((\\\\)?[\\d\\w])(\\s)]+)?";
	private static Pattern grammarPattern;

	/**
	 * 单例模式，该文法的匹配正则式编译对象Pattern
	 * 
	 * @return 返回该文法的匹配正则式编译对象Pattern
	 */
	public static Pattern getGrammarPattern() {
		if (null == grammarPattern) {
			grammarPattern = Pattern.compile(GNFGrammar.Grammar_MatchExpression);
		}
		return grammarPattern;
	}

	/**
	 * 以字符串表的形式存储文法
	 */
	private List<String> grammarsList = new ArrayList<String>();
	private List<GrammarItem_G2> grammarItemList;
	private boolean isGNF = false;

	/**
	 * 使用一组文法字符串集初始化文法类对象<br>
	 * 该构造函数将对该文法字符串集进行模式匹配（GNF文法检查），若字符串集符合GNF文法类型的规范，则将置属性isGNF为true
	 * 
	 * @param grammars
	 *            用于初始化文法类对象的文法字符串集
	 */
	public GNFGrammar(String[] grammars) {
		isGNF = true;
		for (String grammar : grammars) {
			if (null != grammar) {
				grammarsList.add(grammar);
			}
		}
	}

	/**
	 * 添加文法字符串，将可能改变文法的类型
	 * 
	 * @param grammar
	 *            欲添加的文法
	 */
	public void add(String grammar) {
		if (true == isGNF) {
			Pattern pattern = GNFGrammar.getGrammarPattern();
			Matcher matcher = pattern.matcher(grammar);
			if (!matcher.matches()) {
				isGNF = false;
			}
		}
		grammarsList.add(grammar);
	}

	/**
	 * 检查是否为GNF文法
	 * 
	 * @return 若是GNF文法，返回true，否则返回false
	 */
	public boolean checkGNF() {

		isGNF = true;
		Pattern pattern = GNFGrammar.getGrammarPattern();
		for (Iterator<String> iterator = grammarsList.iterator(); iterator.hasNext();) {
			Matcher matcher = pattern.matcher((String) iterator.next());
			if (!matcher.matches()) {
				isGNF = false;
				break;
			}
		}
		return isGNF;
	}

	/**
	 * 获取指定索引的文法字符串
	 * 
	 * @param index
	 *            指定索引
	 * @return 文法字符串
	 */
	public String getgrammar(int index) {
		return grammarsList.get(index);
	}

	/**
	 * 获得文法字符串集的长度，即文法“行”数
	 * 
	 * @return 文法字符串集的长度
	 */
	public int getGrammarLen() {
		return grammarsList.size();
	}

	public String toString() {
		String res = "this is ";
		if (!isGNF) {
			res += "no ";
		}
		res += "a GNF grammar. \n";

		if (0 == grammarsList.size()) {
			res += "There is not any grammar string\n";
		} else {
			for (Iterator<String> iterator = grammarsList.iterator(); iterator.hasNext();) {
				res += (String) iterator.next() + "\n";
			}
		}
		return res;
	}

	/**
	 * 获得该文法的一个迭代器
	 * 
	 * @return 该文法的迭代器
	 */
	public Iterator<String> iterator() {
		return grammarsList.iterator();
	}

	/**
	 * 该函数将返回文法项表，若当前文法不存在文法项表，则进行解析，并返回
	 * 
	 * @return 文法项表
	 */
	public List<GrammarItem_G2> getItemList() {
		if (null == grammarItemList) {
			grammarItemList = new ArrayList<>();
		}
		return grammarItemList;
	}

	public boolean isGNF() {
		return isGNF;
	}

	public static void main(String[] args) {
		// TODO GNF文法正则式检查
		// Pattern pattern = GNFGrammar.getGrammarPattern();
		// Matcher matcher = pattern.matcher("<A>->a<V> ASD");
		// if (matcher.matches()) {
		// System.out.println("ss");
		// System.out.println("ident= " + matcher.group("ident"));
		// System.out.println("VT= " + matcher.group("VT"));
		// System.out.println("VN= " + matcher.group("VN"));
		// }
	}
}
