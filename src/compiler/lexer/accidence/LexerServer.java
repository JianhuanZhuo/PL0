package compiler.lexer.accidence;

public interface LexerServer {

	//获得上一个 单词与其属性
	public Token getPreToken();
		//检查单词属性
	//获得下一个 单词与其属性
	public Token getToken();
	
	//获得当前 单词与其属性
	
	//获得当前行号
	//重置
	public void restart();
}
