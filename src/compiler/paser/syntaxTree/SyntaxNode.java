package compiler.paser.syntaxTree;

import java.util.Vector;

import compiler.lexer.automata.Symbol;

/**
 * 语法节点类
 * 
 * @author keepf
 *
 */
public class SyntaxNode {

	// 节点名
	String nodeName;

	/**
	 * 孩子节点集合
	 */
	Vector<SyntaxNode> childs = new Vector<>();

	private SyntaxNode fatherNode;

	public void setFatherNode(SyntaxNode fatherNode) {
		this.fatherNode = fatherNode;
	}

	/**
	 * 对应的符号
	 */
	Symbol symbol;

	/**
	 * 使用一个符号初始化该节点
	 * 
	 * @param symbol
	 */
	public SyntaxNode(Symbol symbol) {
		this.symbol = symbol;
		symbol.setRelateNode(this);
	}

	/**
	 * 添加一个孩子节点<br>
	 * 该函数会为孩子节点设置父亲节点
	 * 
	 * @param child
	 *            欲添加的孩子节点
	 */
	public void addChild(SyntaxNode child) {
		childs.add(child);
		child.setFatherNode(this);
	}

	/**
	 * 获得节点的父亲节点
	 * 
	 * @return 父亲节点，可能该节点未设置父亲节点则返回null
	 */
	public SyntaxNode getFatherNode() {
		return fatherNode;
	}

}
