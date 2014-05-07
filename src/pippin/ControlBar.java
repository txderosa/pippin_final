package pippin;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ControlBar implements Observer{
	Machine machine;
	private JButton stepButton = new JButton("Step");
	private JButton clearButton = new JButton("Clear");
	private JButton runButton = new JButton("Run/Pause");
	private JButton reloadButton = new JButton("Reload");
	
	public ControlBar(Machine machine_in){
		machine = machine_in;
		machine.addObserver(this);
	}
	
	public void checkEnabledButtons() { 
		runButton.setEnabled(machine.getState().getRunSuspendActive()); 
		stepButton.setEnabled(machine.getState().getStepActive());
		clearButton.setEnabled(machine.getState().getClearActive()); 
		reloadButton.setEnabled(machine.getState().getReloadActive());
	}
	@Override
	public void update(Observable arg0, Object arg1) {
	            checkEnabledButtons();
	}
	
	private class RunPauseListener implements ActionListener { @Override
		public void actionPerformed(ActionEvent arg0) { 
			if (machine.isAutoStepping()) {
				machine.setAutoStepping(false);
			}
			else {
				machine.setAutoStepping(true);
			}
		}
	}
	private class ClearListener implements ActionListener { 
		@Override
		public void actionPerformed(ActionEvent arg0) { 
			machine.clearAll();
		}
	}
	
	private class ReloadListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) { 
			machine.reload();
		}
	}
	
	private class StepListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) { 
			machine.step();
		}
	}
	
	public JComponent createControlDisplay() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		panel.add(stepButton);
		panel.setBackground(Color.WHITE);
		stepButton.addActionListener(new StepListener());
		
		return panel;
	}
}
	
