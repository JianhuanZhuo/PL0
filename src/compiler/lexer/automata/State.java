package compiler.lexer.automata;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class State implements Comparable<State> {

	private static int count;

	/**
	 * 开始状态标志
	 */
	private boolean start = false;

	/**
	 * 可满足状态标志
	 */
	private boolean end = false;

	/**
	 * 状态名
	 */
	String name;

	/**
	 * 节点向量，表示状态及其弧
	 */
	private Map<String, Arc> arcs = new HashMap<String, Arc>();

	/**
	 * 默认构造函数，使用默认分配的状态名构造一个非起始状态
	 */
	public State() {
		this(false);
	}

	public State(boolean start) {
		this(start, "STM" + (++count));
	}

	public State(boolean start, String name) {
		this.start = start;
		if (null != name) {
			this.name = name;
		}
	}

	public Set<Entry<String, Arc>> getArcs() {
		return arcs.entrySet();
	}

	/**
	 * 设置该状态为开始状态
	 */
	public void setStart() {
		start = true;
	}

	/**
	 * 设置该状态是否为开始状态
	 */
	public void setStart(boolean s) {
		start = s;
	}

	/**
	 * 设置该状态为结束状态（可满足状态）
	 */
	public void setEnd() {
		end = true;
	}

	/**
	 * 设置该状态是否为结束状态（可满足状态）
	 * 
	 * @param e
	 *            欲设置的值
	 */
	public void setEnd(boolean e) {
		end = e;
	}

	/**
	 * 添加一个弧
	 * 
	 * @return 添加成功返回true，添加失败返回false
	 */
	public boolean addArc(Arc arc) {
		if (arcs.containsKey(arc.getJumpKey())) {
			return false;
		}
		arcs.put(arc.getJumpKey(), arc);
		return true;
	}

	public boolean isStart() {
		return start;
	}

	public boolean isEnd() {
		return end;
	}

	/**
	 * 按状态标志符、结束标识符、状态名Unicode值进行排序
	 */
	@Override
	public int compareTo(State o) {
		if (start && !o.start) {
			return 1;
		} else if (!start && o.start) {
			return -1;
		}

		if (end && !o.end) {
			return 1;
		} else if (!end && o.end) {
			return -1;
		}
		return name.compareTo(o.name);
	}

}
