package com.e23.compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Lexer {

	private BufferedReader sourceBuffer; // �����Դ�����Buffer
	private char currentChar = ' '; // ��ǰ��ȡ���ַ�

	private String currentLineString; // ��ǰ��ȡ��һ��Դ����
	private int currentLineStringLength = 0; // ��ǰ��ȡ��һ��Դ����ĵĳ���
	private int currentLineStringIndex = 0; // ��ȡ����ĳ�������ַ��±�

	private Symbol lastSymbol;
	private String lastName;
	static public int lineNum = 0;

	// �򵥵Ĳ���������ȡ��һ���ַ������ȷ�����͵��ַ�
	// Ҳ���� . , ; = + - ( ) * /
	private int[] simpleOperators;

	public String getLastName() {
		return lastName;
	}

	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            Դ������ļ�·��
	 */
	public Lexer(String filePath) {
		try {
			sourceBuffer = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("���� �ļ�������");
		}

		simpleOperators = new int[256]; 
		Arrays.fill(simpleOperators, Symbol.NUL); 
		simpleOperators['.'] = Symbol.PERIOD;
		simpleOperators[','] = Symbol.COMMA;
		simpleOperators[';'] = Symbol.SEMICOLON;
		simpleOperators['='] = Symbol.EQUAL;
		simpleOperators['+'] = Symbol.PLUS;
		simpleOperators['-'] = Symbol.MINUS;
		simpleOperators['('] = Symbol.LEFT_PARENTHESIS;
		simpleOperators[')'] = Symbol.RIGHT_PARENTHESIS;
		simpleOperators['*'] = Symbol.MULTIPLY;
		simpleOperators['/'] = Symbol.DIVIDE;
	}

	/**
	 * �������һ�ε�Symbol������
	 * 
	 * @return
	 */
	public Symbol lastSymbol() {
		return lastSymbol;
	}

	public int getLineNum() {
		return lineNum;
	}

	/**
	 * ������µ�symbol�����Ƿ������Ƿ�Ϊָ������
	 * 
	 * @param requestType
	 *            ָ������
	 * @return �Ƿ���true������Ϊfalse
	 */
	public boolean checkSymbolType(int requestType) {
		return (requestType == lastSymbol.getSymbolType());
	}

	/**
	 * �ʷ������л�ȡһ������
	 * 
	 * @return Symbol
	 */
	public Symbol getSymbol() {

		// ÿ�ζ���ǰ��ȡһ���ַ�

		// �����ո�
		while (isSpace()) {
			getChar();
		}

		if (isLetter()) {
			// �����ֻ��߱�ʶ��
			lastSymbol = getKeywordOrIdentifier();
			lastName = lastSymbol.getName();
		} else if (isDigit()) {
			// �޷�������
			lastSymbol = getNumber();
		} else {
			// �ֽ�� ���� �Ƿ��ַ�
			lastSymbol = getOperator();
		}

		return lastSymbol();
	}

	/**
	 * ��ȡ��һ���ַ���Ϊ�����ȡ����IO��ʵ��ÿ�ζ�ȡһ��
	 * 
	 */
	private void getChar() {
		if (currentLineStringIndex == currentLineStringLength) { // �����������ĩβ�����ٶ�ȡһ��
			try {
				String readString = "";

				// ��������
				while (readString.equals("")) {

					readString = sourceBuffer.readLine();

					if (readString == null) {
						// �ļ���ȫ����ȡ����Ҫ������ȡ������
						System.out.println("EOF, ERROR!");
						System.exit(0);
					}
					lineNum++;
					System.out.println(readString);
					// ȥ����ͷ��β�Ŀհ׷�
					readString = readString.trim();
				}

				currentLineString = readString;

				// ���ַ����е�[tab]�滻��[�ո�]
				currentLineString = currentLineString.replaceAll("\t", " ");

				// Ϊ��ֱֹ������[���з�]����������[�ո�]����[���з�]
				currentLineString += " ";

				currentLineStringLength = currentLineString.length();
				currentLineStringIndex = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		currentChar = currentLineString.charAt(currentLineStringIndex); // ��ȡ��һ���ַ�
		currentLineStringIndex++; // �±�����һ��λ��
	}

	/**
	 * �жϵ�ǰ��ȡ���ַ��Ƿ���[�ո�]
	 * 
	 * @return boolean
	 */
	private boolean isSpace() {
		return (currentChar == ' ');
	}

	/**
	 * �жϵ�ǰ��ȡ���ַ��Ƿ���[����]
	 * 
	 * @return boolean
	 */
	private boolean isDigit() {
		return currentChar >= '0' && currentChar <= '9';
	}

	/**
	 * �жϵ�ǰ��ȡ���ַ��Ƿ���[��ĸ]
	 * 
	 * @return boolean
	 */
	private boolean isLetter() {
		return (currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z');
	}

	/**
	 * ��ȡ���š�����ʶ�����߱�����
	 * 
	 * @return Symbol
	 */
	private Symbol getKeywordOrIdentifier() {
		Symbol symbol;
		String token = ""; // ʶ��ĵ���

		// ���ַ�ƴ�ӳ��ַ���
		while (isLetter() || isDigit()) {
			token += currentChar;
			getChar();
		}

		// ���ֲ��ұ�����
		int index = Arrays.binarySearch(Symbol.WORD, token);

		if (index < 0) { // δ�ڱ������������ҵ�token��˵��token�Ǳ�ʶ��
			symbol = new Symbol(Symbol.IDENTIFIER);
			symbol.setName(token);
		} else { // �ڱ������������ҵ�token��˵��token�Ǳ�����
			symbol = new Symbol(Symbol.WORD_SYMBOL[index]);
		}

		return symbol;
	}

	/**
	 * ��ȡ���š����޷�������
	 * 
	 * @return Symbol
	 */
	private Symbol getNumber() {

		Symbol symbol = new Symbol(Symbol.NUMBER);
		// TODO ʵ��
		int value = 0;
		do {
			value = value * 10 + (currentChar - '0'); // �����޷���������ֵ
			getChar();
		} while (isDigit());

		symbol.setNumber(value); // �����޷���������ֵ

		return symbol;
	}

	/**
	 * ��ȡ���š����ֽ���������
	 * 
	 * @return Symbol
	 */
	private Symbol getOperator() {
		Symbol symbol;

		switch (currentChar) {
			case ':':
				getChar();
				if (currentChar == '=') { // ��ֵ���� :=
					symbol = new Symbol(Symbol.BECOMES);
					getChar();
				} else {
					symbol = new Symbol(Symbol.NUL);
				}
				break;
			case '<':
				getChar();
				switch (currentChar) {
					case '>': // ������ <>
						symbol = new Symbol(Symbol.NOT_EQUAL);
						getChar();
						break;
					case '=': // С�ڵ��� <=
						symbol = new Symbol(Symbol.LESS_OR_EQUAL);
						getChar();
						break;
					default: // С��
						symbol = new Symbol(Symbol.LESS);
						break;
				}
				break;
			case '>':
				getChar();
				if (currentChar == '=') { // ���ڵ��� >=
					symbol = new Symbol(Symbol.GREATER_OR_EQUAL);
					getChar();
				} else { // ����
					symbol = new Symbol(Symbol.GREATER);
				}
				break;
			default:
				// ֱ������simpleOperators[]������伴��
				symbol = new Symbol(simpleOperators[currentChar]);

				// ��ǰ��ȡ��һ���ַ�
				getChar();
				break;
		}

		return symbol;
	}
}
