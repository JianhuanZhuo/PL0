package compiler.lexer.automata;

import compiler.paser.syntaxTree.SyntaxNode;

public class Symbol implements Cloneable, Comparable<Symbol> {

	public static final String Symbol_EscapeCharacter_NULL = "\\N"; // 表示空转义
	public static final String Symbol_EscapeCharacter_END = "\\E"; // 结束符号
	public static final String Symbol_EscapeCharacter_START = "\\S"; // 开始符号
	public static final String Symbol_EscapeCharacter_ESCAPE = "\\\\"; // 转义斜杠

	static Symbol Symbol_NULL = new Symbol(Symbol_EscapeCharacter_NULL);
	static Symbol Symbol_END = new Symbol(Symbol_EscapeCharacter_END);
	static Symbol Symbol_START = new Symbol(Symbol_EscapeCharacter_START);
	static Symbol Symbol_ESCAPE = new Symbol(Symbol_EscapeCharacter_ESCAPE);

	/**
	 * 是否为非终结符号
	 */
	private boolean isVN;

	/**
	 * 符号名
	 */
	private String name;

	/**
	 * 相关语法树的节点<br>
	 * 该节点记录该符号所归属的语法树节点
	 */
	private SyntaxNode relateNode;

	/**
	 * 符号类对象的构造函数<br />
	 * 将默认构造终结符号
	 * 
	 * @param name
	 *            符号名
	 */
	public Symbol(String name) {
		this(name, false);
	}

	/**
	 * 符号类对象的构造函数
	 * 
	 * @param name
	 *            符号名
	 * @param isVN
	 *            是否为非终结符号
	 */
	public Symbol(String name, boolean isVN) {
		this.isVN = isVN;
		this.name = name;
	}

	public boolean getIsVN() {
		return isVN;
	}

	/**
	 * 设置语法树相关的语法节点对象<br>
	 * 用于下推自动机生成语法树时进行语法节点连接
	 * 
	 * @param node
	 *            欲设置的语法节点
	 */
	public void setRelateNode(SyntaxNode node) {
		if (null != relateNode) {
			throw new RuntimeException("相关节点已使用，可能存在BUG！");
		} else {
			relateNode = node;
		}
	}

	/**
	 * 获得符号名
	 * 
	 * @return 符号名
	 */
	public String getName() {
		return name;
	}

	@Override
	protected Object clone() {
		Symbol c = new Symbol(name);
		c.isVN = isVN;
		return c;
	}

	/**
	 * 获得语法相关节点
	 * 
	 * @return 语法相关节点
	 */
	public SyntaxNode getRelateNode() {
		return relateNode;
	}

	@Override
	public int compareTo(Symbol o) {
		if (isVN && !o.isVN) {
			return 1;
		} else if (!isVN && o.isVN) {
			return -1;
		}
		return name.compareTo(o.name);
	}
}
