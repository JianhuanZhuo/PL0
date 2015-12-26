package compiler.lexer.automata;

import compiler.paser.syntaxTree.SyntaxNode;

public class Symbol implements Cloneable, Comparable<Symbol> {

	/**
	 * 是否为非终结符号 nonterminal
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
		if (!isVN) {
			formlize();
		}
	}

	public boolean getIsVN() {
		return isVN;
	}

	/**
	 * 检查该符号是否为空符号
	 * 
	 * @return 是返回true，否则返回false
	 */
	public boolean empty() {
		if (!isVN && (name.charAt(0) == 0x18)) {
			return true;
		} else {
			return false;
		}
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

	public void formlize() {
		if (!isVN) {
			try {
				if ('\\' == name.charAt(0)) {
					if (name.length() > 1) {
						switch (name.charAt(1)) {
						case 'N':
							name = new String("" + new Character((char) 0x18));
							break;
						case '#':
							name = new String("" + new Character((char) 0x19));
							break;
						default:
							name = name.substring(1);
							break;
						}
					}
				}
			} catch (StringIndexOutOfBoundsException e) {
				// TODO: handle exception
				System.err.println("String index out of range: "+ name);
				System.err.println("legth : "+ name.length());
				System.err.println("exit!!!");
				System.exit(0);
			}
		}
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

	@Override
	public boolean equals(Object obj) {
		if (null != obj) {
			Symbol target = (Symbol) obj;
			if (isVN == target.isVN && name.equals(target.name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return (isVN ? "_VN_" : "_VT_") + name;
	}

	@Override
	public int hashCode() {
		return (isVN ? -1 : 1) * name.hashCode();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 0xff; i++) {
			System.out.println(i + " : " + new Character((char) i));
		}
	}
}
