package compiler.lexer.automata;

public class DD extends AA{

	public DD() {
		System.out.println("D");
	}

	public static void main(String[] args) {
		DD a = new DD();
		DD b = new DD();
		if (a.hashCode() == b.hashCode()) {
			System.out.println("s");
		}else {
			System.out.println("x");
		}
	}
	
}

class AA{
	public AA() {
		System.out.println("AA");
	}
}

