package compiler.lexer.automata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tomKit.printOut.PO;

import compiler.lexer.gammar.GrammarItemList_LL1;
import compiler.lexer.gammar.GrammarItem_G2;
import compiler.lexer.gammar.SelectSet;

/**
 * <h1>使用于LL(1)文法的单态确定型下推自动机子类</h1><br>
 * 该类实现了语法树生产业务、LL1文法转换
 * 
 * @author keepf
 *
 */
public class SDPDA_LL1 extends SDPDA {

	/**
	 * <h2>使用LL1文法的select集初始化它的转换规则</h2><br />
	 * 
	 * @return 
	 */
	public boolean translateLL1(GrammarItemList_LL1 gLL1) {

		boolean res = true;
		// TODO 建立临时规则表
		RuleSet rules = new RuleSet();
		Map<GrammarItem_G2, SelectSet> selectS = gLL1.getSelectS();

		for (Iterator<GrammarItem_G2> iterator = gLL1.iterator(); iterator.hasNext();) {
			GrammarItem_G2 item = (GrammarItem_G2) iterator.next();
			item = item.copy();
			Set<Symbol> selectSym = selectS.get(item).getSelectSet();
			List<Symbol> ls = new ArrayList<>(selectSym);
			for (int i = 0; i < ls.size(); i++) {
				SDPDARule_LL1 r = new SDPDARule_LL1();
				r.nowStackTop = item.getLeft();
				r.matchSymbol = ls.get(i);
				r.setNextStackTop(new ArrayList<>());
				if (!item.meetGNF()) {
					r.nullAccept();
				}else {
					//终结符开头，则需消耗一个
					item.takeRightFirstSymbol();
				}
				r.setNextStackTop(item.getRightList_Forss());
				rules.add(r);
			}
			
			//TODO 添加非终结符的匹配
			for (Symbol vt : gLL1.getVTSet()) {
				SDPDARule_LL1 r = new SDPDARule_LL1();
				r.nowStackTop = vt;
				r.matchSymbol = vt;
				r.setNextStackTop(new ArrayList<>());
				rules.add(r);
			}
		}

		// TODO GNF文法转PDA无异常，则将临时PDA自动机更新入this对象的属性中
		if (res) {
			ruleSet = rules;
			pdStack = new PushDownStack(gLL1.getStartSymbol());
		}

		return res;
	}

	/**
	 * 替换下推栈状态
	 */
	@Override
	public void replaceStack(SDPDARule r, Symbol input) {
		// TODO 下推栈状态替换
		pdStack.pop();
		int t = r.getNextStackTop().size();
		for (int i = 1; i <= t; i++) {
			Symbol e = r.getNextStackTop().get(t-i);
			pdStack.push(e);
		}
		if (((SDPDARule_LL1)r).isNullAccept) {
			step(input);
		}
	}
}
