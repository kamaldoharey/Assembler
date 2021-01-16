package assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class Assembler {
	public static HashMap<String, Integer> symbolTable = new HashMap<>();
	public static HashMap<String, Integer> opTable = new HashMap<>();
	//	Integers
	int locCounter = 0;
	int startAdd = 0;
	int lineCounter = 0;

	// Strings
	String label = null;

	public static void getOptable() {
		symbolTable = new HashMap<String, Integer>(10);
		opTable = new HashMap<String, Integer>(10);
		opTable.put("mul", 1010);
		opTable.put("jmp", 1065);
		opTable.put("add", 1124);
		opTable.put("cmp", 1224);
		opTable.put("sub", 1310);
		opTable.put("mvi", 1005);
	}

	public void passOne() {
		getOptable();

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("MyFile.txt"));
			String str = br.readLine();
			while (str != null) {
				lineCounter++;
				// System.out.println(str);
				String[] arr = str.split(" ");
				if (str.equals("END")) {
					break;
				}

				String opcode = arr[0];
				String operand = arr[1];

				if (opcode.equals("START")) {
					startAdd = Integer.parseInt(operand);
					locCounter = startAdd;
					str = br.readLine();
				} else {
					while (!str.equals("END")) {
						lineCounter++;
						arr = str.split(" ");
						// System.out.println(str);
						if (arr.length == 1 && (!arr[0].equals("END") || !str.startsWith("#"))) {
							System.out.println("Invaild Instruction");
						}
						if (arr[0].endsWith(":")) {
							label = arr[0];
							opcode = arr[1];
							operand = arr[2];

							if (label != null) {
								label = arr[0].substring(0, arr[0].length() - 1);
								if (symbolTable.containsKey(label)) {
									System.out.println("Duplicate Symbol" + label);
								} else {
									symbolTable.put(label, locCounter);
									locCounter += 3;
								}
							}
						} else {
							opcode = arr[0];
							operand = arr[1];

							if (!opcode.startsWith("#")) {
								if (opTable.containsKey(opcode)) {

									String s = opcode.toLowerCase();
									if (opTable.containsKey(s) || s.equals("WORD") || s.equals("RESW")) {
										locCounter += 3;
									} else if (s.equals("RESB") || s.equals("BYTE")) {
										locCounter += 1;
									}
								} else {
									System.out.println("Invaild Operation code:" + str);
								}
							}
						}
						str = br.readLine();
					}
				}
			}
			System.out.println("Pass 1 Completed Sucessfully....!! \n");
			br.close();
		} catch (Exception e) {
			System.out.println("invaild instruction");
			e.printStackTrace();
		}
	}

	public void passTwo() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("MyFile.txt"));
			FileWriter fw = new FileWriter("MyOutputFile.txt");
			String str = br.readLine();

			while (str != null) {
				String[] arr = str.split(" ");
				if (str.equals("END")) {
					fw.write("\nE " + (locCounter - startAdd));
					// System.out.println("\nE "+(locCounter-startAdd));
					break;
				}

				String opcode = arr[0];
				String operand = arr[1];

				if (opcode.equals("START")) {
					fw.write("H program1 \nT ");
					// System.out.println("H program1 ");
					// System.out.print("T ");
					str = br.readLine();
				} else {
					while (!str.equals("END")) {
						arr = str.split(" ");
						if (arr[0].endsWith(":")) {
							label = arr[0];
							opcode = arr[1];
							operand = arr[2];
							fw.write(opTable.get(opcode) + operand + " ");
							// System.out.print(opTable.get(opcode)+operand+" ");
						} else {
							opcode = arr[0];
							operand = arr[1];

							if (!opcode.startsWith("#")) {
								if (opTable.containsKey(opcode)) {
									if (symbolTable.containsKey(operand)) {
										fw.write(opTable.get(opcode) + "" + symbolTable.get(operand) + " ");
										// System.out.print(opTable.get(opcode)+""
										// +symbolTable.get(operand)+" ");
									} else {
										fw.write(opTable.get(opcode) + operand + " ");
										// System.out.print(opTable.get(opcode)+operand+" ");
									}
								}
							}
						}
						str = br.readLine();
					}
				}
			}
			System.out.println("Object File Created Sucessfully....!!");
			fw.close();
		} catch (Exception e) {
		}
	}
}
