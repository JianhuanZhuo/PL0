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
	 * ���а汾ID
	 */
	private static final long serialVersionUID = 6570307684186770261L;

	protected StyledDocument doc;
	protected SyntaxFormatter formatter = new SyntaxFormatter("PL0style.stx");
	// ������ĵ�����ͨ�ı����������
	private SimpleAttributeSet normalAttr = formatter.getNormalAttributeSet();
	private SimpleAttributeSet quotAttr = new SimpleAttributeSet();
	// �����ĵ��ı�Ŀ�ʼλ��
	private int docChangeStart = 0;
	// �����ĵ��ı�ĳ���
	private int docChangeLength = 0;

	public MainTextPane() {
		StyleConstants.setForeground(quotAttr, new Color(255, 0, 255));
		StyleConstants.setFontSize(quotAttr, 16);
		this.doc = super.getStyledDocument();
		// ���ø��ĵ���ҳ�߾�
		this.setMargin(new Insets(3, 40, 0, 0));
		// ��Ӱ������������������ɿ�ʱ�����﷨����
		this.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				syntaxParse();
			}
		});
		// ����ĵ�������
		doc.addDocumentListener(new DocumentListener() {
			// ��Document�����Ի����Լ������˸ı�ʱ�����÷���
			public void changedUpdate(DocumentEvent e) {
			}

			// ����Document�в����ı�ʱ�����÷���
			public void insertUpdate(DocumentEvent e) {
				docChangeStart = e.getOffset();
				docChangeLength = e.getLength();
			}

			// ����Document��ɾ���ı�ʱ�����÷���
			public void removeUpdate(DocumentEvent e) {
			}
		});
	}

	/**
	 * ����ĵ�ȫ�����ݵ���ɫ
	 */
	public void syntaxParseAll() {

		// ��ȡ�ĵ��ĸ�Ԫ�أ����ĵ��ڵ�ȫ������
		Element root = doc.getDefaultRootElement();
		for (int i = 0; i < root.getElementCount(); i++) {
			syntaxLine(i);
		}
	}


	/**
	 * ����ĵ�ָ���е���ɫ
	 */
	public void syntaxLine(int line) {

		try {
			// ��ȡ�ĵ��ĸ�Ԫ�أ����ĵ��ڵ�ȫ������
			Element root = doc.getDefaultRootElement();
			// ��ȡ�������λ�õ���
			Element para = root.getElement(line);
			// �����������е���ͷ���ĵ���λ��
			int start = para.getStartOffset();
			// ��start����start��docChangeStart�н�Сֵ��
			start = start > docChangeStart ? docChangeStart : start;
			// ���屻�޸Ĳ��ֵĳ���
			int length = para.getEndOffset() - start;
			length = length < docChangeLength ? docChangeLength + 1 : length;
			// ȡ�����п��ܱ��޸ĵ��ַ���
			String s = doc.getText(start, length);
			// �Կո񡢵�ŵ���Ϊ�ָ���
			String[] tokens = s.split("\\s+|\\.|\\(|\\)|\\{|\\}|\\[|\\]");
			// ���嵱ǰ�������ʵ���s�ַ����еĿ�ʼλ��
			int curStart = 0;
			// ���嵥���Ƿ�����������
			boolean isQuot = false;
			for (String token : tokens) {
				// �ҳ���ǰ����������s�ַ����е�λ��
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
					// ʹ�ø�ʽ���Ե�ǰ����������ɫ
					formatter.setHighLight(doc, token, start + tokenPos, token.length());
				}
				// ��ʼ������һ������
				curStart = tokenPos + token.length();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ����������е���ɫ
	 */
	public void syntaxParse() {

		// ��ȡ�ĵ��ĸ�Ԫ�أ����ĵ��ڵ�ȫ������
		Element root = doc.getDefaultRootElement();
		// ��ȡ�ĵ��й��������λ��
		int cursorPos = this.getCaretPosition();
		int line = root.getElementIndex(cursorPos);
		syntaxLine(line);
	}

	// �ػ�������������к�
	public void paint(Graphics g) {
		super.paint(g);
		Element root = doc.getDefaultRootElement();
		// ����к�
		int line = root.getElementIndex(doc.getLength());
		// ������ɫ
		g.setColor(new Color(230, 230, 230));
		// ������ʾ�����ľ��ο�
		g.fillRect(0, 0, this.getMargin().left - 10, getSize().height);
		// �����кŵ���ɫ
		g.setColor(new Color(40, 40, 40));
		// ÿ�л���һ���к�
		for (int count = 0, j = 1; count <= line; count++, j++) {
			g.drawString(String.valueOf(j), 3, (int) ((count + 1) * 1.375 * StyleConstants.getFontSize(normalAttr)));
		}
	}
}
