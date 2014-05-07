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

public class CodeViewPanel implements Observer {
	private GUIMemoryDecorator memory;
	private JScrollPane scroller;

	public CodeViewPanel(Machine machine) {
		memory = (GUIMemoryDecorator) machine.getMemory();
		machine.addObserver(this);
	}

	public JComponent createCodeDisplay() {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new BorderLayout());
		returnPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK),
				"Code Memory View", TitledBorder.CENTER, TitledBorder.TOP));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel numPanel = new JPanel();
		JPanel sourcePanel = new JPanel();
		JPanel hexPanel = new JPanel();
		numPanel.setLayout(new GridLayout(0, 1));
		sourcePanel.setLayout(new GridLayout(0, 1));
		hexPanel.setLayout(new GridLayout(0, 1));

		panel.add(numPanel, BorderLayout.LINE_START);
		panel.add(sourcePanel, BorderLayout.CENTER);
		panel.add(hexPanel, BorderLayout.LINE_END);
		for (int i = 0; i < Memory.CODE_SIZE; i++) {
			numPanel.add(memory.getCodeLabel(i));
			sourcePanel.add(memory.getCodeText(i));
			hexPanel.add(memory.getCodeHex(i));
		}

		scroller = new JScrollPane(panel);
		returnPanel.add(scroller);
		return returnPanel;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (scroller != null && memory != null
				&& memory.getCodeHighlight() != null) {
			JScrollBar bar = scroller.getVerticalScrollBar();
			Rectangle bounds = memory.getCodeHighlight();
			bar.setValue(Math.max(0, bounds.y - 15 * bounds.height));
		}
	}
}
