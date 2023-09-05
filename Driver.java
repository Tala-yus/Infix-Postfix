package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
	static CursorStack CheckValidity = new CursorStack(20);
	static ArrayList<String> fileList = new ArrayList<>();
	static ArrayList<String> equationList = new ArrayList<>();

	static CursorStack undoStack = new CursorStack(20);

//	public static void main(String[] args) throws IOException {
//		System.out.println(FileReader("file1.242"));
//		System.out.println(fileList.toString());
//		System.out.println(equationList.toString());
//		System.out.println(SolveEquations(equationList));
//
//	}

	// for undo method when loading another file/subset file push the parent path to
	// the undoStack
	// and when the undo is pressed you get the previous path and run it in the
	// FileReader
	public static String FileReader(String path) throws FileNotFoundException {
		if (path.isEmpty()) {
			return "Error: Path is empty!";
		}
		String[] extension = path.split("\\.");
		if (!(extension[extension.length - 1].equals("242"))) {
			return "Error: Wrong file extension";
		}
		fileList = new ArrayList<>();
		equationList = new ArrayList<>();
		File myfile = new File(path);
		Scanner scanner = new Scanner(myfile);
		while (scanner.hasNext()) {
			String line = scanner.nextLine().trim();
			String delimiter = "";
			String data = "";
			String prev = "";
			for (int i = 0; i < line.length(); i++) {
				char ch = line.charAt(i);
				switch (ch) {
				case '<':
					if (!delimiter.isEmpty()) {
						return "Error: a Delimiter doesnt have a closing '>'";
					} else {
						if (!data.isEmpty()) {
							switch (prev) {
							case "<file>":
								fileList.add(data);
								data = "";
								break;
							case "<equation>":
								equationList.add(data);
								data = "";
								break;
							default:
								return "Error: Delimiter " + prev + " isn't recognized as data input or does not exist";
							}
						}
						delimiter += ch;
					}
					break;
				case '>':
					delimiter += ch;
					switch (delimiter) {
					case "</242>":
					case "</files>":
					case "</file>":
					case "</equation>":
					case "</equations>":
						if (!CheckValidity.isEmpty()) {
							String StackContent = String.valueOf(CheckValidity.pop().getElement());
							if (delimiter.equals("</242>") && !(StackContent.equals("<242>"))
									|| delimiter.equals("</files>") && !(StackContent.equals("<files>"))
									|| delimiter.equals("</equations>") && !(StackContent.equals("<equations>")
											|| delimiter.equals("</file>") && !(StackContent.equals("<file>"))
											|| delimiter.equals("</equation>")
													&& !(StackContent.equals("<equation>")))) {
								return "Error: File format doesn't match";
							}
							break;
						}
					case "<242>":
					case "<files>":
					case "<file>":
					case "<equation>":
					case "<equations>":
						CheckValidity.push(delimiter);
						break;
					default:
						return "Error: Delimiter " + delimiter + " isn't recognized";
					}
					prev = delimiter;
					delimiter = "";
					break;
				default:
					if (delimiter.isEmpty()) {
						data += ch;
					} else {
						delimiter += ch;
					}
					break;
				}
			}
		}
		if (!CheckValidity.isEmpty()) {
			return "Error: File format doesn't match";
		}
		return "Success";
	}

	public static String undo() {
		if (!undoStack.isEmpty()) {
			return undoStack.pop().getElement();
		}
		return "Error: Nothing to undo";
	}


	public static int getThePrecedence(String c) {
		if (c.equals("^"))
			return 3;
		if (c.equals("*") || c.equals("/"))
			return 2;
		if (c.equals("-") || c.equals("+"))
			return 1;
		return 0;
	}

	private static boolean isOperator(String c) {
		switch (c) {
		case "^":
		case "*":
		case "/":
		case "-":
		case "+":
			return true;
		default:
			return false;
		}
	}

	public static String SolveEquations(ArrayList<String> equationList) {
		String output = "";
		for (int i = 0; i < equationList.size(); i++) {
			String eq = equationList.get(i);
			output += eq;
			if (!checkParenthesis(eq)) {
				output += " => " + "Unbalanced parenthesis";
			} else if (!infixValidation(eq)) {
				output += " => " + "Invalid equation";
			} else {
				output += " => " + infixToPostfix(eq);
				output += " => " + evaluatePostfix(infixToPostfix(eq));
			}
			output += "\n";
		}
		return output;
	}

	public static boolean checkParenthesis(String eq) {
		CursorStack temp = new CursorStack(20);
		for (int i = 0; i < eq.length(); i++) {
			String c = String.valueOf(eq.charAt(i));
			switch (c) {
			case "(":
			case ")":
			case "{":
			case "}":
			case "[":
			case "]":
				if (temp.isEmpty()) {
					temp.push(c);
				} else if ((temp.peek().equals("(") && c.equals(")")) || (temp.peek().equals("{") && c.equals("}"))
						|| (temp.peek().equals("[") && c.equals("]"))) {
					temp.pop();
				} else
					temp.push(c);
				break;
			default:
				break;
			}
		}
		if (temp.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean infixValidation(String eq) {
		boolean result = true;
		CursorStack operands = new CursorStack(20);
		CursorStack operators = new CursorStack(20);
		String digit = "";
		for (int i = 0; i < eq.length(); i++) {
			char c = eq.charAt(i);
			if (c == ' ') {
				continue;
			} else if (Character.isDigit(c)) {
				digit += c;
			} else if (isOperator(String.valueOf(c))) {
				operators.push(String.valueOf(c));
				if (!digit.isEmpty()) {
					operands.push(digit);
					digit = "";
				}
			} else if (c == '(') {
				operators.push(String.valueOf(c));
			} else {
				if (!digit.isEmpty()) {
					operands.push(digit);
					digit = "";
				}
				boolean flag = true;
				while (!operators.isEmpty()) {
					String d = operators.pop().getElement();
					if (d.equals("(")) {
						flag = false;
						break;
					} else {
						if (operands.getStackSize() < 2) {
							return false;
						} else {
							operands.pop();
						}
					}
				}
				if (flag) {
					return false;
				}
			}
		}
		if (!digit.isEmpty()) {
			operands.push(digit);
		}
		while (!operators.isEmpty()) {
			String c = operators.pop().getElement();
			if (!isOperator(c)) {
				return false;
			}
			if (operands.getStackSize() < 2) {
				return false;
			} else {
				operands.pop();
			}
		}
		if (operands.getStackSize() > 1 || !operators.isEmpty()) {
			return false;
		}
		return result;
	}

	public static String infixToPostfix(String eq) {
		String digit = "";
		String output = "";
		CursorStack temp = new CursorStack(20);
		for (int i = 0; i < eq.length(); ++i) {
			char c = eq.charAt(i);
			if (c != ' ') {
				if (!isOperator(String.valueOf(c)) && c != '(' && c != ')') {
					output += c;
				} else if (c == '(') {
					temp.push(String.valueOf(c));
				} else if (c == ')') {
					while (!temp.isEmpty() && !temp.peek().equals("(")) {
						output += " " + temp.pop().getElement();
					}

					if (!temp.isEmpty() && !temp.peek().equals("(")) {
						return "Error: invalid equation";
					} else
						temp.pop();
				} else {
					output += " ";
					while (!temp.isEmpty() && getThePrecedence(String.valueOf(c)) <= getThePrecedence(temp.peek())) {
						output += temp.pop().getElement() + " ";
					}
					temp.push(String.valueOf(c));
				}
			}
		}
		while (!temp.isEmpty()) {
			output += " " + temp.pop().getElement();
		}
		return output;
	}

	public static String evaluatePostfix(String eq) {
		CursorStack operands = new CursorStack(20);
		String[] tokens = eq.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			if (!isOperator(tokens[i])) {
				operands.push(tokens[i]);
			} else {
				Double val1 = Double.parseDouble(operands.pop().getElement());
				Double val2 = Double.parseDouble(operands.pop().getElement());
				switch (tokens[i]) {
				case "+":
					operands.push(String.valueOf(val2 + val1));
					break;
				case "-":
					operands.push(String.valueOf(val2 - val1));
					break;
				case "*":
					operands.push(String.valueOf(val2 * val1));
					break;
				case "/":
					operands.push(String.valueOf(val2 / val1));
					break;
				}
			}
		}
		return operands.pop().getElement();
	}
}
