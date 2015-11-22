package com.e23.lexer;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类是用于表示3型文法（正规文法）和它的其他形式 <br />
 * 2型文法将不适用该类的表示
 * 
 * @author keepf
 *
 */
public class LexerDescriptor {

	public static final String Grammar2_MatchExpression = "^\\s*\\<(?<ident>[\\w\\d]*)\\>\\s*-\\>\\s*(?<VT>(\\\\)?[\\d\\w])(\\<(?<VN>[\\w\\d]*)\\>\\s*)?";
	public static final String Grammar_EscapeCharacter_NULL = "\\E"; // 表示空转义的那个字符

	/**
	 * 有穷自动机类，作为3型文法的内部存储形式 <br />
	 * 其他形式将由该自动机推出
	 */
	AutoMachine autoMachine = null;

	/**
	 * 文法类，用于存储文法 <br />
	 * 用户输入缓存文法，借此生成DFA/NFA
	 */
	Grammar grammars = null;

	/**
	 * 识别五类单词成分 1. 基本字，关键字=》由标识符中进行筛选得 2. 运算符 3. 标识符 4. 常数 5. 界定符
	 * 在词法解析器中，单词分类由文法决定，即由终止状态决定
	 */

	/**
	 * 生成有穷自动机（FAM/Finite Automata Machine） <br />
	 * 生成有向图 <br />
	 * 节（带属性：起始状态/可满足状态）点表示状态，弧（带条件）表示动作，
	 */
	void generateFAM() {

	}

	/**
	 * 翻译正规式RG 1(0|1)*101
	 */

	public boolean translateGR(String[] grammars) {

		boolean res = false;

		if (null != grammars) {
			Grammar gm = new Grammar(grammars);
			res = translateGR(gm);
		}
		return res;
	}

	/**
	 * 翻译正规文法，并将翻译结果存储为内部有穷状态机
	 * 
	 * @param gm 欲翻译的正规文法
	 * @return 翻译成功，返回true，否则返回false
	 */
	public boolean translateGR(Grammar gm) {
		boolean res = false;

		if (null != gm) {
			if (gm.isRG()) {
				res = true;
				AutoMachine autoMachine = new AutoMachine();
				Pattern pattern = Pattern.compile(Grammar2_MatchExpression);
				boolean firstGrammar = true;
				for (Iterator<String> iterator = gm.iterator(); iterator.hasNext();) {
					String grammar = (String) iterator.next();
					Matcher matcher = pattern.matcher(grammar);
					if (!matcher.matches()) {
						System.err.println("Unable to matcher : " + grammar);
						res = false;
						break;
					}

					State ident = new State(false, matcher.group("ident"));
					if (firstGrammar) {
						// TODO 设置开始状态
						ident.setStart();
						firstGrammar = false;
					}
					autoMachine.add(ident);
					if (null != matcher.group("VN")) {
						State TN = new State(false, matcher.group("VN"));
						autoMachine.add(TN);
						ident.addArc(new Arc(TN, matcher.group("VT")));
					} else {
						ident.addArc(new Arc(autoMachine.getDefaultEndStm(), matcher.group("VT")));
						if (matcher.group("VT").equals(Grammar_EscapeCharacter_NULL)) {
							ident.setEnd();
						}
					}
				} // end of for()
				if (res) {
					this.grammars = gm;
					this.autoMachine = autoMachine;
				}
			}
		}
		return res;
	}

	/**
	 * <h1>翻译文法Grammar</h1> <br />
	 * <标识符> -> 表达式
	 * 
	 * @param grammars
	 *            文法类对象
	 */
	public boolean translateG(String[] grammars) {
		Pattern pattern = Pattern.compile("^\\s*\\<(?<ident>[\\w\\d]*)\\>\\s*-\\>\\s*(?<expressGroup>[\\W\\w]+)\\s*");

		for (int i = 0; i < grammars.length; i++) {
			String grammar = grammars[i];
			Matcher matcher = pattern.matcher(grammar);
			if (!matcher.matches()) {
				System.err.println("Unable to matcher : " + grammar);
				return false;
			} else {
				String ident = matcher.group("ident");
				String expressGroup = matcher.group("expressGroup");
				String[] expresses = expressGroup.split("\\|");
				for (String express : expresses) {
					express = express.trim();
					System.out.println(express);
				}

				// TODO 生成FAM的指定状态
				AutoMachine fam = new AutoMachine();
				State stm = new State(0 == i, ident);
				fam.add(stm);
			}
		}
		return true;
	}

	/**
	 * 翻译DFA/NFA定义式
	 */

	/**
	 * NFA到DFA的等价转换
	 */

	/**
	 * DFA化简
	 */

	/**
	 * 生成正规式
	 */

	/**
	 * 生成文法 <br />
	 * 在文法缓存中存文法时，将直接返回文法 <br />
	 * 否则，若存在状态机不为空，则使用状态机生产文法
	 * 
	 * @return 返回null表示生成失败，否则返回一个文法类对象
	 */
	public Grammar genGrammar() {
		Grammar res = null;

		if (null != grammars) {
			res = grammars;
		} else if (null != autoMachine) {
			res = autoMachine.generateGammar();
		}
		return res;
	}

	public static void main(String[] args) {
		String[] grammars = { "<S>->a<A>", "<S>->b<B>", "<S>->\\E", "<A>->a<B>", "<A>->b<A>", "<B>->a<S>", "<B>->b<A>",
				"<B>->\\E" };
		LexerDescriptor ss = new LexerDescriptor();
		System.out.println(ss.translateGR(grammars));
		Grammar gg = ss.genGrammar();
		for (int i = 0; i < gg.getGrammarLen(); i++) {
			System.out.println(gg.getgrammar(i));
		}
	}
}
