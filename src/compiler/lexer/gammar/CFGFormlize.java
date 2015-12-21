package compiler.lexer.gammar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对带有[a-zA-Z0-9]、括号()、或选择|
 * 
 * @author keepf
 *
 */
public class CFGFormlize {

	/**
	 * 用于在符号检查时，替换一个转义符号
	 */
	private static char REPLACE_CHAR = '!';
	private static char[] REPLACE_CHAR_CLOSE = { 0x01 }; // *的替换符，\*
	private static char[] REPLACE_CHAR_LSQUARE = { 0x02 }; // [的替换符，\[
	private static char[] REPLACE_CHAR_RSQUARE = { 0x03 }; // ]的替换符，\]
	private static char[] REPLACE_CHAR_OR = { 0x04 }; // |的替换符，\|
	private static char[] REPLACE_CHAR_LPRES = { 0x05 }; // (的替换符，\(
	private static char[] REPLACE_CHAR_RPRES = { 0x06 }; // )的替换符，\)
	private static char[] REPLACE_CHAR_CLOSE_P = { 0x07 }; // +的替换符，\+
	private static char[] REPLACE_CHAR_ESC = { 0x1B }; // \的替换符，\\

	private static int tempCount = 0;

	public static int getTempCount() {
		return tempCount++;
	}

	/**
	 * 对CFG文法进行形式化<br>
	 * 该函数仅对CFG文法中的[a-z][A-Z][0-9]|处理，使其转换为形式化的CFG文法<br>
	 * 对于
	 * 
	 * @param grammars
	 *            欲形式化的CFG文法
	 * @return 转换成功返回形式化后的文法，否则返回null
	 */
	static String[] formalize(String[] grammars) {
		String[] res = null;
		List<String> fGrammars = new ArrayList<>();
		for (int i = 0; i < grammars.length; i++) {
			fGrammars.add(grammars[i]);
		}
		int count = 0;
		while (count < fGrammars.size()) {
			count = fGrammars.size();
			for (int j = 0; j < fGrammars.size(); j++) {
				String gs = fGrammars.get(j);
				String[] resGrammars = formalizeGroup(gs);
				if (null != resGrammars) {
					fGrammars.remove(gs);
					for (int i = 0; i < resGrammars.length; i++) {
						fGrammars.add(resGrammars[i]);
					}
					j--;
					continue;
				}
				resGrammars = formalizeOr(gs);
				if (null != resGrammars) {
					fGrammars.remove(gs);
					for (int i = 0; i < resGrammars.length; i++) {
						fGrammars.add(resGrammars[i]);
					}
					j--;
					continue;
				}
				resGrammars = formalizeBrackets(gs);
				if (null != resGrammars) {
					fGrammars.remove(gs);
					for (int i = 0; i < resGrammars.length; i++) {
						fGrammars.add(resGrammars[i]);
					}
					j--;
					continue;
				}
			}
		}
		res = fGrammars.toArray(new String[fGrammars.size()]);
		return res;
	}

	/**
	 * 对于下面的情况将：<br>
	 * "<A>->a\\[1-2\\]b\\[1-2\\]"<br>
	 * 分解为： 只允许[a-z]
	 * 
	 * @param g
	 * @return
	 */
	static String[] formalizeBrackets(String g) {
		String[] res = null;
		List<String> fGrammars = new ArrayList<>();
		CFGrammar simple = new CFGrammar();
		String sp = simple.getPart(g, "rightPart");
		if (null == sp) {
			System.err.println("文法不匹配：" + g);
			return null;
		}
		sp = escapeChar(sp.trim());
		g = simple.getPart(g, "ident");
		Pattern p = Pattern.compile("(?<front>[\\S\\s]*?)(\\[(?<centerS>\\w)-(?<centerE>\\w)\\])(?<end>[\\S\\s]*?)");
		Matcher m = p.matcher(sp);
		if (m.matches()) {
			// System.out.println(m.group("front"));
			// System.out.println(m.group("centerS"));
			// System.out.println(m.group("centerE"));
			// System.out.println(m.group("end"));
			char s = m.group("centerS").charAt(0);
			char e = m.group("centerE").charAt(0);
			if ((('a' <= s && e <= 'z') || ('A' <= s && e <= 'Z') || ('0' <= s && e <= '9')) && (s < e)) {
				String headStr = "<" + g + ">->" + m.group("front");
				headStr = reEscape(headStr);
				int index = headStr.length();
				StringBuffer sb = new StringBuffer(headStr + "0" + m.group("end"));
				for (char c = s; c <= e; c++) {
					sb.setCharAt(index, c);
					fGrammars.add(sb.toString());
				}
			}
		}
		if (!fGrammars.isEmpty()) {
			res = new String[fGrammars.size()];
			res = fGrammars.toArray(res);
		}
		return res;
	}

