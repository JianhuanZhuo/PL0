package compiler.lexer.automata;

import java.util.HashSet;
import java.util.Set;

/**
 * Finite state Automaton, FA <br />
 * <b>有限状态自动机</b>
 * 
 * @author keepf
 *
 */
public class FA extends Automata {

	/**
	 * 结束状态集
	 */
	Set<State> endSet = new HashSet<State>();

	/**
	 * 默认结束状态
	 */
	private State defaultEndStm;

	/**
	 * 开始状态 <br />
	 * 在有限状态自动机中，开始状态只有一个
	 */
	State startStm;

	/**
	 * FA（有限状态自动机）构造函数，将初始化一个默认结束状态
	 */
	public FA() {
		defaultEndStm = new State();
		defaultEndStm.setEnd();
		add(defaultEndStm);
		endSet.add(defaultEndStm);
	}
}
