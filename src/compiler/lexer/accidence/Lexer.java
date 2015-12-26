package compiler.lexer.accidence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import compiler.lexer.automata.SDPDA_LL1;
import compiler.lexer.automata.Symbol;
import compiler.lexer.gammar.CFGFormlize;
import compiler.lexer.gammar.CFGrammar;
import compiler.lexer.gammar.FirstSet;
import compiler.lexer.gammar.FollowSet;
import compiler.lexer.gammar.GrammarItemList_LL1;
import compiler.lexer.gammar.GrammarItem_G2;
import compiler.lexer.gammar.SelectSet;

/**
 * <h1>词法分析器</h1><br />
 * 过滤空白，换行，记录行数（错误时，赋予行号）<br />
 * 
 * 建立一个输入缓冲，（可能为一个双缓冲机制）<br />
 * 
 * <hr />
 * 
 * <h2>输入</h2><br />
 * 
 * 1. 词法<br />
 * 2. 字符流<br />
 * 
 * <h2>输出</h2><br />
 * 1. 单词序列token stream<br />
 * 
 * 
 * @author keepf
 *
 */
public class Lexer implements LexerServer {

	// 输入
	Preprocessor prep;
	// 输出

	// 分析器
	Classifier pdaClassifier;

	//
	protected boolean isSetup = false;
	protected boolean hasParse = false;
	protected List<Token> tokens = new ArrayList<>();
	protected int nowIndex = 0;

	public Lexer(BufferedReader bfreader, Keyword kw) {
		prep = new Preprocessor(bfreader);
		pdaClassifier = new Classifier(kw);
	}

	/**
	 * 设置文法，再检查通过时，将初始化自动机
	 * 
	 * @return 检查通过返回true，否则返回false
	 */
	public boolean setup() {
		String[] gs = CFGFormlize.loadGrammarsFile("Token_LL1_correct");
		CFGrammar constantG = new CFGrammar("token");
		gs = CFGFormlize.formalize(gs);
		constantG.add(gs);
		GrammarItemList_LL1 gl = new GrammarItemList_LL1(constantG.getItemList());

		// for (int i = 0; i < gs.length; i++) {
		// System.out.println(gs[i]);
		// }
		// System.out.println(gl.meetLL1());
		isSetup = gl.meetLL1();
		if (isSetup) {
			pdaClassifier.translateLL1(gl);
			pdaClassifier.restart();
		}
		return isSetup;
	}

	/**
	 * 使用自动机一次性解析出词牌
	 */
	public void parse() {
		hasParse = true;
		// TODO 此处需再添加一个文件末的null返回
		Symbol s = prep.getSymbol();
		// 算法fu
		boolean isContinueParse = false;
		String targetLexeme = "";
		while (s != null) {
			if (s.equals(new Symbol(":"))) {
				System.out.println();
				;
			}
			if (pdaClassifier.step(s)) {
				System.out.println("上面那个是：" + Classifier.getCategoryName(pdaClassifier.getTokenCategory(targetLexeme)));
				// TODO 解析出一个词牌
				int c = pdaClassifier.getTokenCategory(targetLexeme);
				Token e;
				switch (c) {
				case Classifier.TOKEN_PUNCTUATOR:
				case Classifier.TOKEN_KEYWORD:
					e = new Token(c, targetLexeme);
					tokens.add(e);
					break;
				case Classifier.TOKEN_CONSTANT:
					e = new Token_number(Integer.parseInt(targetLexeme));
					tokens.add(e);
					break;
				case Classifier.TOKEN_IDENT:
					e = new Token_ident(targetLexeme);
					tokens.add(e);
					break;
				default:
					break;
				}
				// if (c != Classifier.GRY) {
				// Token e = new Token(c, targetLexeme);
				// tokens.add(e);
				// }
				targetLexeme = "";
				pdaClassifier.restart();
				if (isContinueParse) {
					s = prep.getSymbol();
				}
				isContinueParse = true;
			} else {
				isContinueParse = false;
				targetLexeme += s.getName();
				s = prep.getSymbol();
			}
		}
	}

	public static void main(String[] args) {

		// String fileName = "PreprocessorTest";
		String fileName = "test1.pl0";
		Keyword kw = new Keyword();

		BufferedReader bfreader = null;
		try {
			bfreader = new BufferedReader(new FileReader(new File(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Lexer lexer = new Lexer(bfreader, kw);
		lexer.setup();
		lexer.parse();
		for (Token t : lexer.tokens) {
			System.out.println(t);
		}
		Token t;
		while ((t = lexer.getToken()) != null) {
			System.out.println(t);
		}
	}

	@Override
	public Token getPreToken() {
		return null;
	}

	@Override
	public Token getToken() {
		if (hasParse && nowIndex < tokens.size()) {
			Token t = tokens.get(nowIndex);
			nowIndex++;
			return t;
		}
		return null;
	}

	@Override
	public void restart() {
		nowIndex = 0;
	}
}
