package com.e23.editor;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * �﷨��ʽ�� ���ڶ���һ���﷨��ʽ��ʽ�������
 * 
 * @author ZhuoJianHuan
 * @date 2015��6��22��
 */
public class SyntaxFormatter {

	/**
	 * ��һ��Map����ؼ��ֺ���ɫ�Ķ�Ӧ��ϵ
	 */
	private Map<SimpleAttributeSet, ArrayList<String>> attMap = new HashMap<SimpleAttributeSet, ArrayList<String>>();

	/**
	 * �����ĵ��������ı����������
	 */
	SimpleAttributeSet normalAttr = new SimpleAttributeSet();

	/**
	 * �﷨��ʱ�ڵĹ��캯�� <br>
	 * ���ļ�һ����.stxΪ�ļ���׺<br>
	 * �ļ�����Ϊ:
	 * 
	 * <pre>
	 * <B>#FF0000</B>
	 * System
	 * Class
	 * String
	 * Integer
	 * Object
	 * </pre>
	 * 
	 * ��һ�б�ʾ�����ֵ���ɫ���ڶ��б�ʾָ���Ĺؼ���
	 * 
	 * @param syntaxFile
	 */
	public SyntaxFormatter(String syntaxFile) {
		// ���������ı�����ɫ����С
		StyleConstants.setForeground(normalAttr, Color.BLACK);
		StyleConstants.setFontSize(normalAttr, 16);
		
		//TODO ����
		
		//TODO ����һ��Scanner���󣬸�������﷨�ļ�������ɫ��Ϣ
		Scanner scaner = null;
		try {
			scaner = new Scanner(new File(syntaxFile));

			int color = -1;
			ArrayList<String> keywords = new ArrayList<String>();
			// ���϶�ȡ�﷨�ļ���������
			while (scaner.hasNextLine()) {
				String line = scaner.nextLine();
				// �����ǰ����#��ͷ
				if (line.startsWith("#")) {
					if (keywords.size() > 0 && color > -1) {
						// ȡ����ǰ�е���ɫֵ������װ��SimpleAttributeSet����
						SimpleAttributeSet att = new SimpleAttributeSet();
						StyleConstants.setForeground(att, new Color(color));
						StyleConstants.setFontSize(att, 16);
						// ����ǰ��ɫ�͹ؼ���List��Ӧ����
						attMap.put(att, keywords);
					}
					// ���´����µĹؼ���List��Ϊ��һ���﷨��ʽ׼��
					keywords = new ArrayList<String>();
					color = Integer.parseInt(line.substring(1), 16);
				} else {
					// ������ͨ�У�ÿ��������ӵ��ؼ���List��
					if (line.trim().length() > 0) {
						keywords.add(line.trim());
					}
				}
			}
			// �����йؼ��ֺ���ɫ��Ӧ����
			if (keywords.size() > 0 && color > -1) {
				SimpleAttributeSet att = new SimpleAttributeSet();
				StyleConstants.setForeground(att, new Color(color));
				StyleConstants.setFontSize(att, 16);
				attMap.put(att, keywords);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("��ʧ�﷨�ļ���" + e.getMessage());
		} finally {
			scaner.close();
		}
	}

	/**
	 * ���ظø�ʽ���������ı���������� ��ȥ���ⶨ��Ĺؼ��֡����ʽ֮����ı���Ϊ�����ı�
	 * 
	 * @return �����ı���������Լ�
	 */
	public SimpleAttributeSet getNormalAttributeSet() {
		return normalAttr;
	}

	/**
	 * �����﷨����
	 * 
	 * @param doc
	 *            ָ���Ķ���
	 * @param token
	 *            �����е��ض��ؼ���
	 * @param start
	 *            ָ����ʽ�������ʼλ��
	 * @param length
	 *            ָ����ʽ����ĳ���
	 */
	public void setHighLight(StyledDocument doc, String token, int start, int length) {
		// ������Ҫ�Ե�ǰ���ʶ�Ӧ���������
		SimpleAttributeSet currentAttributeSet = null;
		outer: for (SimpleAttributeSet att : attMap.keySet()) {
			// ȡ����ǰ��ɫ��Ӧ�����йؼ���
			ArrayList<String> keywords = attMap.get(att);
			// �������йؼ���
			for (String keyword : keywords) {
				// ����ùؼ����뵱ǰ������ͬ
				if (keyword.equals(token)) {
					// ����ѭ���������õ�ǰ���ʶ�Ӧ���������
					currentAttributeSet = att;
					break outer;
				}
			}
		}
		// �����ǰ���ʶ�Ӧ��������Բ�Ϊ��
		if (currentAttributeSet != null) {
			// ���õ�ǰ���ʵ���ɫ
			doc.setCharacterAttributes(start, length, currentAttributeSet, false);
		}
		// ����ʹ����ͨ��������øõ���
		else {
			doc.setCharacterAttributes(start, length, normalAttr, false);
		}
	}
}
