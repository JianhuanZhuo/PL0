package com.e23.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MainTextPane extends JTextPane {

	/**
	 * 串行版本ID
	 */
	private static final long serialVersionUID = 6570307684186770261L;

	protected StyledDocument doc;
	protected SyntaxFormatter formatter = new SyntaxFormatter("PL0style.stx");
	// 定义该文档的普通文本的外观属性
	private SimpleAttributeSet normalAttr = formatter.getNormalAttributeSet();
	private SimpleAttributeSet quotAttr = new SimpleAttributeSet();
	// 保存文档改变的开始位置
	private int docChangeStart = 0;
	// 保存文档改变的长度
	private int docChangeLength = 0;

	public MainTextPane() {
		StyleConstants.setForeground(quotAttr, new Color(255, 0, 255));
		StyleConstants.setFontSize(quotAttr, 16);
		this.doc = super.getStyledDocument();
		// 设置该文档的页边距
		this.setMargin(new Insets(3, 40, 0, 0));
		// 添加按键监听器，当按键松开时进行语法分析
		this.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				syntaxParse();
			}
		});
		// 添加文档监听器
		doc.addDocumentListener(new DocumentListener() {
			// 当Document的属性或属性集发生了改变时触发该方法
			public void changedUpdate(DocumentEvent e) {
			}

			// 当向Document中插入文本时触发该方法
			public void insertUpdate(DocumentEvent e) {
				docChangeStart = e.getOffset();
				docChangeLength = e.getLength();
			}

			// 当从Document中删除文本时触发该方法
			public void removeUpdate(DocumentEvent e) {
			}
		});
	}

	/**
	 * 检查文档全部内容的着色
	 */
	public void syntaxParseAll() {

		// 获取文档的根元素，即文档内的全部内容
		Element root = doc.getDefaultRootElement();
		for (int i = 0; i < root.getElementCount(); i++) {
			syntaxLine(i);
		}
	}


	/**
	 * 检查文档指定行的着色
	 */
	public void syntaxLine(int line) {

		try {
			// 获取文档的根元素，即文档内的全部内容
			Element root = doc.getDefaultRootElement();
			// 获取光标所在位置的行
			Element para = root.getElement(line);
			// 定义光标所在行的行头在文档中位置
			int start = para.getStartOffset();
			// 让start等于start与docChangeStart中较小值。
			start = start > docChangeStart ? docChangeStart : start;
			// 定义被修改部分的长度
			int length = para.getEndOffset() - start;
			length = length < docChangeLength ? docChangeLength + 1 : length;
			// 取出所有可能被修改的字符串
			String s = doc.getText(start, length);
			// 以空格、点号等作为分隔符
			String[] tokens = s.split("\\s+|\\.|\\(|\\)|\\{|\\}|\\[|\\]");
			// 定义当前分析单词的在s字符串中的开始位置
			int curStart = 0;
			// 定义单词是否处于引号以内
			boolean isQuot = false;
			for (String token : tokens) {
				// 找出当前分析单词在s字符串中的位置
				int tokenPos = s.indexOf(token, curStart);
				if (isQuot && (token.endsWith("\"") || token.endsWith("\'"))) {
					doc.setCharacterAttributes(start + tokenPos, token.length(), quotAttr, false);
					isQuot = false;
				} else if (isQuot && !(token.endsWith("\"") || token.endsWith("\'"))) {
					doc.setCharacterAttributes(start + tokenPos, token.length(), quotAttr, false);
				} else if ((token.startsWith("\"") || token.startsWith("\'"))
						&& (token.endsWith("\"") || token.endsWith("\'"))) {
					doc.setCharacterAttributes(start + tokenPos, token.length(), quotAttr, false);
				} else if ((token.startsWith("\"") || token.startsWith("\'"))
						&& !(token.endsWith("\"") || token.endsWith("\'"))) {
					doc.setCharacterAttributes(start + tokenPos, token.length(), quotAttr, false);
					isQuot = true;
				} else {
					// 使用格式器对当前单词设置颜色
					formatter.setHighLight(doc, token, start + tokenPos, token.length());
				}
				// 开始分析下一个单词
				curStart = tokenPos + token.length();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 检查光标所在行的作色
	 */
	public void syntaxParse() {

		// 获取文档的根元素，即文档内的全部内容
		Element root = doc.getDefaultRootElement();
		// 获取文档中光标插入符的位置
		int cursorPos = this.getCaretPosition();
		int line = root.getElementIndex(cursorPos);
		syntaxLine(line);
	}

	// 重画该组件，设置行号
	public void paint(Graphics g) {
		super.paint(g);
		Element root = doc.getDefaultRootElement();
		// 获得行号
		int line = root.getElementIndex(doc.getLength());
		// 设置颜色
		g.setColor(new Color(230, 230, 230));
		// 绘制显示行数的矩形框
		g.fillRect(0, 0, this.getMargin().left - 10, getSize().height);
		// 设置行号的颜色
		g.setColor(new Color(40, 40, 40));
		// 每行绘制一个行号
		for (int count = 0, j = 1; count <= line; count++, j++) {
			g.drawString(String.valueOf(j), 3, (int) ((count + 1) * 1.375 * StyleConstants.getFontSize(normalAttr)));
		}
	}
}
