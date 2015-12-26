package compiler.lexer.accidence;

/**
 * <h1>词牌</h1><br />
 * 数值常量的词牌
 * 
 * @author keepf
 *
 */
public class Token_number extends Token {

	int value;

	public Token_number(int value) {
		super(Classifier.TOKEN_CONSTANT, Classifier.getCategoryName(Classifier.TOKEN_CONSTANT));
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return Classifier.getCategoryName(category)+" "+value;
	}
}
