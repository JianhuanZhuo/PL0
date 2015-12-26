package compiler.parser.syntaxTree;

public class SyntaxTree {

	// 当前结点
	private SyntaxNode startNode;

	public SyntaxTree(SyntaxNode startNode) {
		this.startNode = startNode;
	}

	public SyntaxNode getStartNode() {
		return startNode;
	}
}
