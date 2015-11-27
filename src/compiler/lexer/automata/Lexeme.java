package compiler.lexer.automata;

/**
 * <h1>词素</h1>
 * 
 * @author keepf
 *
 */
public class Lexeme {

	public static final String Symbol_EscapeCharacter_NULL = "\\N"; // 表示空转义
	public static final String Symbol_EscapeCharacter_END = "\\E"; // 结束符号
	public static final String Symbol_EscapeCharacter_START = "\\S"; // 开始符号
	public static final String Symbol_EscapeCharacter_ESCAPE = "\\\\"; // 转义斜杠
	public static final String Symbol_ErrorCharacter_ESCAPE = "\\R"; // 错误字符

	char[] lex;

	public Lexeme(String l) throws LexemeConstructException {
		switch (l.length()) {
		case 1:
			lex = new char[1];
			lex[0] = l.charAt(0);
			break;
		case 2:
			lex = new char[2];
			lex[0] = l.charAt(0);
			lex[1] = l.charAt(1);
			break;
		default:
			throw new LexemeConstructException("Lexeme construct error with " + l + "!");
		}
	}
}
