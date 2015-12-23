package com.tomKit.printOut;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class PO {

	private static Map<String, Profile> profiles = new HashMap<>();

	/**
	 * 获得上一级的调用者的配置文件
	 * 
	 * @return 上一级的调用者的配置文件
	 */
	private static Profile getOrderProfile() {
		// TODO 获得调用者类名
		String order = new Throwable().getStackTrace()[2].getClassName();
		Profile p = profiles.get(order);
		if (null == p) {
			p = new Profile();
			p.logFileName = order + ".txt";
			profiles.put(order, p);

		}
		return p;
	}

	/**
	 * 使能调试
	 */
	public static void enable() {

		getOrderProfile().printEnable = true;
	}

	/**
	 * 使能指定Java文件名，也就是使能指定的类
	 * 
	 * @param javaFileName
	 */
	public static void enable(String javaFileName) {
		Profile p = profiles.get(javaFileName);
		if (null == p) {
			p = new Profile();
			p.logFileName = javaFileName + ".txt";
			profiles.put(javaFileName, p);
		}
		p.printEnable = true;

	}

	/**
	 * 失能调试
	 */
	public static void disable() {

		getOrderProfile().printEnable = false;
	}

	/**
	 * 重定向输出流
	 * 
	 * @param newPrintStream
	 *            指定的输出流
	 */
	public static void setPrintStream(PrintStream newPrintStream) {
		if (null != newPrintStream) {
			getOrderProfile().printStream = newPrintStream;
		}
	}

	/**
	 * 设置调试输出文件名
	 * 
	 * @param fileName
	 */
	public static void setLogFile(String fileName) {
		if (null != fileName && !fileName.equals("")) {
			getOrderProfile().logFileName = fileName;
		}
	}

	/**
	 * 打印调试输出信息
	 * 
	 * @param content
	 *            需要打印的对象
	 */
	public static void p(Object content) {
		Profile p = getOrderProfile();
		if (p.printEnable) {
			p.printStream.println(content);
		}
	}

	public static void main(String[] args) {
		Reg.name();
	}

	public static void d() {
		// System.
		StackTraceElement stack[] = new Throwable().getStackTrace();
		for (int i = 0; i < stack.length; i++) {
			StackTraceElement s = stack[i];
			System.out.format(" ClassName:%d\t%s\n", i, s.getClassName());
			System.out.format("MethodName:%d\t%s\n", i, s.getMethodName());
			System.out.format("  FileName:%d\t%s\n", i, s.getFileName());
			System.out.format("LineNumber:%d\t%s\n\n", i, s.getLineNumber());
		}
	}

	public static void pl(Object object) {
		//p(object);
		Profile p = getOrderProfile();
		if (p.printEnable) {
			p.printStream.println(object);
		}
		//l(object.toString());
		// TODO add the date time
		String dataTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS").format(System.currentTimeMillis());
		String logMsg = dataTime + ":\n\t" + object.toString();
		// TODO write into the log file
		File f = new File(p.logFileName);

		FileWriter fw = null;
		try {
			fw = new FileWriter(f, true);
			fw.write(logMsg);
			fw.flush();

			// 清理操作
			fw.close();

		} catch (IOException e) {
			System.out.println("error ourror in logging!!!");
			e.printStackTrace();
		}
	}

	/**
	 * 输出调试信息知道日志文件
	 * 
	 * @param logMsg
	 *            调试信息
	 */
	public static void l(String logMsg) {
		Profile p = getOrderProfile();
		// TODO add the date time
		String dataTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS").format(System.currentTimeMillis());
		logMsg = dataTime + ":\n\t" + logMsg;
		// TODO write into the log file
		File f = new File(p.logFileName);

		FileWriter fw = null;
		try {
			fw = new FileWriter(f, true);
			fw.write(logMsg);
			fw.flush();

			// 清理操作
			fw.close();

		} catch (IOException e) {
			System.out.println("error ourror in logging!!!");
			e.printStackTrace();
		}
	}
}
