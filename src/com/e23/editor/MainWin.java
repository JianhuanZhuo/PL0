package com.e23.editor;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.e23.compiler.Compiler;

public class MainWin extends JFrame {

	MainTextPane mainTextPane;
	LogOutPut logOutPut;
	Compiler compiler;
	ConsoleFrame consoleFrame = new ConsoleFrame();

	// ��������
	private GridBagLayout editGridBagLayout = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	private File fileEditing = null;

	// ָ�������������Ļ�ļ�϶
	private int inset = 50;

	// TODO �˵���������
	private MenuBar menuBar = new MenuBar();

	Menu file = new Menu("�ļ�");
	Menu edit = new Menu("�༭");
	Menu format = new Menu("��ʽ");
	Menu run = new Menu("����");
	Menu debug = new Menu("����");
	Menu help = new Menu("����");

	MenuItem newItem = new MenuItem("�½�", new MenuShortcut(KeyEvent.VK_N));
	MenuItem openItem = new MenuItem("��...", new MenuShortcut(KeyEvent.VK_O));
	MenuItem saveItem = new MenuItem("����", new MenuShortcut(KeyEvent.VK_S));
	MenuItem saveAsItem = new MenuItem("���Ϊ...");
	MenuItem exitItem = new MenuItem("�˳�", new MenuShortcut(KeyEvent.VK_Q));
	// CheckboxMenuItem autoWrap = new CheckboxMenuItem("�Զ�����");
	MenuItem undoItem = new MenuItem("����", new MenuShortcut(KeyEvent.VK_Z));
	MenuItem redoItem = new MenuItem("����", new MenuShortcut(KeyEvent.VK_Y));
	MenuItem cutItem = new MenuItem("����", new MenuShortcut(KeyEvent.VK_X));
	MenuItem copyItem = new MenuItem("����", new MenuShortcut(KeyEvent.VK_C));
	MenuItem pasteItem = new MenuItem("ճ��", new MenuShortcut(KeyEvent.VK_V));
	// ����commentItem�˵��ָ��ʹ�� Ctrl+Shift+/ ��ݼ�
	MenuItem commentItem = new MenuItem("ע��", new MenuShortcut(KeyEvent.VK_SLASH));
	MenuItem cancelItem = new MenuItem("ȡ��ע��", new MenuShortcut(KeyEvent.VK_SLASH, true));

	MenuItem buildItem = new MenuItem("����", new MenuShortcut(KeyEvent.VK_F5));
	MenuItem execItem = new MenuItem("����", new MenuShortcut(KeyEvent.VK_F5, true));
	MenuItem clearItem = new MenuItem("��տ���̨");

	MenuItem debugController = new MenuItem("��������");
	MenuItem stepInto = new MenuItem("����", new MenuShortcut(KeyEvent.VK_F9));
	MenuItem stepOver = new MenuItem("����", new MenuShortcut(KeyEvent.VK_F10));

	MenuItem helpItem = new MenuItem("����", new MenuShortcut(KeyEvent.VK_F1));
	MenuItem aboutItem = new MenuItem("����");

	private FileDialog openDia = new FileDialog(this, "���ļ�", FileDialog.LOAD); // �򿪶Ի���
	private FileDialog saveDia = new FileDialog(this, "�ļ�����", FileDialog.SAVE); // ����Ի���

	/**
	 * ���캯����
	 * 
	 * @param title
	 *            ����ı�����
	 */
	public MainWin(String title) {
		super(title);

		// TODO �˵�����
		configMenu();
		setMenuBar(menuBar);
		// TODO ��������������
		setLayout(editGridBagLayout);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 8;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		mainTextPane = new MainTextPane();
		JScrollPane scrollPane = new JScrollPane(mainTextPane);
		editGridBagLayout.setConstraints(scrollPane, gbc);
		add(scrollPane);

		gbc.weighty = 0;
		Label label = new Label("������־ - log");
		editGridBagLayout.setConstraints(label, gbc);
		add(label);

		gbc.weighty = 3;
		logOutPut = new LogOutPut();
		scrollPane = new JScrollPane(logOutPut);
		editGridBagLayout.setConstraints(scrollPane, gbc);
		add(scrollPane);

		// TODO ��������
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
		setVisible(true);
	}

	private boolean buildAction() {
		File fileBuilding = new File("builf_temp");

		try {
			BufferedWriter bufw = new BufferedWriter(new FileWriter(fileBuilding));

			String text = mainTextPane.getText();

			if (text.equals("")) {
				logOutPut.setText(logOutPut.getText() + "\n������Դ�ļ��ɱ��룡");
				bufw.close();
				return false;
			}
			bufw.write(text);

			bufw.close();
		} catch (IOException ex) {
			throw new RuntimeException("�ļ�����ʧ�ܣ�");
		}

		compiler = new Compiler(consoleFrame, "builf_temp");
		String buildRes = compiler.build();
		if (buildRes.equals("")) {
			logOutPut.setText(logOutPut.getText() + "\n" + "����ɹ���");
			return true;
		} else {
			logOutPut.setText(logOutPut.getText() + "\n" + buildRes);
			return false;
		}

	}

