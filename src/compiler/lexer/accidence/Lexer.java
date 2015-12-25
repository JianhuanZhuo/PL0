package compiler.lexer.accidence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

	String fileName = "PreprocessorTest";

	// 输入
	Preprocessor prep;
	// 输出

	// 分析器
	SDPDA_LL1 sdpda_LL1 = new SDPDA_LL1();

	public Lexer() {
		BufferedReader bfreader = null;
		try {
			bfreader = new BufferedReader(new FileReader(new File(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		prep = new Preprocessor(bfreader);
	}

	public void name() {
		String[] gs = CFGFormlize.loadGrammarsFile("Token_LL1_correct");
		CFGrammar constantG = new CFGrammar("token");
		gs = CFGFormlize.formalize(gs);
		constantG.add(gs);
		GrammarItemList_LL1 gl = new GrammarItemList_LL1(constantG.getItemList());
		Vector<Symbol> right = new Vector<>();
		GrammarItem_G2 e = new GrammarItem_G2(new Symbol("token"), right);
		gl.add(e);
		for (int i = 0; i < gs.length; i++) {
			System.out.println(gs[i]);
		}
		System.out.println(gl.meetLL1());

		sdpda_LL1.translateLL1(gl);
		sdpda_LL1.restart();
		Symbol s = prep.getSymbol();
		boolean c = false;
		while (s != null) {
			if (sdpda_LL1.step(s)) {
				System.out.println();
				sdpda_LL1.restart();
				if (c) {
					s = prep.getSymbol();
				}
				c = true;
			} else {
				c = false;
				System.out.print(s.getName());
				s = prep.getSymbol();
			}
		}
	}

	public static void main(String[] args) {
		new Lexer().name();
	}

	@Override
	public Token getPreToken() {

		return null;
	}

	@Override
	public Token getToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
	}
}
