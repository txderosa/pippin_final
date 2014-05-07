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
	private String sourceDir;
	private String executableDir;
	private String eclipseDir;
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
		eclipseDir = extractDir(eclipseFile);
		// System.out.println(eclipseDir);--for debugging
		try { // load properties file "propertyfile.txt", if it exists
			properties = new Properties();
			File inFile = new File("propertyfile.txt");
			if (inFile.exists()) {
				FileInputStream in = new FileInputStream(inFile);
				properties.load(in);
				sourceDir = properties.getProperty("SourceDirectory");
				executableDir = properties.getProperty("ExecutableDirectory");
				in.close();
			}

			// CLEAN UP ANY ERRORS IN WHAT IS STORED:

			if (sourceDir == null || sourceDir.length() == 0
					|| !new File(sourceDir).exists()) {
				sourceDir = eclipseDir;
			}

			if (executableDir == null || executableDir.length() == 0
					|| !new File(executableDir).exists()) {
				executableDir = eclipseDir;
			}
		} catch (Exception e) {
			sourceDir = eclipseDir;
			executableDir = eclipseDir;
		}
		System.out.println(eclipseDir);
		System.out.println(sourceDir);
		System.out.println(executableDir);

	}

	public String getSourceDir() {
		return sourceDir;
	}

	public String getExecutableDir() {
		return executableDir;
	}

	public String getEclipseDir() {
		return eclipseDir;
	}

	private static String extractDir(String fileName) {
		fileName.replace("\\", "/");
		int end = fileName.lastIndexOf("/");
		String returnName = fileName.substring(0, end);
		System.out.println(returnName);
		return returnName;
	}

	public void recordExecutableDir(String executableFile) {
		executableDir = extractDir(executableFile);
		properties.setProperty("ExecutableDirectory", executableDir);

	}

	public void recordSourceDir(String sourceFile) {
		sourceDir = extractDir(sourceFile);
		properties.setProperty("Soruce Directory", sourceDir);
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