	/**
	 * 将使用‘|’的文法形式化表示
	 * 
	 * <pre>
	 * "&lt;A>->s\\|\\|a\\|"
	 * 
	 * 上述文法在分解后将得到：
	 * 
	 * &lt;A>->s
	 * &lt;A>->\N
	 * &lt;A>->a
	 * &lt;A>->\N
	 * </pre>
	 * 
	 * @param g
	 * @return
	 */
	static String[] formalizeOr(String g) {
		String[] res = null;
		CFGrammar simple = new CFGrammar();
		String sp = simple.getPart(g, "rightPart");
		if (null == sp) {
			System.err.println("文法不匹配：" + g);
			return null;
		}
		g = "<" + simple.getPart(g, "ident") + ">->";
		sp = sp.trim();
		// TODO 将可能引起干扰的"\|"转义字符进行替换
		sp = escapeChar(sp) + " ";
		res = sp.split("\\|");
		if (res.length > 1) {

			for (int i = 0; i < res.length; i++) {
				// TODO 将字符替换回
				res[i] = reEscape(res[i]);
				if (res[i].equals("")) {
					res[i] = g + "\\N";
				} else {
					res[i] = g + res[i];
				}
			}
		} else {
			res = null;
		}

		return res;
	}

	/**
	 * 括号
	 * 
	 * @param grammars
	 * @return
	 */
	static String[] formalizeGroup(String g) {
		//System.out.println("g : "+g);
		String[] res = null;
		List<String> fGrammars = new ArrayList<>();
		CFGrammar simple = new CFGrammar();
		String sp = simple.getPart(g, "rightPart");
		if (null == sp) {
			System.err.println("文法不匹配：" + g);
			return null;
		}
		g = "<" + simple.getPart(g, "ident") + ">->";
		sp = escapeChar(sp);
		//System.out.println("sp : "+sp);
		int leftCount = 0;
		int beginIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < sp.length(); i++) {
			char c = sp.charAt(i);
			switch (c) {
			case '(':
				if (0 == leftCount) {
					beginIndex = i;
				}
				leftCount++;
				break;
			case ')':
				leftCount--;
				if (0 == leftCount) {
					endIndex = i;
					// TODO 跳出for循环
					int ctp = getTempCount();
					String sub = "<TEMP_VN_" + ctp + ">->" + reEscape(sp.substring(beginIndex + 1, endIndex));
					//System.out.println("sub : "+sub);
					String[] subStr = formalizeGroup(sub);
					if (null == subStr) {
						fGrammars.add(sub);
					} else {
						for (int j = 0; j < subStr.length; j++) {
							fGrammars.add(subStr[j]);
						}
					}
					
					//System.out.println("reEscape   : "+reEscape(sp));
					sp = new StringBuffer(sp).replace(beginIndex, endIndex + 1, "<TEMP_VN_" + ctp + ">")
							.toString();
					//System.out.println("replace sp : "+sp);
					i = 0;
				}
				break;
			default:
				break;
			}
		} // end of for()

		sp = reEscape(sp);
		
		if (!fGrammars.isEmpty()) {
			fGrammars.add(g + sp);
			res = fGrammars.toArray(new String[fGrammars.size()]);
		}

