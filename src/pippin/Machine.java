package pippin;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Machine extends Observable {
	class CPU {
		private int accumulator;
		private int programCounter;
	}

	private CPU cpu = new CPU();
	private Instruction[] iSet;
	private MemoryInterface memory;
	private States state;
	private boolean halted = false;
	private boolean autoStepping = false;
	private JFrame frame;

	public Machine() {
		memory = new Memory();
		iSet = new Instruction[32];
		setGUIMemory();

	}

	public void setGUIMemory() {
		memory = new GUIMemoryDecorator(this, memory);
		iSet[0] = new NOP(this, memory);
		iSet[1] = new LOD(this, memory);
		iSet[2] = new LODI(this, memory);
		iSet[3] = new STO(this, memory);
		iSet[4] = new ADD(this, memory);
		iSet[5] = new SUB(this, memory);
		iSet[6] = new MUL(this, memory);
		iSet[7] = new DIV(this, memory);
		iSet[8] = new ADDI(this, memory);
		iSet[9] = new SUBI(this, memory);
		iSet[0xA] = new MULI(this, memory);
		iSet[0xB] = new DIVI(this, memory);
		iSet[0x10] = new AND(this, memory);
		iSet[0x11] = new ANDI(this, memory);
		iSet[0x12] = new NOT(this, memory);
		iSet[0x13] = new CMPZ(this, memory);
		iSet[0x14] = new CMPL(this, memory);
		iSet[0x1A] = new JUMP(this, memory);
		iSet[0x1B] = new JMPZ(this, memory);
		iSet[0x1F] = new HALT(this, memory);
	}

	public int getAccumulator() {
		return cpu.accumulator;
	}

	public void setAccumulator(int i) {
		cpu.accumulator = i;
	}

	public int getProgramCounter() {
		return cpu.programCounter;
	}

	public void setProgramCounter(int i) {
		cpu.programCounter = i;
	}

	public void incrementCounter() {
		cpu.programCounter++;
	}

	public MemoryInterface getMemory() {
		return memory;
	}

	public Instruction[] getiSet() {
		return iSet;
	}

	public States getState() {
		return state;
	}

	public void setMemory(MemoryInterface memory) {
		this.memory = memory;
	}

	public boolean isAutoStepping() {
		return autoStepping;
	}

	public void setAutoStepping(boolean autoStepping) {
		this.autoStepping = autoStepping;
	}

	public void halt() {
		halted = true;
		state = States.PROGRAM_HALTED;
	}

	// ------------------------------------------------------
	// NEED GETTER FOR HALTED AND FRAME INSTANCE? And setter for iset?

	public boolean isHalted() {
		return halted;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public void setiSet(Instruction[] iSet) {
		this.iSet = iSet;
	}

	// ------------------------------------------------------
	public static void main(String[] args) throws DataAccessException,
			FileNotFoundException, CodeAccessException {
		test3();
	}

	public static void test1() throws DataAccessException,
			FileNotFoundException, CodeAccessException {
		Machine m = new Machine();
		MemoryInterface data = m.getMemory();
		LoaderInterface loader = new Loader();
		for (int i = 0; i < MemoryInterface.CODE_SIZE; i++) {
			data.setCode(i, -1); // an invalid code
		}

		System.out.println(loader.load(data, new File("factorial8.pexe")));

		long lng = 0;
		Instruction instr = m.iSet[0x1F];
		do {
			lng = data.getCode(m.getProgramCounter());
			System.out.println(lng);
			if (lng >= 0) {
				long op = lng >> 32;
				int arg = (int) (lng ^ (op << 32));
				boolean indirect = false;
				if (op % 2 == 1) {
					indirect = true;
				}
				int opcode = (int) op / 2;
				instr = m.iSet[opcode];
				m.setAccumulator(instr.execute(arg, indirect));
				System.out.println(instr.toString() + " " + arg);
				System.out.println("Memory: 0 => " + data.getData(0)
						+ ", 1 => " + data.getData(1));
			}
		} while (!m.halted);
	}

	public void step() {
		if (!halted) {
			long lng;
			Instruction instr = iSet[0x1F];
			try {
				lng = memory.getCode(getProgramCounter());
				if (lng >= 0) {
					long op = lng >> 32;
					int arg = (int) (lng ^ (op << 32));
					boolean indirect = false;
					if (op % 2 == 1) {
						indirect = true;
					}
					int opcode = (int) op / 2;
					instr = getiSet()[opcode];
					setAccumulator(instr.execute(arg, indirect));
					setChanged();
					notifyObservers();
				}
			} catch (CodeAccessException e) {
				JOptionPane.showMessageDialog(null,
						"There was a code access exception executing " + instr,
						"Error on code line " + this.getProgramCounter(),
						JOptionPane.WARNING_MESSAGE);
				halt();
			} catch (DataAccessException e) {
				JOptionPane.showMessageDialog(null,
						"There was a data access exception executing " + instr,
						"Error on code line " + this.getProgramCounter(),
						JOptionPane.WARNING_MESSAGE);
				halt();
			} catch (NullPointerException e) {
				JOptionPane
						.showMessageDialog(
								null,
								"There was a Null pointer exception executing "
										+ instr,
								"Error on code line "
										+ this.getProgramCounter(),
								JOptionPane.WARNING_MESSAGE);
				halt();
			} catch (DivideByZeroException e) {
				JOptionPane.showMessageDialog(null,
						"There was a divide by zero exception executing "
								+ instr,
						"Error on code line " + this.getProgramCounter(),
						JOptionPane.WARNING_MESSAGE);
				halt();
			}

		}

	}

	public static void test3() throws FileNotFoundException {
		Machine m = new Machine();
		m.frame = new JFrame();
		m.frame.setSize(600, 500);
		DataViewPanel dvp = new DataViewPanel(m);
		m.frame.add(dvp.createDataDisplay(), BorderLayout.LINE_END);
		CodeViewPanel cvp = new CodeViewPanel(m);
		m.frame.add(cvp.createCodeDisplay(), BorderLayout.CENTER);
		ProcessorViewPanel pvp = new ProcessorViewPanel(m);
		m.frame.add(pvp.createProcessorDisplay(), BorderLayout.PAGE_START);
		;
		m.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m.frame.setVisible(true);

		LoaderInterface loader = new Loader();
		m.memory.clearCode();
		m.memory.clearData();
		System.out.println(loader.load(m.memory, new File("factorial8.pexe")));

		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			m.step();
		} while (!m.halted);
	}

	public static void test2() throws DataAccessException,
			FileNotFoundException, CodeAccessException {
		Machine m = new Machine();
		m.frame = new JFrame();
		m.frame.setSize(300, 500);
		DataViewPanel dvp = new DataViewPanel(m);
		m.frame.add(dvp.createDataDisplay());
		m.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m.frame.setVisible(true);

		LoaderInterface loader = new Loader();
		m.memory.clearCode();
		m.memory.clearData();
		System.out.println(loader.load(m.memory, new File("factorial8.pexe")));
		long lng = 0;
		Instruction instr = m.iSet[0x1F];
		do {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lng = m.memory.getCode(m.getProgramCounter());
			System.out.println(lng);
			if (lng >= 0) {
				long op = lng >> 32;
				int arg = (int) (lng ^ (op << 32));
				boolean indirect = false;
				if (op % 2 == 1) {
					indirect = true;
				}
				int opcode = (int) op / 2;
				instr = m.iSet[opcode];
				m.setAccumulator(instr.execute(arg, indirect));
				System.out.println(instr.toString() + " " + arg);
				System.out.println("Memory: 0 => " + m.memory.getData(0)
						+ ", 1 => " + m.memory.getData(1));
			}
		} while (!m.halted);
	}

}
