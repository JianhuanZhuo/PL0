package compiler.lexer.automata;

public class Arc {

	private String jumpKey;
	private State target;

	public String getJumpKey() {
		return jumpKey;
	}

	public State getTarget() {
		return target;
	}

	/**
	 * 构造一个转变弧，其转变条件可为空，即null
	 * 
	 * @param target
	 *            目标状态
	 * @param jumpKey
	 *            转变条件
	 */
	public Arc(State target, String jumpKey) {
		if (null == target) {
			throw new RuntimeException("弧构造错误，目标状态不可为空");
		}
		this.target = target;
		this.jumpKey = jumpKey;
	}

}