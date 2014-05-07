package pippin;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LoaderAdapter {
	Machine machine;
	File currentlyExecutingFile;
	Loader loader = new Loader();
	
	public LoaderAdapter(Machine machine_in){
		machine = machine_in;
	}
	
	public void load(){
		JFileChooser chooser = new JFileChooser(machine.getDirectoryManager().getExecutableDir());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pippin Executable Files", "pexe");
		chooser.setFileFilter(filter);
		int openOK = chooser.showOpenDialog(machine.getFrame());
		if(openOK == JFileChooser.APPROVE_OPTION){
			currentlyExecutingFile = chooser.getSelectedFile();
			if(currentlyExecutingFile != null && currentlyExecutingFile.exists()){
				String executableFile = currentlyExecutingFile .getAbsolutePath();
				machine.getDirectoryManager().recordExecutableDir(executableFile);
				finalStep();
			}
			else{
				JOptionPane.showMessageDialog(machine.getFrame(),
						"No file was selected or the file does not exist.\nCannot load the program",
						"Warning",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	public void finalStep(){
		try {
			loader.load(machine.getMemory(), currentlyExecutingFile);
			//machine.setRunnable(); ask?????
			machine.setAutoStepping(false); 
			machine.setAccumulator(0);
			machine.setProgramCounter(0);
			machine.callForUpdates();
		} 
		catch(IOException e) {
			JOptionPane.showMessageDialog(machine.getFrame(),
					"The selected has" + " problems. \nCannot load the program",
					"Warning",JOptionPane.WARNING_MESSAGE);
		}
	}
	public void reload(){
		if(currentlyExecutingFile != null && currentlyExecutingFile.exists()){
			finalStep();
		}
		else{
			JOptionPane.showMessageDialog(machine.getFrame(),
					"For some reason the currently executing file is lost.\n" + "Cannot load the program",
					"Warning",JOptionPane.WARNING_MESSAGE);
		}
	}
}
