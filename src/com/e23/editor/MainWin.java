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

	// 布局声明
	private GridBagLayout editGridBagLayout = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	private File fileEditing = null;

	// 指定窗体距整个屏幕的间隙
	private int inset = 50;

	// TODO 菜单对象声明
	private MenuBar menuBar = new MenuBar();

	Menu file = new Menu("文件");
	Menu edit = new Menu("编辑");
	Menu format = new Menu("格式");
	Menu run = new Menu("运行");
	Menu debug = new Menu("调试");
	Menu help = new Menu("帮助");

	MenuItem newItem = new MenuItem("新建", new MenuShortcut(KeyEvent.VK_N));
	MenuItem openItem = new MenuItem("打开...", new MenuShortcut(KeyEvent.VK_O));
	MenuItem saveItem = new MenuItem("保存", new MenuShortcut(KeyEvent.VK_S));
	MenuItem saveAsItem = new MenuItem("另存为...");
	MenuItem exitItem = new MenuItem("退出", new MenuShortcut(KeyEvent.VK_Q));
	// CheckboxMenuItem autoWrap = new CheckboxMenuItem("自动换行");
	MenuItem undoItem = new MenuItem("撤销", new MenuShortcut(KeyEvent.VK_Z));
	MenuItem redoItem = new MenuItem("重做", new MenuShortcut(KeyEvent.VK_Y));
	MenuItem cutItem = new MenuItem("剪切", new MenuShortcut(KeyEvent.VK_X));
	MenuItem copyItem = new MenuItem("复制", new MenuShortcut(KeyEvent.VK_C));
	MenuItem pasteItem = new MenuItem("粘贴", new MenuShortcut(KeyEvent.VK_V));
	// 创建commentItem菜单项，指定使用 Ctrl+Shift+/ 快捷键
	MenuItem commentItem = new MenuItem("注释", new MenuShortcut(KeyEvent.VK_SLASH));
	MenuItem cancelItem = new MenuItem("取消注释", new MenuShortcut(KeyEvent.VK_SLASH, true));

	MenuItem buildItem = new MenuItem("构建", new MenuShortcut(KeyEvent.VK_F5));
	MenuItem execItem = new MenuItem("运行", new MenuShortcut(KeyEvent.VK_F5, true));
	MenuItem clearItem = new MenuItem("清空控制台");

	MenuItem debugController = new MenuItem("启动调试");
	MenuItem stepInto = new MenuItem("单步", new MenuShortcut(KeyEvent.VK_F9));
	MenuItem stepOver = new MenuItem("单行", new MenuShortcut(KeyEvent.VK_F10));

	MenuItem helpItem = new MenuItem("帮助", new MenuShortcut(KeyEvent.VK_F1));
	MenuItem aboutItem = new MenuItem("关于");

	private FileDialog openDia = new FileDialog(this, "打开文件", FileDialog.LOAD); // 打开对话框
	private FileDialog saveDia = new FileDialog(this, "文件保存", FileDialog.SAVE); // 保存对话框

	/**
	 * 构造函数，
	 * 
	 * @param title
	 *            窗体的标题名
	 */
	public MainWin(String title) {
		super(title);

		// TODO 菜单设置
		configMenu();
		setMenuBar(menuBar);
		// TODO 窗体组件添加设置
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
		Label label = new Label("编译日志 - log");
		editGridBagLayout.setConstraints(label, gbc);
		add(label);

		gbc.weighty = 3;
		logOutPut = new LogOutPut();
		scrollPane = new JScrollPane(logOutPut);
		editGridBagLayout.setConstraints(scrollPane, gbc);
		add(scrollPane);

		// TODO 窗体设置
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
				logOutPut.setText(logOutPut.getText() + "\n错误：无源文件可编译！");
				bufw.close();
				return false;
			}
			bufw.write(text);

			bufw.close();
		} catch (IOException ex) {
			throw new RuntimeException("文件保存失败！");
		}

		compiler = new Compiler(consoleFrame, "builf_temp");
		String buildRes = compiler.build();
		if (buildRes.equals("")) {
			logOutPut.setText(logOutPut.getText() + "\n" + "编译成功！");
			return true;
		} else {
			logOutPut.setText(logOutPut.getText() + "\n" + buildRes);
			return false;
		}

	}

	/**
	 * 配置菜单项，并添加相应的监听器
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

		// TODO 添加文件保存监听
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileEditing == null) {
					saveDia.setVisible(true);
					String dirPath = saveDia.getDirectory();
					String fileName = saveDia.getFile();

					if (dirPath == null || fileName == null)
						return;
					fileEditing = new File(dirPath, fileName);
					setTitle(fileName + " - PL0编译器");
				}

				try {
					BufferedWriter bufw = new BufferedWriter(new FileWriter(fileEditing));

					String text = mainTextPane.getText();

					bufw.write(text);

					bufw.close();
				} catch (IOException ex) {
					throw new RuntimeException("文件保存失败！");
				}
			}
		});

		// TODO 添加另存为监听
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				saveDia.setVisible(true);
				String dirPath = saveDia.getDirectory();
				String fileName = saveDia.getFile();

				if (dirPath == null || fileName == null)
					return;
				fileEditing = new File(dirPath, fileName);
				setTitle(fileName + " - PL0编译器");

				try {
					BufferedWriter bufw = new BufferedWriter(new FileWriter(fileEditing));

					String text = mainTextPane.getText();

					bufw.write(text);

					bufw.close();
				} catch (IOException ex) {
					throw new RuntimeException("文件保存失败！");
				}
			}

		});

		// TODO 添加新建文件监听
		newItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mainTextPane.setText("");// 清空文本
				fileEditing = null;
				setTitle("新建文件" + " - PL0编译器");
			}
		});

		// TODO 添加打开文件监听
		openItem.addActionListener(new ActionListener() {
			// 设置打开文件功能
			public void actionPerformed(ActionEvent e) {
				openDia.setVisible(true);
				String dirPath = openDia.getDirectory();// 获取文件路径
				String fileName = openDia.getFile();// 获取文件名称
				// System.out.println(dirPath +"++"+ fileName);

				// 如果打开路径 或 目录为空 则返回空
				if (dirPath == null || fileName == null)
					return;

				mainTextPane.setText("");// 清空文本

				fileEditing = new File(dirPath, fileName);
				setTitle(fileName + " - PL0编译器");

				try {
					BufferedReader bufr = new BufferedReader(new FileReader(fileEditing));

					String line = null;

					while ((line = bufr.readLine()) != null) {
						mainTextPane.setText(mainTextPane.getText() + line + "\r\n");
					}
					mainTextPane.syntaxParseAll();
					bufr.close();
				} catch (IOException ex) {
					throw new RuntimeException("文件读取失败！");
				}

			}
		});

		// TODO 添加退出按键监听
		exitItem.addActionListener(new ActionListener() {
			// 设置退出功能
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// TODO 添加主窗体关闭监听
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// TODO 添加构建监听
		buildItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				buildAction();
			}
		});

		// TODO 添加添加执行监听
		execItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					consoleFrame.clear();
					consoleFrame.outString("程序开始运行\n");
					compiler.exec();
					consoleFrame.setVisible(true);
				} catch (NullPointerException e2) {
					logOutPut.setText(logOutPut.getText() + "\n错误：运行前请先编译一次！");
				}

			}
		});

		//TODO 控制台内容清空监听
		clearItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				consoleFrame.clear();
			}
		});

		//TODO 启动调试监听
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
		new MainWin("PL0编译器");
	}
}
