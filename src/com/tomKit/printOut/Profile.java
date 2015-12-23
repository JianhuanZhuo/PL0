package com.tomKit.printOut;

import java.io.PrintStream;

class Profile {
	boolean printEnable = false;
	PrintStream printStream = System.out;
	String logFileName = "log.txt";

	@Override
	public String toString() {
		return "boolean printEnable = " + printEnable + "\n" + "PrintStream printStream = " + printStream + "\n"
				+ "String logFileName = " + logFileName + "\n";
	}
}