package com.e23.lexer;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * <TN>->l|l<标识符>
 * 
 * @author keepf
 *
 */
public class LexerFrame extends JFrame {

	/**
	 * 默认串行版本号
	 */
	private static final long serialVersionUID = 1L;

	public LexerFrame() {
		init();
	}
	
	/**
	 * 可视化界面布局初始化
	 */
	private void init() {
		
		//TODO 添加组件
		
		//TODO 设置布局
		
		//TODO 设置JFrame属性
		setTitle("LexerPanel");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int inset = 100;
		setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
		setVisible(true);
	}
	// 测试的
	// 输出测试段，测试按钮
	// 输出测试结果

	// 输出结果
	// 形成可视化输出
	// 形成词法分析接口
	// 查看当前词法分析接口

	// 输入规则文件
	// 打开文件输入

	
	
	/**
	 * 测试用例
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new LexerFrame();
	}
}
