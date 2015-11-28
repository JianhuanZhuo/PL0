package compiler.lexer.gammar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用了扩展的BNF范式<br>
 * <VN>-><VN>NT
 * 
 * <hr>
 * 此时尖括号符号<>将需要得到转义<br>
 * 允许存在()分组、|或、[a-zA-Z0-9]选择、*闭包、+正闭包，但是这是不规范的，需要通过Formlize进行规范化/形式化 <br>
 * 在检查文法时用到。 <br>
 * 在进行解析的时候用到/<br>
 * 文法将解析成文法项进行使用<br>
 * 该类将保证添加的文法字符串都是为规范的，在尝试添加非规范文法时，将被拒绝。<br>
 * 欲进行添加的文法字符串必须又CFGFormlize类进行规范化之后才能添加。
 * 
 * @author keepf
 *
 */
public class Grammar {

	/**
	 * 获得单一实例
	 */
	private static Grammar constant;

	public static Grammar getConstant() {
		if (null == constant) {
			constant = new Grammar();
		}
		return constant;
	}

	/**
	 * 以字符串表的形式存储文法
	 */
	private List<String> grammarsList = new ArrayList<String>();

	private static String Grammar_MatchExpression = "^\\s*(?<left>[^\\n]+)\\s*-\\>\\s*(?<rightPart>[^\\n]+)\\s*$";

	private static Pattern grammarPattern;
	// private static Pattern grammarPattern =
	// Pattern.compile(Grammar_MatchExpression);

	public String getMatchExpression() {
		return Grammar_MatchExpression;
	}

	/**
	 * 单例模式，该文法的匹配正则式编译对象Pattern<br>
	 * 仅给子类用
	 * 
	 * @return 返回该文法的匹配正则式编译对象Pattern
	 */
	protected Pattern getGrammarPattern() {
		if (null == grammarPattern) {
			grammarPattern = Pattern.compile(getMatchExpression());
		}
		return grammarPattern;
	}

	public String getPart(String grammar, String ident) {
		String res = null;
		Pattern p = getGrammarPattern();
		Matcher matcher = p.matcher(grammar);
		if (matcher.matches()) {
			res = matcher.group(ident).trim();
		}
		return res;
	}

	public boolean match(String g) {
		g = CFGFormlize.hasFormlize(g);
		if (null != g && getGrammarPattern().matcher(g).matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 使用一组文法字符串集初始化文法类对象<br>
	 * 该构造函数将对该文法字符串集进行模式匹配（GNF文法检查），若字符串集符合GNF文法类型的规范，则将置属性isGNF为true
	 * 
	 * @param grammars
	 *            用于初始化文法类对象的文法字符串集
	 */
	public Grammar() {
	}

	/**
	 * 添加文法字符串，将可能改变文法的类型<br>
	 * 若继承该类，应该在子类中进行检查<br>
	 * 该处将进行一个是否规范化的检查
	 * 
	 * @param g
	 *            欲添加的文法
	 */
	public boolean add(String g) {
		if (match(g)) {
			grammarsList.add(g);
			return true;
		}
		return false;
	}

	public boolean add(String[] gs) {
		if (null == gs) {
			return false;
		}
		for (int i = 0; i < gs.length; i++) {
			if (!match(gs[i])) {
				return false;
			}
		}

		// TODO 检查通过
		for (int i = 0; i < gs.length; i++) {
			grammarsList.add(gs[i]);
		}
		return true;
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
		String res = "";
		if (grammarsList.size() > 0) {
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
	 * 检查文法是否符号规范
	 * 
	 * @return
	 */
	// public boolean checkGrammar() {
	// Pattern p = getGrammarPattern();
	// for (Iterator<String> iterator = iterator(); iterator.hasNext();) {
	// String s = (String) iterator.next();
	// Matcher matcher = p.matcher(s);
	// if (!matcher.matches()) {
	// return false;
	// }
	// }
	// return true;
	// }

	public static void main(String[] args) {
		String[] s ={
				"	   <constant> 				-> <integer-constant>",
				"	   <constant> 				-> <floating-constant>",
				"	   <constant> 				-> <enumeration-constant>",
				"	   <constant> 				-> <character-constant>",
				"	   <integer-constant>		-> <decimal-constant><integer-suffix>",
				"	   <integer-constant>		-> <decimal-constant>",
				"	   <integer-constant>		-> <octal-constant><integer-suffix>",
				"	   <integer-constant>		-> <octal-constant>",
				"	   <integer-constant>		-> <hexadecimal-constant><integer-suffix>",
				"	   <integer-constant>		-> <hexadecimal-constant>",
				"	   <decimal-constant>		-> <nonzero-digit>",
				"	   <decimal-constant>		-> <decimal-constant><digit>",
				"	   <octal-constant>			-> 0",
				"	   <octal-constant>			-> <octal-constant><octal-digit>",
				"	   <hexadecimal-constant>	-> <hexadecimal-prefix><hexadecimal-digit>",
				"	   <hexadecimal-constant>	-> <hexadecimal-constant><hexadecimal-digit>",
				"	   <hexadecimal-prefix>		-> 0x",
				"	   <hexadecimal-prefix>		-> 0X",
				"	   <nonzero-digit>			-> \\[1-9\\]",
				"	   <octal-digit>			-> \\[0-7\\]",
				"	   <hexadecimal-digit>		-> \\[0-9\\]\\[a-f\\]\\[A-F\\]",
				"	   <integer-suffix>			-> <unsigned-suffix><long-suffix>",
				"	   <integer-suffix>			-> <unsigned-suffix>",
				"	   <integer-suffix>			-> <unsigned-suffix><long-suffix>",
				"	   <integer-suffix>			-> <unsigned-suffix><long-long-suffix>",
				"	   <integer-suffix>			-> <long-suffix>",
				"	   <integer-suffix>			-> <long-suffix><unsigned-suffix>",
				"	   <integer-suffix>			-> <long-long-suffix>",
				"	   <integer-suffix>			-> <long-long-suffix><unsigned-suffix>",
				"	   <unsigned-suffix>		-> u",
				"	   <unsigned-suffix>		-> U",
				"	   <long-suffix>			-> l",
				"	   <long-suffix>			-> L",
				"	   <long-long-suffix>		-> ll",
				"	   <long-long-suffix>		-> LL"
		};
		Grammar gm = new Grammar();
		System.out.println(gm.add(s));
	}
}
