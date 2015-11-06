package com.e23.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.e23.compiler.consoleOutput;



public class ConsoleFrame extends JFrame implements consoleOutput{

	private JTextArea contents = new JTextArea();
	
	public ConsoleFrame(){
		super("����̨���");
		
		contents.setBackground(Color.BLACK);
		contents.setForeground(Color.WHITE);
		contents.setEditable(false);
		add(new JScrollPane(contents));
		

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(600, 200, screenSize.width - 800, screenSize.height - 300);
		
		setVisible(false);
	}
	
	/**
	 * ��ָ���ַ���append׷�����������
	 *	@param append
	 */
	public void append(String append) {
		contents.setText(contents.getText()+"\n"+append);
	}
	
	/**
	 * ��տ������
	 */
	public void clear() {
		contents.setText("");
	}

	/**
	 * ���ָ���ַ���append���������
	 */
	public void outString(String string) {
		contents.setText(string);		
	}
}
