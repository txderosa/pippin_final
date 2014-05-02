package pippin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ProcessorViewPanel implements Observer {
	private GUIMemoryDecorator memory;
	private JScrollPane scroller;
	private JTextField accumulator;
	private JTextField programCounter;
	
	//Make this?
	private Machine machine;
	

	public ProcessorViewPanel(Machine machine) {
		memory = (GUIMemoryDecorator) machine.getMemory();
		machine.addObserver(this);
		this.machine=machine;
		accumulator = new JTextField();
		programCounter = new JTextField();
	}

	public JComponent createProcessorDisplay() {
		JPanel returnPanel = new JPanel();
		JLabel label1 = new JLabel("accumulator", SwingConstants.RIGHT);
		JLabel label2 = new JLabel("program counter", SwingConstants.RIGHT);
		returnPanel.setLayout(new GridLayout(1,0));
		returnPanel.add(label1);
		returnPanel.add(accumulator);
		returnPanel.add(label2);
		returnPanel.add(programCounter);
		
		return returnPanel;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		accumulator.setText("" + machine.getAccumulator());
		programCounter.setText(""+ machine.getProgramCounter());
	}

}
