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
 * 语法格式器 用于定义一个语法样式格式定义的类
 * 
 * @author ZhuoJianHuan
 * @date 2015年6月22日
 */
public class SyntaxFormatter {

	/**
	 * 以一个Map保存关键字和颜色的对应关系
	 */
	private Map<SimpleAttributeSet, ArrayList<String>> attMap = new HashMap<>();

	/**
	 * 定义文档的正常文本的外观属性
	 */
	SimpleAttributeSet normalAttr = new SimpleAttributeSet();

	/**
	 * 语法各时期的构造函数 <br>
	 * 该文件一般以.stx为文件后缀<br>
	 * 文件内容为:
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
	 * 第一行表示欲表现的颜色，第二行表示指定的关键字
	 * 
	 * @param syntaxFile
	 */
	public SyntaxFormatter(String syntaxFile) {
		// 设置正常文本的颜色、大小
		StyleConstants.setForeground(normalAttr, Color.BLACK);
		StyleConstants.setFontSize(normalAttr, 16);
		
		//TODO 设置
		
		//TODO 创建一个Scanner对象，负责根据语法文件加载颜色信息
		Scanner scaner = null;
		try {
			scaner = new Scanner(new File(syntaxFile));

			int color = -1;
			ArrayList<String> keywords = new ArrayList<>();
			// 不断读取语法文件的内容行
			while (scaner.hasNextLine()) {
				String line = scaner.nextLine();
				// 如果当前行以#开头
				if (line.startsWith("#")) {
					if (keywords.size() > 0 && color > -1) {
						// 取出当前行的颜色值，并封装成SimpleAttributeSet对象
						SimpleAttributeSet att = new SimpleAttributeSet();
						StyleConstants.setForeground(att, new Color(color));
						StyleConstants.setFontSize(att, 16);
						// 将当前颜色和关键字List对应起来
						attMap.put(att, keywords);
					}
					// 重新创建新的关键字List，为下一个语法格式准备
					keywords = new ArrayList<>();
					color = Integer.parseInt(line.substring(1), 16);
				} else {
					// 对于普通行，每行内容添加到关键字List里
					if (line.trim().length() > 0) {
						keywords.add(line.trim());
					}
				}
			}
			// 把所有关键字和颜色对应起来
			if (keywords.size() > 0 && color > -1) {
				SimpleAttributeSet att = new SimpleAttributeSet();
				StyleConstants.setForeground(att, new Color(color));
				StyleConstants.setFontSize(att, 16);
				attMap.put(att, keywords);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("丢失语法文件：" + e.getMessage());
		} finally {
			scaner.close();
		}
	}

	/**
	 * 返回该格式器里正常文本的外观属性 除去特殊定义的关键字、表达式之外的文本视为正常文本
	 * 
	 * @return 正常文本的外观属性集
	 */
	public SimpleAttributeSet getNormalAttributeSet() {
		return normalAttr;
	}

	/**
	 * 设置语法高亮
	 * 
	 * @param doc
	 *            指定的段落
	 * @param token
	 *            段落中的特定关键字
	 * @param start
	 *            指定格式处理的起始位置
	 * @param length
	 *            指定格式处理的长度
	 */
	public void setHighLight(StyledDocument doc, String token, int start, int length) {
		// 保存需要对当前单词对应的外观属性
		SimpleAttributeSet currentAttributeSet = null;
		outer: for (SimpleAttributeSet att : attMap.keySet()) {
			// 取出当前颜色对应的所有关键字
			ArrayList<String> keywords = attMap.get(att);
			// 遍历所有关键字
			for (String keyword : keywords) {
				// 如果该关键字与当前单词相同
				if (keyword.equals(token)) {
					// 跳出循环，并设置当前单词对应的外观属性
					currentAttributeSet = att;
					break outer;
				}
			}
		}
		// 如果当前单词对应的外观属性不为空
		if (currentAttributeSet != null) {
			// 设置当前单词的颜色
			doc.setCharacterAttributes(start, length, currentAttributeSet, false);
		}
		// 否则使用普通外观来设置该单词
		else {
			doc.setCharacterAttributes(start, length, normalAttr, false);
		}
	}
}
