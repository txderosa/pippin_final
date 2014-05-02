package pippin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class DataViewPanel implements Observer {
	private GUIMemoryDecorator memory;
	private JScrollPane scroller;

	public DataViewPanel(Machine machine) {
		memory = (GUIMemoryDecorator)machine.getMemory();
		machine.addObserver(this);
	}

	
	public JComponent createDataDisplay() {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new BorderLayout());
		returnPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), "Data Memory View", 
				TitledBorder.CENTER, TitledBorder.TOP));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel numPanel = new JPanel();
		JPanel intPanel = new JPanel();
		JPanel hexPanel = new JPanel();
		numPanel.setLayout(new GridLayout(0,1));
		intPanel.setLayout(new GridLayout(0,1));
		hexPanel.setLayout(new GridLayout(0,1));

		panel.add(numPanel, BorderLayout.LINE_START);
		panel.add(intPanel, BorderLayout.CENTER);
		panel.add(hexPanel, BorderLayout.LINE_END);
		for(int i = 0; i < MemoryInterface.DATA_SIZE; i++) {
			numPanel.add(memory.getDataLabel(i));
			intPanel.add(memory.getDataValue(i));
			hexPanel.add(memory.getDataHex(i));
		}
		scroller = new JScrollPane(panel);
		returnPanel.add(scroller);
		return returnPanel;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(scroller != null && memory != null && memory.getDataHighlight() != null) {
			JScrollBar bar= scroller.getVerticalScrollBar();
			Rectangle bounds = memory.getDataHighlight();
			bar.setValue(Math.max(0, bounds.y - 15*bounds.height));
		}
	}	
}