		return res;
	}

	/**
	 * 将输入字符串sp进行转义，顺序必须是先进行Esc转义。
	 * 
	 * @param sp
	 *            欲进行转义的字符
	 * @return 转义后的字符
	 */
	public static String escapeChar(String sp) {
		sp = sp.replaceAll("\\\\\\\\", new String(REPLACE_CHAR_ESC));
		sp = sp.replaceAll("\\\\\\(", new String(REPLACE_CHAR_LPRES));
		sp = sp.replaceAll("\\\\\\)", new String(REPLACE_CHAR_RPRES));
		sp = sp.replaceAll("\\\\\\[", new String(REPLACE_CHAR_LSQUARE));
		sp = sp.replaceAll("\\\\\\]", new String(REPLACE_CHAR_RSQUARE));
		sp = sp.replaceAll("\\\\\\+", new String(REPLACE_CHAR_CLOSE_P));
		sp = sp.replaceAll("\\\\\\*", new String(REPLACE_CHAR_CLOSE));
		sp = sp.replaceAll("\\\\\\|", new String(REPLACE_CHAR_OR));
		return sp;
	}

	/**
	 * 将被替换后的转义字符进行还原
	 * 
	 * @param sp
	 *            欲进行还原的字符串
	 * @return 还原后的字符串
	 */
	public static String reEscape(String sp) {
		sp = sp.replaceAll(new String(REPLACE_CHAR_OR), "\\\\\\|");
		sp = sp.replaceAll(new String(REPLACE_CHAR_LPRES), "\\\\\\(");
		sp = sp.replaceAll(new String(REPLACE_CHAR_RPRES), "\\\\\\)");
		sp = sp.replaceAll(new String(REPLACE_CHAR_LSQUARE), "\\\\\\[");
		sp = sp.replaceAll(new String(REPLACE_CHAR_RSQUARE), "\\\\\\]");
		sp = sp.replaceAll(new String(REPLACE_CHAR_CLOSE), "\\\\\\*");
		sp = sp.replaceAll(new String(REPLACE_CHAR_CLOSE_P), "\\\\\\+");
		sp = sp.replaceAll(new String(REPLACE_CHAR_ESC), "\\\\").trim();
		return sp;
	}

	/**
	 * 是否已进行规范化，将检查文法是否包含不规范的用法。<br>
	 * 这里的不规范用法是指存在不规范的转义，如()分组、|或、[a-zA-Z0-9]选择、*闭包、+正闭包
	 * 
	 * <hr>
	 * 
	 * 此处需要注意的是，不检查正常转义符<>是否正常搭配使用<br>
	 * 返回的无干扰的文法字符串将<b>仅</b>用于匹配文法类型。
	 * 
	 * @param g
	 *            欲进行检查的文法字符串
	 * @return 规范返回无干扰的文法字符串，否则返回null
	 */
	public static String hasFormlize(String g) {
		if (null == g) {
			return null;
		}
		StringBuffer sb = new StringBuffer(g);
		for (int i = 0; i < sb.length(); i++) {
			try {
				switch (sb.charAt(i)) {
				// TODO 存在转义字符：
				case '(':
				case ')':
				case '*':
				case '+':
				case '[':
				case ']':
				case '|':
					if ('\\' != sb.charAt(i - 1)) {
						return null;
					} else {
						sb.delete(i - 1, i + 1);
						sb.insert(i - 1, REPLACE_CHAR);
						i--;
					}
					break;

				// TODO 正常的转义：<、>、\、N(空)
				case '<':
				case '>':
				case '\\':
				case 'N':
					if ('\\' == sb.charAt(i - 1)) {
						sb.delete(i - 1, i + 1);
						sb.insert(i - 1, REPLACE_CHAR);
						i--;
					}
					break;

				default:
					if ('\\' == sb.charAt(i - 1)) {
						return null;
					}
					break;
				}
			} catch (IndexOutOfBoundsException e) {
				// TODO 第一个字符就会发生下溢
			}
		}
		return sb.toString();
	}

	public static String[] loadGrammarsFile(String fileName) {
		List<String> grammars = new ArrayList<>();
		BufferedReader bfReader = null;
		try {
			bfReader = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			while ((line = bfReader.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) != '#') {
					grammars.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bfReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return grammars.toArray(new String[grammars.size()]);
	}

	public static void main(String[] args) {
		// System.out.println(CFGFormlize.hasFormlize("<a> ->\\\\\\*d"));
//		String[] kk = CFGFormlize.formalizeOr("<nonzeroDigit>->[1-9]|2");
		// for (int i = 0; i < kk.length; i++) {
		// System.out.println(kk[i]);
		// }
		// kk = CFGFormlize.formalizeBrackets("<nonzeroDigit>->[1-9]");
		// for (int i = 0; i < kk.length; i++) {
		// System.out.println(kk[i]);
		// }
		//
//		kk = CFGFormlize.formalizeGroup("<a> ->\\[sdd\\(x(x\\)xsx)sdf(x)\\\\");
//		for (int i = 0; i < kk.length; i++) {
//			System.out.println(kk[i]);
//		}
		//
		// String[] ss = { "<identifier> -> <nondigit>", "<identifier> ->
		// <identifier><digit>",
		// "<nondigit> -> _|[a-z]|[A-Z]", "<digit> -> [0-9]" };
		// ss = CFGFormlize.formalize(ss);
		// for (int i = 0; i < ss.length; i++) {
		// System.out.println(ss[i]);
		// }
		String[] tokenGrammars = CFGFormlize.loadGrammarsFile("PL0");
		// String[] tokenGrammars =CFGFormlize.loadGrammarsFile("Token_LL1");
		tokenGrammars = CFGFormlize.formalize(tokenGrammars);
		for (int i = 0; i < tokenGrammars.length; i++) {
			System.out.println(tokenGrammars[i]);
		}
	}

}