	/**
	 * ���ò˵���������Ӧ�ļ�����
	 */
	private void configMenu() {

		file.add(newItem);
		file.add(openItem);
		file.add(saveItem);
		file.add(saveAsItem);
		file.addSeparator();
		file.add(exitItem);

		edit.add(undoItem);
		edit.add(redoItem);
		edit.addSeparator();
		edit.add(cutItem);
		edit.add(copyItem);
		edit.add(pasteItem);

		run.add(buildItem);
		run.add(execItem);
		run.add(clearItem);

		debug.add(debugController);
		debug.add(stepInto);
		debug.add(stepOver);
		stepInto.setEnabled(false);
		stepOver.setEnabled(false);

		help.add(helpItem);
		help.add(aboutItem);

		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(run);
		menuBar.add(debug);
		menuBar.add(help);

		// TODO ����ļ��������
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileEditing == null) {
					saveDia.setVisible(true);
					String dirPath = saveDia.getDirectory();
					String fileName = saveDia.getFile();

					if (dirPath == null || fileName == null)
						return;
					fileEditing = new File(dirPath, fileName);
					setTitle(fileName + " - PL0������");
				}

				try {
					BufferedWriter bufw = new BufferedWriter(new FileWriter(fileEditing));

					String text = mainTextPane.getText();

					bufw.write(text);

					bufw.close();
				} catch (IOException ex) {
					throw new RuntimeException("�ļ�����ʧ�ܣ�");
				}
			}
		});

		// TODO ������Ϊ����
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				saveDia.setVisible(true);
				String dirPath = saveDia.getDirectory();
				String fileName = saveDia.getFile();

				if (dirPath == null || fileName == null)
					return;
				fileEditing = new File(dirPath, fileName);
				setTitle(fileName + " - PL0������");

				try {
					BufferedWriter bufw = new BufferedWriter(new FileWriter(fileEditing));

					String text = mainTextPane.getText();

					bufw.write(text);

					bufw.close();
				} catch (IOException ex) {
					throw new RuntimeException("�ļ�����ʧ�ܣ�");
				}
			}

		});

		// TODO ����½��ļ�����
		newItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mainTextPane.setText("");// ����ı�
				fileEditing = null;
				setTitle("�½��ļ�" + " - PL0������");
			}
		});

		// TODO ��Ӵ��ļ�����
		openItem.addActionListener(new ActionListener() {
			// ���ô��ļ�����
			public void actionPerformed(ActionEvent e) {
				openDia.setVisible(true);
				String dirPath = openDia.getDirectory();// ��ȡ�ļ�·��
				String fileName = openDia.getFile();// ��ȡ�ļ�����
				// System.out.println(dirPath +"++"+ fileName);

				// �����·�� �� Ŀ¼Ϊ�� �򷵻ؿ�
				if (dirPath == null || fileName == null)
					return;

				mainTextPane.setText("");// ����ı�

				fileEditing = new File(dirPath, fileName);
				setTitle(fileName + " - PL0������");

				try {
					BufferedReader bufr = new BufferedReader(new FileReader(fileEditing));

					String line = null;

					while ((line = bufr.readLine()) != null) {
						mainTextPane.setText(mainTextPane.getText() + line + "\r\n");
					}
					mainTextPane.syntaxParseAll();
					bufr.close();
				} catch (IOException ex) {
					throw new RuntimeException("�ļ���ȡʧ�ܣ�");
				}

			}
		});

		// TODO ����˳���������
		exitItem.addActionListener(new ActionListener() {
			// �����˳�����
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// TODO ���������رռ���
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// TODO ��ӹ�������
		buildItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				buildAction();
			}
		});

		// TODO ������ִ�м���
		execItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					consoleFrame.clear();
					consoleFrame.outString("����ʼ����\n");
					compiler.exec();
					consoleFrame.setVisible(true);
				} catch (NullPointerException e2) {
					logOutPut.setText(logOutPut.getText() + "\n��������ǰ���ȱ���һ�Σ�");
				}

			}
		});

		//TODO ����̨������ռ���
		clearItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				consoleFrame.clear();
			}
		});

		//TODO �������Լ���
		debugController.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				stepInto.setEnabled(false);
				stepOver.setEnabled(false);
				if (buildAction()) {
					stepInto.setEnabled(true);
					stepOver.setEnabled(true);
				}
			}

		});

	}

	public static void main(String[] args) {
		new MainWin("PL0������");
	}
}
