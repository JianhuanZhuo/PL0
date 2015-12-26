package compiler.parser;

import java.util.Map.Entry;

import compiler.lexer.automata.Symbol;
import compiler.lexer.gammar.FirstSet;
import compiler.lexer.gammar.FollowSet;
import compiler.lexer.gammar.GrammarItemList_LL1;
import compiler.lexer.gammar.GrammarItem_G2;
import compiler.lexer.gammar.SelectSet;

public class Parser {

	GrammarTransform gtf = new GrammarTransform();

	public void name() {

		GrammarItemList_LL1 gl = gtf.transform();
		System.out.println(gl);
		
		gl.calcEmptySet();
		System.out.println("Empty set:");
		for (Entry<Symbol, Boolean> e : gl.getEmptySet().entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}

		gl.calcFirstSet();
		System.out.println("\nFirst set:");
		for (Entry<Symbol, FirstSet> f : gl.getFirstS().entrySet()) {
			try {
				System.out.println(f.getValue());
			} catch (Exception e) {
				System.out.println(f.getKey());
				e.printStackTrace();
			}
		}

		System.out.println("\nFirst set:");
		for (Entry<Symbol, FirstSet> f : gl.getFirstS().entrySet()) {
			System.out.println(f.getKey());
			FirstSet fs = f.getValue();
			for (Symbol s : fs.getFirstSet()) {
				System.out.println("\t" + s);
			}
		}

		System.out.println("\nFollow set:");
		gl.calcFollowSet();
		for (Entry<Symbol, FollowSet> e : gl.getFollowS().entrySet()) {
			System.out.println(e.getValue());
		}

		System.out.println("\nSelect set:");
		gl.calcSelect();
		for (Entry<GrammarItem_G2, SelectSet> e : gl.getSelectS().entrySet()) {
			System.out.println(e.getValue());
		}
		System.out.println("check intersertion ：" + gl.calcIntersertion());

		// TODO 生成语法分析树
	}

	public static void main(String[] args) {
		new Parser().name();
	}

}
