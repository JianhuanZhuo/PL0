package com.e23.lexer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 自动机类
 * 
 * @author Administrator
 *
 */
public class AutoMachine {

	/**
	 * 结束状态集
	 */
	Set<State> endSet = new HashSet<State>();

	/**
	 * 默认结束状态
	 */
	private State defaultEndStm;

	/**
	 * 开始状态
	 */
	State startStm;

	/**
	 * 节点映射，表示状态及其弧 <br />
	 * K：String 使用状态名作为检索键 <br />
	 * V:Statement 欲存储的状态
	 */
	Map<String, State> stms = new HashMap<String, State>();

	public AutoMachine() {
		defaultEndStm = new State();
		defaultEndStm.setEnd();
		add(defaultEndStm);
		endSet.add(defaultEndStm);
	}

	/**
	 * 将指定的状态加入结束集中
	 * 
	 * @param stmName
	 *            欲结束集(可满足集)的状态名
	 * @return 若自动机中存在该状态则将状态加入结束集并返回true，否则返回false
	 */
	public boolean addEndSet(String stmName) {

		if (stms.containsKey(stmName)) {
			State stm = stms.get(stmName);
			endSet.add(stm);
			return true;
		}
		return false;
	}

	/**
	 * 获得默认的结束状态
	 * 
	 * @return 默认的结束状态
	 */
	public State getDefaultEndStm() {
		return defaultEndStm;
	}

	/**
	 * 添加一个状态，不应该与现有的状态重名
	 * 
	 * @param stm
	 *            欲加入的状态
	 * @return 如果存在一个状态与新添加的状态的状态名相同，则取消此次调用，并返回当前状态引用，否则加入该元素并返回参数引用
	 */
	public State add(State stm) {
		if (stms.containsKey(stm.name)) {
			return stms.get(stm.name);
		}
		stms.put(stm.name, stm);
		return stm;
	}

	/**
	 * 由当前状态机生成一个正规文法表达式
	 * 
	 * @return
	 */
	public Grammar generateGammar() {
		Grammar res = new Grammar();
		Set<String> keyset = stms.keySet();
		for (Iterator<String> iterator = keyset.iterator(); iterator.hasNext();) {
			State stm = stms.get((String) iterator.next());
			Set<Entry<String, Arc>> entrySet = stm.getArcs();
			for (Iterator<Entry<String, Arc>> iterator2 = entrySet.iterator(); iterator2.hasNext();) {
				Entry<String, Arc> entry = (Entry<String, Arc>) iterator2.next();
				res.add("<"+stm.name+">->"+entry.getKey()+"<"+entry.getValue().getTarget().name+">");
				if (entry.getValue().getTarget().isEnd()) {
					res.add("<"+entry.getKey()+">->"+entry.getValue().getTarget().name);
				}
			}
		}
		return res;
	}

	public static void main(String[] args) {
		AutoMachine s = new AutoMachine();

		Map<String, String> stms = new HashMap<String, String>();
		String S1 = new String("S1");
		String S2 = new String("S1");
		if (S1 == S2) {
			System.out.println("eq");
		}
		System.out.println(stms.put(S1, "s"));
		System.out.println(stms.put(S2, "k"));
	}
}
