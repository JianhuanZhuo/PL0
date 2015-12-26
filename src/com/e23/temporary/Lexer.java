package com.e23.temporary;

import java.util.Map.Entry;

import compiler.lexer.automata.Symbol;
import compiler.lexer.gammar.CFGFormlize;
import compiler.lexer.gammar.CFGrammar;
import compiler.lexer.gammar.FirstSet;
import compiler.lexer.gammar.FollowSet;
import compiler.lexer.gammar.GrammarItemList_LL1;
import compiler.lexer.gammar.GrammarItem_G2;
import compiler.lexer.gammar.SelectSet;

public class Lexer {

	
	public void name() {

//		String[] gs = CFGFormlize.loadGrammarsFile("Token_LL1_correct");
//		CFGrammar constantG = new CFGrammar("token");

		String[] gs = CFGFormlize.loadGrammarsFile("PL0");
		CFGrammar constantG = new CFGrammar("program");
//		String[] gs = CFGFormlize.loadGrammarsFile("LL1_Test");
//		CFGrammar constantG = new CFGrammar("S");
		gs = CFGFormlize.formalize(gs);
		constantG.add(gs);
		
		GrammarItemList_LL1 gl = new GrammarItemList_LL1(constantG.getItemList());

		// /**
		// * 添加用于辅助的空格
		// */
		// Vector<Symbol> right = new Vector<>();
		// right.add(new Symbol(" "));
		// GrammarItem_G2 e = new GrammarItem_G2(new Symbol("token"), right);
		// gl.add(e);
		
		for (int i = 0; i < gs.length; i++) {
			System.out.println(gs[i]);
		}

		gl.calcEmptySet();
		System.out.println("Empty set:");
		for (Entry<Symbol, Boolean> e : gl.getEmptySet().entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}

		gl.calcFirstSet();
		System.out.println("\nFirst set:");
		for (Entry<Symbol, FirstSet> f : gl.getFirstS().entrySet()) {
			System.out.println(f.getValue());
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
//		System.out.println(gl.meetLL1());
	}
	
	public static void main(String[] args) {
		new Lexer().name();
	}
}
