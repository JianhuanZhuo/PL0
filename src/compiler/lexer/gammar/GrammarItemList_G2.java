package compiler.lexer.gammar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import compiler.lexer.automata.Symbol;

public class GrammarItemList_G2 {

	private List<GrammarItem_G2> grammarItemList;
	private Symbol startSymbol;

	public GrammarItemList_G2() {
		grammarItemList = new ArrayList<>();
	}

	public void add(GrammarItem_G2 e) {
		grammarItemList.add(e);
	}

	public GrammarItem_G2[] toArray() {
		return (GrammarItem_G2[]) grammarItemList.toArray();
	}

	public Iterator<GrammarItem_G2> iterator() {
		return grammarItemList.iterator();
	}

	public String toString() {
		String res = "";
		for (Iterator<GrammarItem_G2> iterator = grammarItemList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 grammarItem_G2 = (GrammarItem_G2) iterator.next();
			res += grammarItem_G2;
		}
		return res;
	}

	public Symbol getStartSymbol() {
		return startSymbol;
	}

	public void setStartSymbol(Symbol s) {
		startSymbol = s;
	}
}
