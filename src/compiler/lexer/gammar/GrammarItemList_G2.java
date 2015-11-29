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
		return grammarItemList.toArray(new GrammarItem_G2[grammarItemList.size()]);
	}

	public Iterator<GrammarItem_G2> iterator() {
		return grammarItemList.iterator();
	}

	public String toString() {
		String res = "";
		res += "start with : " + startSymbol.getName() + "\n";
		for (GrammarItem_G2 g : grammarItemList) {
			res += g;
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
