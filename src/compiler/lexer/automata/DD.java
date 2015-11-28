package compiler.lexer.automata;

public class DD extends AA {

	static String k="xxxx";
	public void s() {

		System.out.println(k);
	}

	public DD() {
		System.out.println("D");
	}

	public static void main(String[] args) {
		DD a = new DD();
		DD b = new DD();
		if (a.hashCode() == b.hashCode()) {
			System.out.println("s");
		} else {
			System.out.println("x");
		}

		AA.name();
	}

}

class AA {
	static String k="xx";
	public void s() {
		System.out.println(k);
	}

	public AA() {
		System.out.println("AA");
	}

	public static void name() {
		System.out.println("xxxsss");
	}
}
