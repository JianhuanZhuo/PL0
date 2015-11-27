package compiler.lexer.gammar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GrammarItemList_G2 {

	private List<GrammarItem_G2> grammarItemList;

	public GrammarItemList_G2() {
		grammarItemList = new ArrayList<>();
	}

	public void add(GrammarItem_G2 e) {
		grammarItemList.add(e);
	}

	public String toString() {
		String res = "";
		for (Iterator<GrammarItem_G2> iterator = grammarItemList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 grammarItem_G2 = (GrammarItem_G2) iterator.next();
			res += grammarItem_G2;
		}
		return res;
	}
}
