package compiler.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compiler.lexer.accidence.Classifier;
import compiler.lexer.automata.Symbol;
import compiler.lexer.gammar.CFGFormlize;
import compiler.lexer.gammar.CFGrammar;
import compiler.lexer.gammar.GrammarItemList_LL1;
import compiler.lexer.gammar.GrammarItem_G2;

public class GrammarTransform {

	/**
	 * 标点符号集
	 */
	Map<String, Integer> punctuatorSet = new HashMap<>();

	/**
	 * 符号计数器
	 */
	int punctCount = 0;

	/**
	 * 关键字集
	 */
	Set<String> keywordSet = new HashSet<>();

	/**
	 * 检索符号对应的值，首次检索的符号将加入符号集中
	 * 
	 * @param p
	 *            欲检索的符号
	 * @return 符号对应的值
	 */
	private int lookupPunctNum(String p) {
		Integer res = punctuatorSet.get(p);
		if (null == res) {
			punctuatorSet.put(p, new Integer(++punctCount));
		} else {
			return res.intValue();
		}
		return punctCount;
	}

	public GrammarItemList_LL1 transform() {

		// TODO 把Parser_PL0_correct解析，并做成文法项
		String[] gs = CFGFormlize.loadGrammarsFile("Parser_test_transform");
		Pattern p = Pattern.compile("\\<@([\\S]+?)\\$\\>");
		for (int i = 0; i < gs.length; i++) {
			String s = gs[i];
			Matcher m = p.matcher(s);
			while (m.find()) {
				gs[i] = new StringBuffer(s)
						.replace(m.start(), m.end(), "<punctuator" + lookupPunctNum(m.group(1)) + ">").toString();
				s = gs[i];
				m = p.matcher(s);
			}
		}

		p = Pattern.compile("<keyword-([a-z]+)>");
		for (String g : gs) {
			Matcher m = p.matcher(g);
			while (m.find()) {
				keywordSet.add(m.group(1));
			}
		}
		gs = CFGFormlize.formalize(gs);
		CFGrammar constantG = new CFGrammar("program");
		constantG.add(gs);
		GrammarItemList_LL1 gl = new GrammarItemList_LL1(constantG.getItemList());

		
		// TODO 将punctuator-token-GrammarItem加入
		for (Entry<String, Integer> e : punctuatorSet.entrySet()) {
			Vector<Symbol> rightList = new Vector<>();
			rightList.add(new Symbol(e.getKey()));
			GrammarItem_G2 item = new GrammarItem_G2(new Symbol("punctuator"+lookupPunctNum(e.getKey()), true), rightList);
			gl.add(item);
		}

		// TODO 将keyword-token-grammaritem加入
		for (String s : keywordSet) {
			Vector<Symbol> rightList = new Vector<>();
			rightList.add(new Symbol(s));
			GrammarItem_G2 item = new GrammarItem_G2(new Symbol("keyword-"+s, true), rightList);
			gl.add(item);
		}
		
		//TODO 添加ident与number
		Vector<Symbol> rightList = new Vector<>();
		rightList.add(new Symbol(Classifier.getCategoryName(Classifier.TOKEN_IDENT)));
		GrammarItem_G2 item = new GrammarItem_G2(new Symbol("ident", true), rightList);
		gl.add(item);

		rightList = new Vector<>();
		rightList.add(new Symbol(Classifier.getCategoryName(Classifier.TOKEN_CONSTANT)));
		item = new GrammarItem_G2(new Symbol("number", true), rightList);
		gl.add(item);
		return gl;
	}

	public static void main(String[] args) {
		System.out.println(new GrammarTransform().transform());;
	}
}
