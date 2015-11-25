package compiler.lexer.automata;

import java.util.Iterator;
import java.util.Vector;

public class RuleSet {

	Vector<Rule> rules = new Vector<>();

	public RuleSet() {
	}

	public void add(Rule r) {
		rules.add(r);
	}

	/**
	 * 检索规则集中的一个与参数规则相似的规则
	 * 
	 * @return 相似的规则
	 */
	public Rule indexRule(Rule similarRule) {

		Rule res = null;
		for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext();) {
			Rule rule = (Rule) iterator.next();
			if (rule.similar(similarRule)) {
				res = rule;
				break;
			}
		}
		return res;
	}
}
