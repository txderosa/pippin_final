package pippin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.swing.JOptionPane;

public class DirectoryManager {
	private Machine machine;
	private String pasmName;
	private String pexeDirectory;
	private String eclipse_default_dir;
	Properties properties;

	public DirectoryManager(Machine machine_in) {
		this.machine = machine_in;
		File temp = new File("propertyfile.txt");
		String eclipseFile = "";
		if (!temp.exists()) {
			PrintWriter out;
			try {
				out = new PrintWriter(temp);
				out.close();
				eclipseFile = temp.getAbsolutePath();
				temp.delete();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			eclipseFile = temp.getAbsolutePath();
		}
		eclipse_default_dir = extractDir(eclipseFile);
		// System.out.println(eclipseDir);--for debugging
		try { // load properties file "propertyfile.txt", if it exists
			properties = new Properties();
			File inFile = new File("propertyfile.txt");
			if (inFile.exists()) {
				FileInputStream in = new FileInputStream(inFile);
				properties.load(in);
				pasmName = properties.getProperty("SourceDirectory");
				pexeDirectory = properties.getProperty("ExecutableDirectory");
				in.close();
			}

			// CLEAN UP ANY ERRORS IN WHAT IS STORED:

			if (pasmName == null || pasmName.length() == 0
					|| !new File(pasmName).exists()) {
				pasmName = eclipse_default_dir;
			}

			if (pexeDirectory == null || pexeDirectory.length() == 0
					|| !new File(pexeDirectory).exists()) {
				pexeDirectory = eclipse_default_dir;
			}
		} catch (Exception e) {
			pasmName = eclipse_default_dir;
			pexeDirectory = eclipse_default_dir;
		}
		System.out.println(eclipse_default_dir);
		System.out.println(pasmName);
		System.out.println(pexeDirectory);

	}

	public String getPasmName() {
		return pasmName;
	}

	public String getPexeDirectory() {
		return pexeDirectory;
	}

	public String getEclipse_default_dir() {
		return eclipse_default_dir;
	}

	private static String extractDir(String fileName) {
		fileName.replace("\\", "/");
		int end = fileName.lastIndexOf("/");
		String returnName = fileName.substring(0, end);
		System.out.println(returnName);
		return returnName;
	}

	public void recordExecutableDir(String executableFile) {
		pexeDirectory = extractDir(executableFile);
		properties.setProperty("ExecutableDirectory", pexeDirectory);

	}

	public void recordSourceDir(String sourceFile) {
		pasmName = extractDir(sourceFile);
		properties.setProperty("Soruce Directory", pasmName);
	}

	public void closePropertiesFile() {
		try {
			FileOutputStream out = new FileOutputStream("propertyfile.txt");
			properties.store(out, "File locations");
			out.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(machine.getFrame(),
					"Problems when storing Source Directory in Property File",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

}
