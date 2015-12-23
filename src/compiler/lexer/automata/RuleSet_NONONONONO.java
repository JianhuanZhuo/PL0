package compiler.lexer.automata;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.control.Tab;

/**
 * <h1>规则集类</h1>
 * 
 * @author keepf
 *
 * @param <Rule>
 *            继承有自然比较接口的规则接口，也就是说该规则集里的规则都是可比较的
 */
public class RuleSet_NONONONONO<T extends Comparable<T>> {

	/**
	 * 规则表
	 */
	Set<T> rules = new HashSet<T>();

	/**
	 * 排序好的规则缓存
	 */
	T[] arrayOfRules;
	
	Class<T> t;

	public RuleSet_NONONONONO(Class<T> t) {
		this.t = t;
	}

	/**
	 * 添加一个规则
	 * 
	 * @param r
	 *            欲添加的规则
	 * @return 存在等价规则返回false，否则返回true
	 */
	public boolean add(T r) {
		return rules.add(r);
	}

	/**
	 * 检索规则集中的一个与参照规则等价的规则
	 * 
	 * @return 查找到对应的参照规则，返回该规则；否则返回null
	 */
	public T indexRule(T r) {
		T res = null;
		// TODO 检查是否存在规则缓存，无则生成它
		if (null == arrayOfRules) {
			
			arrayOfRules = rules.toArray((T[])Array.newInstance(t,rules.size()));
			
			
			Arrays.sort(arrayOfRules);
		}

		// TODO 使用二分查找法查找合适的规则
		int i = Arrays.binarySearch(arrayOfRules, r);
		if (i > 0) {
			res = arrayOfRules[i];
		}
		return res;
	}
}
