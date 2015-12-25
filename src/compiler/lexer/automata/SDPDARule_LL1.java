package compiler.lexer.automata;

public class SDPDARule_LL1 extends SDPDARule {
	protected boolean isNullAccept = false;			//不接受
	public void setNullAccept(boolean isNullAccept) {
		this.isNullAccept = isNullAccept;
	}
	
	public void nullAccept() {
		isNullAccept = true;
	}
	
	public boolean isNullAccept() {
		return isNullAccept;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\tisNullAccept : "+isNullAccept;
	}
}
