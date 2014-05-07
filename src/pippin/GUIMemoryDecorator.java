package pippin;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUIMemoryDecorator implements MemoryInterface, Observer {
	public static final Color HIGHLIGHT_COLOR = Color.YELLOW;

	private DataGUI[] dataComponents = new DataGUI[MemoryInterface.DATA_SIZE];
	private CodeGUI[] codeComponents = new CodeGUI[MemoryInterface.CODE_SIZE];
	private Machine machine;
	private MemoryInterface memory;
	private int currentCodeHighlight = 0;
	private int currentDataHighlight = 0;
	private Rectangle dataHighlight;
	private Rectangle codeHighlight;

	public GUIMemoryDecorator(Machine m, MemoryInterface mem) {
		machine = m;
		memory = mem;
		for (int i = 0; i < MemoryInterface.CODE_SIZE; i++) {
			codeComponents[i] = new CodeGUI(i);
		}
		for (int i = 0; i < MemoryInterface.DATA_SIZE; i++) {
			dataComponents[i] = new DataGUI(i);
		}
		machine.addObserver(this);
	}

	public void setData(int index, int value) throws DataAccessException {
		if (index < 0 || index >= dataComponents.length) {
			throw new DataAccessException("ERROR: Cannot access data location "
					+ index);
		}
		memory.setData(index, value);
		dataComponents[currentDataHighlight].setColor(Color.WHITE);
		currentDataHighlight = index;
		dataComponents[currentDataHighlight].fillCells();
		dataComponents[currentDataHighlight].setColor(HIGHLIGHT_COLOR);
		dataHighlight = dataComponents[currentDataHighlight].rowIndex
				.getBounds();
	}

	public int getData(int index) throws DataAccessException {
		return memory.getData(index);
	}

	public void setCode(int index, long lng) throws CodeAccessException {
		if (index < 0 || index >= codeComponents.length) {
			throw new CodeAccessException("ERROR: Cannot access code location "
					+ index);
		}
		memory.setCode(index, lng);
		codeComponents[index].fillCells();
	}

	public long getCode(int index) throws CodeAccessException {
		return memory.getCode(index);
	}

	public void recolorCode() {
		codeComponents[currentCodeHighlight].setColor(Color.WHITE);
		currentCodeHighlight = machine.getProgramCounter();
		if (machine.getState() != States.NOTHING_LOADED) {
			codeComponents[currentCodeHighlight].setColor(HIGHLIGHT_COLOR);
		} else {
			dataComponents[currentDataHighlight].setColor(Color.WHITE);
		}
		codeHighlight = codeComponents[currentCodeHighlight].rowIndex
				.getBounds();
	}

	public void clearCode() {
		memory.clearCode();
		for (int i = 0; i < MemoryInterface.CODE_SIZE; i++) {
			codeComponents[i].rowInstr.setText("");
			codeComponents[i].rowHex.setText("");
		}
		for (int i = 0; i < MemoryInterface.DATA_SIZE; i++) {
			dataComponents[i].rowValue.setText("");
			dataComponents[i].rowHex.setText("");
		}
		codeComponents[currentCodeHighlight].setColor(Color.WHITE);
		currentCodeHighlight = 0;
		if (machine.getState() != States.NOTHING_LOADED) {
			codeComponents[currentCodeHighlight].setColor(HIGHLIGHT_COLOR);
		} else {
			codeComponents[currentDataHighlight].setColor(Color.WHITE);
		}
		codeHighlight = codeComponents[currentCodeHighlight].rowIndex
				.getBounds();
		dataComponents[currentDataHighlight].setColor(Color.WHITE);
		currentDataHighlight = 0;
		dataComponents[currentDataHighlight].setColor(HIGHLIGHT_COLOR);
	}

	public Rectangle getDataHighlight() {
		return dataHighlight;
	}

	public Rectangle getCodeHighlight() {
		return codeHighlight;
	}

	public JLabel getDataLabel(int index) {
		return dataComponents[index].rowIndex;
	}

	public JTextField getDataValue(int index) {
		return dataComponents[index].rowValue;
	}

	public JTextField getDataHex(int index) {
		return dataComponents[index].rowHex;
	}

	private class DataGUI {
		private JLabel rowIndex;
		private JTextField rowValue;
		private JTextField rowHex;
		private int location;

		public DataGUI(int loc) {
			location = loc;
			rowIndex = new JLabel(loc + ": ", JLabel.RIGHT);
			rowValue = new JTextField(10);
			rowHex = new JTextField(10);
			rowIndex.setOpaque(true);
		}

		public void fillCells() throws DataAccessException {
			int value = memory.getData(location);
			rowValue.setText("" + value);
			rowHex.setText("0x" + Integer.toHexString(value).toUpperCase());
		}

		public void setColor(Color color) {
			rowIndex.setBackground(color);
			rowValue.setBackground(color);
			rowHex.setBackground(color);
		}
	}

	public JLabel getCodeLabel(int index) {
		return codeComponents[index].rowIndex;
	}

	public JTextField getCodeText(int index) {
		return codeComponents[index].rowInstr;
	}

	public JTextField getCodeHex(int index) {
		return codeComponents[index].rowHex;
	}

	private class CodeGUI {
		private JLabel rowIndex;
		private JTextField rowInstr;
		private JTextField rowHex;
		private int location;

		public CodeGUI(int loc) {
			location = loc;
			rowIndex = new JLabel(loc + ": ", JLabel.RIGHT);
			rowInstr = new JTextField(10);
			rowHex = new JTextField(10);
			rowIndex.setOpaque(true);
		}

		public void fillCells() throws CodeAccessException {
			long lng = memory.getCode(location);
			long op = lng >> 32;
			int arg = (int) (lng ^ (op << 32));
			boolean indirect = false;
			if (op % 2 == 1) {
				indirect = true;
			}
			int opcode = (int) op / 2;
			Instruction instr = machine.getiSet()[opcode];
			String inString = instr.toString();
			if (indirect)
				inString += "#";
			rowInstr.setText(inString + " "
					+ Integer.toHexString(arg).toUpperCase());
			rowHex.setText("0x" + Long.toHexString(lng).toUpperCase());
		}

		public void setColor(Color color) {
			rowIndex.setBackground(color);
			rowInstr.setBackground(color);
			rowHex.setBackground(color);
		}
	}

	@Override
	public void clearData() {
		memory.clearData();
		for (int i = 0; i < MemoryInterface.DATA_SIZE; i++) {
			dataComponents[i].rowValue.setText("");
			dataComponents[i].rowHex.setText("");
		}
		dataComponents[currentDataHighlight].setColor(Color.WHITE);
		currentDataHighlight = 0;
		dataComponents[currentDataHighlight].setColor(HIGHLIGHT_COLOR);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		recolorCode();
	}

	

}
