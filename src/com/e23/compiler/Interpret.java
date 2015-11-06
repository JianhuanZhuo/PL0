package com.e23.compiler;


public class Interpret {

	/**
	 * ��ִ�еĴ���
	 */
	private Code code;

	/**
	 * ����ʱջ
	 */
	private RunningStack runningStack;

	/**
	 * ָ����Ļ�ַ
	 */
	private Integer base;

	/**
	 * ����ָ��
	 */
	private int program;

	/**
	 * ��ǰ����ָ��
	 */
	private Instruction instruction;

	/**
	 * �������
	 */
	private consoleOutput consoleOutput;
	
	public Interpret(Code code) {
		this.code = code;
	}

	public void configConsole(consoleOutput consoleOutput) {
		this.consoleOutput = consoleOutput;
	}
	
	/**
	 * ֱ��ִ��
	 */
	public void exec() {

		// TODO ��ʼ��״̬
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
//				System.out.println("ִ��=�� "+instruction.toString());
				execInstruction();
			} while (0 != program);
		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("P = " + (program - 1) + "\tI = " + instruction.toString());
		}
	}

	/**
	 * ��ָ��ִ�к���
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
						// TODO ������������Ԫ
						Integer SL = runningStack.get(runningStack.indexOf(base) + 0);
						Integer DL = runningStack.get(runningStack.indexOf(base) + 1);
						Integer RA = runningStack.get(runningStack.indexOf(base) + 2);

						// TODO ���³���ָ��
						program = RA;

						// TODO ���»�ַ
						base = runningStack.get(DL);

						// TODO ��ջ��ջ��
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
				//System.out.println("ֵ��= " + runningStack.peek());
				tempInt = baseCount(instruction.getL()) + instruction.getA();
				runningStack.set(tempInt, runningStack.pop());
				break;

			case cal: // generate new block mark
			{
				// TODO ������������Ԫ
				Integer SL = Integer.valueOf(baseCount(instruction.getL()));
				Integer DL = Integer.valueOf(base);
				Integer RA = Integer.valueOf(program);
				runningStack.push(SL);
				runningStack.push(DL);
				runningStack.push(RA);
				// TODO ����baseָ�������ָ��
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
	 * ��þ൱ǰ��ջ����l���ĵ�ַ������
	 * 
	 * @param l
	 *            ���ֵ
	 * @return Ŀ���ַ
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
