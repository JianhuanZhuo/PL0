package compiler.lexer.accidence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import compiler.lexer.automata.Symbol;

/**
 * <h1>预编译器</h1><br>
 * 预编译阶段将需要处理，多文件合并（使用#include载入的），注释处理，预编译处理（宏扩展）
 * 
 * @author keepf
 *
 */
public class Preprocessor {

	// 文件名
	String fileName;
	// 过滤注释所用的正则表达式
	String commentExpression;

	// 过滤注释

	String source;

	private BufferedReader sourceBuffer; // 读入的源代码的Buffer
	private char currentChar = ' '; // 当前读取的字符
	private char preChar = ' ';// 之前读到的字符的副本

	private String currentLineString; // 当前读取的一行源代码
	private int currentLineStringLength = 0; // 当前读取的一行源代码的的长度
	private int currentLineStringIndex = 0; // 读取到的某个具体字符下标

	static public int lineNum = 0;

	private char cacheChar;
	private boolean hasCache = false;

	public Preprocessor(BufferedReader bfreader) {
		sourceBuffer = bfreader;
	}
	
	/**
	 * 从缓冲中读取一个字符
	 * 
	 * @return 读取到的字符
	 */
	private char readChar() {
		// 清除缓存区字符
		if (hasCache) {
			hasCache = false;
			return cacheChar;
		}
		if (currentLineStringIndex == currentLineStringLength) { // 如果读到了行末尾，则再读取一行
			try {
				String readString = "";
				// 跳过空行
				while (readString.equals("")) {
					readString = sourceBuffer.readLine();
					if (readString == null) {
						// 文件已全部读取，还要继续读取，出错
						System.err.println("EOF, ERROR!");
						System.exit(0);
					}
					lineNum++;
					System.out.println(readString);
					// 去除开头结尾的空白符
					readString = readString.trim();
				}
				currentLineString = readString;
				// 将字符串中的[tab]替换成[空格]
				currentLineString = currentLineString.replaceAll("\t", " ");
				// 为防止直接跳过[换行符]，在最后加上[空格]代替[换行符]
				currentLineString += " ";
				currentLineStringLength = currentLineString.length();
				currentLineStringIndex = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		preChar = currentChar;
		currentChar = currentLineString.charAt(currentLineStringIndex); // 获取下一个字符
		currentLineStringIndex++; // 下标下移一个位置
		return currentChar;
	}

	/**
	 * 检查下一个字符
	 * 
	 * @return 下一个字符
	 */
	private char seeNext() {
		if (hasCache) {
			return cacheChar;
		} else {
			cacheChar = readChar();
			hasCache = true;
		}
		return cacheChar;
	}

	/**
	 * 获得一个字符[符号]
	 * 
	 * @return 返回当前的字符符号
	 */
	public Symbol getSymbol() {
		String symbolName = "";
		//TODO 除去空格
		if (' ' == readChar()) {
			while (' ' == seeNext())
				;
			return new Symbol(" ");
		}
		symbolName += currentChar;
		return new Symbol(symbolName);
	}

	public Symbol preSymbol() {
		return null;
	}

	public Symbol nextSymbol() {
		return null;
	}

	public int getCurrentLine() {
		return lineNum;
	}

	public static void main(String[] args) throws FileNotFoundException {
		String fileName = "PreprocessorTest";
		BufferedReader bfreader = new BufferedReader(new FileReader(new File(fileName)));
		Preprocessor p = new Preprocessor(bfreader);
		while(true){
			System.out.println(p.getSymbol());
		}
	}
}
