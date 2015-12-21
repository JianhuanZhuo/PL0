package compiler.lexer.accidence;

import compiler.lexer.automata.SDPDA_LL1;

/**
 * <h1>词法分析器</h1><br />
 * 过滤空白，换行，记录行数（错误时，赋予行号）<br />
 * 
 * 建立一个输入缓冲，（可能为一个双缓冲机制）<br />
 * 
 * <hr />
 * 
 * <h2>输入</h2><br />
 * 
 * 1. 词法<br />
 * 2. 字符流<br />
 * 
 * <h2>输出</h2><br />
 * 1. 单词序列token stream<br />
 * 
 * 
 * @author keepf
 *
 */
public class Lexer implements LexerServer {

	//输入
	
	
	// 输出
	
	//分析器
	SDPDA_LL1 sdpda_LL1 = new SDPDA_LL1();
	
	
	public void name() {
		sdpda_LL1.translateGNF(grammars);
		sdpda_LL1.restart();
		sdpda_LL1.step(input);
	}
}
