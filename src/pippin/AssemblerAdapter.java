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
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pippin Source Files", "pasm");
		chooser.setFileFilter(filter);
		// CODE TO LOAD DESIRED FILE
		int openOK = chooser.showOpenDialog(machine.getFrame());
		if (openOK == JFileChooser.APPROVE_OPTION) {
			source = chooser.getSelectedFile();
		}
		if(source==null && (!source.exists())){
			JOptionPane.showMessageDialog(machine.getFrame(),"The source file was not selected or has problems.\n" + 
		"Cannot assemble the progrram.","Warning",JOptionPane.WARNING_MESSAGE);
		}
		else {
			String sourceFile = source.getAbsolutePath();
			String outName = source.getName();
			outName = outName.replace("pasm", "pexe");
			machine.getDirectoryManager().recordSourceDir(sourceFile);
			filter = new FileNameExtensionFilter("Pippin Executable Files", "pexe");
			if(machine.getDirectoryManager().getExecutableDir() == null ||
					machine.getDirectoryManager().getExecutableDir().equals(
							machine.getDirectoryManager().getEclipseDir())) {
				outName =  machine.getDirectoryManager().getSourceDir()+"/"+outName;	
			}
			else{
				outName = machine.getDirectoryManager().getExecutableDir()+"/"+outName;
			}
			chooser = new JFileChooser();
			chooser.setFileFilter(filter);
			chooser.setSelectedFile(new File(outName));
			int saveOK = chooser.showSaveDialog(machine.getFrame());
			// GET THE OUTPUT FILE EVEN IF THE USER CHANGED TO ANOTHER DIRECTORY 
			if(saveOK == JFileChooser.APPROVE_OPTION) {
				outputExe = chooser.getSelectedFile();
			}
			if(outputExe == null){
				JOptionPane.showMessageDialog(machine.getFrame(),
						"The output file was not selected or could not be created.\n" + 
						"Cannot assemble the progrram.","Warning",JOptionPane.WARNING_MESSAGE);
			}
			else {
				String executableFile = outputExe.getAbsolutePath();
				machine.getDirectoryManager().recordExecutableDir(executableFile);
				String response = assembler.assemble(source, outputExe); 
				if(response.equals("goodProgram")){
					JOptionPane.showMessageDialog(machine.getFrame(),"The source was assembled to an executable", 
							"Success",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(machine.getFrame(),
							"The selected file has problems. The assembler error is:\n" + response,"Warning",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	public static void main(String[] args) {
        Machine m = new Machine();
        AssemblerAdapter ad = new AssemblerAdapter(m);
        ad.assemble();
	}

}
