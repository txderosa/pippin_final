package pippin;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AssemblerAdapter {

	Assembler assembler = new Assembler();
	Machine machine;

	public AssemblerAdapter(Machine machine_in) {
		machine = machine_in;
	}

	public void assemble() {
		File source = null;
		File outputExe = null;
		JFileChooser chooser = new JFileChooser(machine.getDirectoryManager().getSourceDir());
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Pippin Source Files", "pasm");
		chooser.setFileFilter(filter);
		// CODE TO LOAD DESIRED FILE
		int openOK = chooser.showOpenDialog(machine.getFrame());
		if (openOK == JFileChooser.APPROVE_OPTION) {
			source = chooser.getSelectedFile();
		}
		if(source==null && (!source.exists())){
			JOptionPane.showMessageDialog(machine.getFrame(),"Cannot assemble the program","Warning",JOptionPane.ge
		}

	}

}
