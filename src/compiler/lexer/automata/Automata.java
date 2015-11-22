package compiler.lexer.automata;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动机
 * 
 * @author keepf
 *
 */
public class Automata {

	/**
	 * 节点映射，表示状态及其弧 <br />
	 * K：String 使用状态名作为检索键 <br />
	 * V:State 欲存储的状态
	 */
	Map<String, State> states = new HashMap<String, State>();

	/**
	 * 输入字符表
	 */
	Symbol symbol = new Symbol();
	
	/**
	 * 添加一个状态，不应该与现有的状态重名
	 * 
	 * @param stm
	 *            欲加入的状态
	 * @return 如果存在一个状态与新添加的状态的状态名相同，则取消此次调用，并返回当前状态引用，否则加入该元素并返回参数引用
	 */
	public State add(State stm) {
		if (states.containsKey(stm.name)) {
			return states.get(stm.name);
		}
		states.put(stm.name, stm);
		return stm;
	}

	
}
