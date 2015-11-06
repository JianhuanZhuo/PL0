package com.e23.compiler;


public class Interpret {

	/**
	 * 欲执行的代码
	 */
	private Code code;

	/**
	 * 运行时栈
	 */
	private RunningStack runningStack;

	/**
	 * 指定层的基址
	 */
	private Integer base;

	/**
	 * 程序指针
	 */
	private int program;

	/**
	 * 当前程序指令
	 */
	private Instruction instruction;

	/**
	 * 控制输出
	 */
	private consoleOutput consoleOutput;
	
	public Interpret(Code code) {
		this.code = code;
	}

	public void configConsole(consoleOutput consoleOutput) {
		this.consoleOutput = consoleOutput;
	}
	
	/**
	 * 直接执行
	 */
	public void exec() {

		// TODO 初始化状态
		runningStack = new RunningStack();
		program = 0;

		Integer SL = new Integer(0);
		Integer DL = new Integer(0);
		Integer RA = new Integer(0);
		runningStack.push(SL);
		runningStack.push(DL);
		runningStack.push(RA);

		base = SL;

		try {
			do {
				instruction = code.getInstruction(program);
				program++;
//				System.out.println("执行=》 "+instruction.toString());
				execInstruction();
			} while (0 != program);
		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("P = " + (program - 1) + "\tI = " + instruction.toString());
		}
	}

	/**
	 * 单指令执行函数
	 */
	private void execInstruction() {

		int tempInt;

		switch (instruction.getFunc()) {

			case lit:
				runningStack.push(instruction.getA());
				break;
			case opr:
				switch (instruction.getA()) { // operator
					case 0: {
						// TODO 创建过程三单元
						Integer SL = runningStack.get(runningStack.indexOf(base) + 0);
						Integer DL = runningStack.get(runningStack.indexOf(base) + 1);
						Integer RA = runningStack.get(runningStack.indexOf(base) + 2);

						// TODO 更新程序指针
						program = RA;

						// TODO 更新基址
						base = runningStack.get(DL);

						// TODO 弹栈至栈底
						while (SL != runningStack.pop());

					}
						break;

					case 1:
						runningStack.push(runningStack.pop().intValue() * -1);
						break;

					case 2:
						runningStack.push(runningStack.pop().intValue() + runningStack.pop().intValue());
						break;

					case 3:
						tempInt = runningStack.pop().intValue();
						runningStack.push(runningStack.pop().intValue() - tempInt);
						break;

					case 4:
						runningStack.push(runningStack.pop().intValue() * runningStack.pop().intValue());
						break;

					case 5:
						tempInt = runningStack.pop().intValue();
						runningStack.push(runningStack.pop().intValue() / tempInt);
						break;

					case 6:
						runningStack.push(runningStack.pop().intValue() % 2);
						break;

					case 8:
						runningStack.push((int) ((runningStack.pop().intValue() == runningStack.pop().intValue()) ? -1
								: 0));
						break;

					case 9:
						runningStack.push((int) ((runningStack.pop().intValue() != runningStack.pop().intValue()) ? -1
								: 0));
						break;

					case 10:
						tempInt = runningStack.pop().intValue();
						runningStack.push((int) ((runningStack.pop().intValue() < tempInt) ? -1 : 0));
						break;

					case 11:
						tempInt = runningStack.pop().intValue();
						runningStack.push((int) ((runningStack.pop().intValue() >= tempInt) ? -1 : 0));
						break;

					case 12:
						tempInt = runningStack.pop().intValue();
						runningStack.push((int) ((runningStack.pop().intValue() > tempInt) ? -1 : 0));
						break;

					case 13:
						tempInt = runningStack.pop().intValue();
						runningStack.push((int) ((runningStack.pop().intValue() <= tempInt) ? -1 : 0));
						break;

					case 14:
						consoleOutput.append("write:"+runningStack.pop()+"\n");
						break;
					default:
						break;
				}
				break;

			case lod:
				tempInt = baseCount(instruction.getL()) + instruction.getA();
				runningStack.push(runningStack.get(tempInt));
				break;

			case sto:
				//System.out.println("值：= " + runningStack.peek());
				tempInt = baseCount(instruction.getL()) + instruction.getA();
				runningStack.set(tempInt, runningStack.pop());
				break;

			case cal: // generate new block mark
			{
				// TODO 创建过程三单元
				Integer SL = Integer.valueOf(baseCount(instruction.getL()));
				Integer DL = Integer.valueOf(base);
				Integer RA = Integer.valueOf(program);
				runningStack.push(SL);
				runningStack.push(DL);
				runningStack.push(RA);
				// TODO 更新base指针与程序指针
				base = SL;
				program = instruction.getA();
			}
				break;

			case Int:
				for (int j = 0; j < instruction.getA() - 3; j++) {
					runningStack.push(new Integer(0));
				}
				break;

			case jmp:
				program = instruction.getA();
				break;

			case jpc:
				if (runningStack.pop() == 0) {
					program = instruction.getA();
				}
		}// end of switch()
	}

	/**
	 * 获得距当前层栈底有l层差的地址，索引
	 * 
	 * @param l
	 *            层差值
	 * @return 目标地址
	 */
	private int baseCount(int l) {

		int resBase = base;

		while (l > 0) {
			resBase = runningStack.get(resBase);
			l--;
		}
		return resBase;
	}

}
