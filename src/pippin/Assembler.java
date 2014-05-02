package pippin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Assembler  {
	static Set<String> allowsIndirect = new HashSet<String>();
	static Set<String> noArgument = new HashSet<String>();
	static Map<String, Integer> opcode = new HashMap<String, Integer>();
	static {
		allowsIndirect.add("ADD");
		allowsIndirect.add("AND");
		allowsIndirect.add("DIV");
		allowsIndirect.add("JUMPZ");
		allowsIndirect.add("JUMP");
		allowsIndirect.add("MUL");
		allowsIndirect.add("LOD");
		allowsIndirect.add("MUL");
		allowsIndirect.add("STO");
		allowsIndirect.add("SUB");
		noArgument.add("HALT");
		noArgument.add("NOOP");
		noArgument.add("NOT");
		opcode.put("NOP", 0x00);
		opcode.put("LOD", 0x01);
		opcode.put("LODI", 0x02);
		opcode.put("STO", 0x03);
		opcode.put("ADD", 0x04);
		opcode.put("SUB", 0x05);
		opcode.put("MUL", 0x06);
		opcode.put("DIV", 0x07);
		opcode.put("ADDI", 0x08);
		opcode.put("SUBI", 0x09);
		opcode.put("MULI", 0x0A);
		opcode.put("DIVI", 0x0B);
		opcode.put("AND", 0x10);
		opcode.put("ANDI", 0x11);
		opcode.put("NOT", 0x12);
		opcode.put("CMPZ", 0x13);
		opcode.put("CMPL", 0x14);
		opcode.put("JUMP", 0x1A);
		opcode.put("JMPZ", 0x1B);
		opcode.put("HALT", 0x1F);
	}

	public String assemble(File input, File output) {
		// note that input and output are the parameters to this method
		boolean goodProgram = false; // will be used often and at the end of
										// method String message =
										// "goodProgram";
		String message = "goodProgram";
		try {
			goodProgram = true;
			Scanner inp = new Scanner(input);
			// make a PrintWriter outp to write to the file output
			PrintWriter outp = new PrintWriter(output);
			// make a boolean blankLineHit, initially false to keep track of the
			// first time\
			// we hit a blank line
			boolean blankLineHit = false;
			// make a boolean inCode initially true to keep track that we are in
			// code, not data
			boolean inCode = true;
			// make an int lineCounter, initially 0.
			int lineCounter = 0;
			while (inp.hasNextLine() && goodProgram) {
				// add 1 to lineCounter, get the String rawInput using
				// inp.nextLine()
				lineCounter += 1;
				String rawInput = inp.nextLine();
				// make the String line and set it equal to rawInput.trim(),
				// which removes
				// any white space at the front and end of rawInput
				String line = rawInput.trim();
				// check for a blank line in the code, else if the current line
				// is blank,
				// set blankLineHit to true, else check if the first character
				// on the line is // white-space,
				// else do the rest of the processing
				if (blankLineHit && line.length() > 0) {
					goodProgram = false;
					message = "There is a blank line in the program on line "
							+ (lineCounter - 1);
				} else if (line.length() == 0) {
					blankLineHit = true;
				} else if (Character.isWhitespace(rawInput.charAt(0))) {
					goodProgram = false;
					message = "Illegal line starts with White Space on line "
							+ (lineCounter - 1);
				} else {
					if (!line.startsWith("DATA")) {
						if (!inCode) {
							goodProgram = false;
							message = "There is more code after data began on line "
									+ (lineCounter);
						} else {
							String[] parts = line.trim().split("\\s+");
							if (parts.length == 1) {
								if (noArgument.contains(parts[0])) {
									long temp = 2 * opcode.get(parts[0]);
									temp = temp << 32;
									// ********temp = -temp;
									outp.println(temp);
								} else {
									goodProgram = false;
									message = "Illegal mnemonic or missing argument on line"
											+ lineCounter;
								}
							}
							if (parts.length == 2) {
								Boolean indirect = false;
								int arg = 0;
								String mnemonic = parts[0];
								if (mnemonic.endsWith("#")) {
									indirect = true;
									mnemonic = mnemonic.substring(0,
											mnemonic.length() - 1);
									if (!opcode.containsKey(mnemonic)) {
										goodProgram = false;
										message = "Illegal mnemonic on line"
												+ lineCounter;
									} else if (indirect
											&& !allowsIndirect
													.contains(mnemonic)) {
										goodProgram = false;
										message = "Instruction cannot be indirect on line"
												+ lineCounter;
									}
								}
								if (goodProgram) {
									try {
										arg = Integer.parseInt(parts[1], 16);
									} catch (NumberFormatException e) {
										goodProgram = false;
										message = "Argument not a hexadecimal number on line "
												+ lineCounter;
									}
								}
								if (goodProgram) {
									int opc = 2 * opcode.get(mnemonic);
									if (indirect) {
										opc++;
									}
									long otemp = opc;
									long atemp = arg;
									otemp = otemp << 32;
									atemp = atemp & 0x00000000FFFFFFFFL;
									otemp = otemp | atemp;
									// ********otemp = -otemp;
									outp.println(otemp);
								}
								// }
							}
							if (parts.length > 2) {
								goodProgram = false;
								message = "Too many arguments on line "
										+ lineCounter;
							}
						}
					} else {
						if (goodProgram) {
							if(inCode)
							{
								outp.println(-1L); //Output a separator
								inCode=false;
							}
							
							//inCode = false;
							String[] parts = line.trim().split("\\s+");
							if (parts.length == 1) {
								goodProgram = false;
								message = "No data on line " + lineCounter;
							} else if (parts.length == 2) {
								goodProgram = false;
								message = "No data on line " + lineCounter;
							} else if (parts.length > 3) {
								goodProgram = false;
								message = "Too many data values on line "
										+ lineCounter;
							} else {
								int addr = 0;
								int val = 0;
								try {
									addr = Integer.parseInt(parts[1], 16);
									val = Integer.parseInt(parts[2], 16);
								} catch (NumberFormatException e) {
									goodProgram = false;
									message = "Address or value not a hexidecimal number on line"
											+ lineCounter;
								}
								if (goodProgram) {
									long atemp = addr;
									// System.out.println(atemp);
									atemp = atemp << 32;
									// System.out.println(atemp);
									long vtemp = val;
									vtemp = vtemp & 0x00000000FFFFFFFFL;
									// System.out.println(vtemp);
									atemp = atemp | vtemp;
									// System.out.println(atemp);
									outp.println(atemp);
								}
							}
						}
					}
				}
				// }
			}
			inp.close();
			outp.close();
		} catch (IOException e) {
			message = "Unable to open the necessary files";
		}
		if (!goodProgram && output != null && output.exists()) {
			output.delete();
		}
		return message;
	}

	public static void main(String[] args) {
		AssemblerInterface ass = new Assembler();
		String fileName = "factorial8";
		System.out.println(ass.assemble(new File(fileName + ".pasm"), new File(
				fileName + ".pexe")));
	}
}
