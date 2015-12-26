package compiler.lexer.accidence;

public class Token_ident extends Token {

	String ident;
	
	public Token_ident(String ident) {
		super(Classifier.TOKEN_IDENT, Classifier.getCategoryName(Classifier.TOKEN_IDENT));
		this.ident = ident;
	}

	public String getIdent() {
		return ident;
	}
	
	@Override
	public String toString() {
		return Classifier.getCategoryName(category)+" "+ident;
	}
}
