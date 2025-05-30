package Client.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * The Keyboard class provides a simple way to read data from the keyboard.
 */
public class Keyboard {
	// ************* Error Handling Section **************************
	private static boolean printErrors = true;
	private static int errorCount = 0;

	/**
	 * Returns the current error count.
	 * @return the current error count.
	 **/
	public static int getErrorCount() {
		return errorCount;
	}

	/**
	 * resetErrorCount method
	 * resets the current error count to zero.
	 */
	// -----------------------------------------------------------------
	// Resets the current error count to zero.
	// -----------------------------------------------------------------
	public static void resetErrorCount() {
		errorCount = 0;
	}

	/**
	 * Returns a boolean indicating whether input errors are
	 * currently printed to standard output.
	 * @return a boolean indicating whether input errors are currently printed to standard output.
	 **/
	public static boolean getPrintErrors() {
		return printErrors;
	}

	/**
	 * setPrintErrors method
	 * sets a boolean indicating whether input errors are to be printed to standard output.
	 * @param flag a boolean indicating whether input errors are to be printed to standard output.
	 */
	public static void setPrintErrors(boolean flag) {
		printErrors = flag;
	}

	// -----------------------------------------------------------------
	// Increments the error count and prints the error message if
	// appropriate.
	// -----------------------------------------------------------------
	private static void error(String str) {
		errorCount++;
		if (printErrors)
			System.out.println(str);
	}

	// ************* Tokenized Input Stream Section ******************
	private static String current_token = null;

	private static StringTokenizer reader;

	private static BufferedReader in = new BufferedReader(
			new InputStreamReader(System.in));

	// -----------------------------------------------------------------
	// Gets the next input token assuming it may be on subsequent
	// input lines.
	// -----------------------------------------------------------------
	private static String getNextToken() {
		return getNextToken(true);
	}

	// -----------------------------------------------------------------
	// Gets the next input token, which may already have been read.
	// -----------------------------------------------------------------
	private static String getNextToken(boolean skip) {
		String token;

		if (current_token == null)
			token = getNextInputToken(skip);
		else {
			token = current_token;
			current_token = null;
		}

		return token;
	}

	// -----------------------------------------------------------------
	// Gets the next token from the input, which may come from the
	// current input line or a subsequent one. The parameter
	// determines if subsequent lines are used.
	// -----------------------------------------------------------------
	private static String getNextInputToken(boolean skip) {
		final String delimiters = " \t\n\r\f";
		String token = null;

		try {
			if (reader == null)
				reader = new StringTokenizer(in.readLine(), delimiters, true);

			while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
				while (!reader.hasMoreTokens())
					reader = new StringTokenizer(in.readLine(), delimiters,
							true);

				token = reader.nextToken();
			}
		} catch (Exception exception) {
			token = null;
		}

		return token;
	}


	// -----------------------------------------------------------------
	// Returns true if there are no more tokens to read on the
	// current input line.
	// -----------------------------------------------------------------
	/**
	 * endOfLine method
	 * @return true if there are no more tokens to read on the current input line.
	 */
	public static boolean endOfLine() {
		return !reader.hasMoreTokens();
	}

	// ************* Reading Section *********************************

	/**
	 * readString method
	 * @return a string read from standard input.
	 */
	public static String readString() {
		String str;

		try {
			str = getNextToken(false);
			while (!endOfLine()) {
				str = str + getNextToken(false);
			}
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			str = null;
		}
		return str;
	}

	/**
	 * readWord method
	 * @return a space-delimited substring (a word) read from standard input.
	 */
	public static String readWord() {
		String token;
		try {
			token = getNextToken();
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			token = null;
		}
		return token;
	}

	/**
	 * readBoolean method
	 * @return a boolean read from standard input.
	 */
	public static boolean readBoolean() {
		String token = getNextToken();
		boolean bool;
		try {
			if (token.toLowerCase().equals("true"))
				bool = true;
			else if (token.toLowerCase().equals("false"))
				bool = false;
			else {
				error("Error reading boolean data, false value returned.");
				bool = false;
			}
		} catch (Exception exception) {
			error("Error reading boolean data, false value returned.");
			bool = false;
		}
		return bool;
	}

	/**
	 * readChar method
	 * @return a char read from standard input.
	 */
	public static char readChar() {
		String token = getNextToken(false);
		char value;
		try {
			if (token.length() > 1) {
				current_token = token.substring(1, token.length());
			} else
				current_token = null;
			value = token.charAt(0);
		} catch (Exception exception) {
			error("Error reading char data, MIN_VALUE value returned.");
			value = Character.MIN_VALUE;
		}

		return value;
	}

	/**
	 * readInt method
	 * @return an int read from standard input.
	 */
	public static int readInt() {
		String token = getNextToken();
		int value;
		try {
			value = Integer.parseInt(token);
		} catch (Exception exception) {
			error("Error reading int data, MIN_VALUE value returned.");
			value = Integer.MIN_VALUE;
		}
		return value;
	}

	/**
	 * readLong method
	 * @return a long read from standard input.
	 */
	public static long readLong() {
		String token = getNextToken();
		long value;
		try {
			value = Long.parseLong(token);
		} catch (Exception exception) {
			error("Error reading long data, MIN_VALUE value returned.");
			value = Long.MIN_VALUE;
		}
		return value;
	}

	/**
	 * readShort method
	 * @return a short read from standard input.
	 */
	public static float readFloat() {
		String token = getNextToken();
		float value;
		try {
			//value = (new Float(token)).floatValue();
			value = Float.parseFloat(token);
		} catch (Exception exception) {
			error("Error reading float data, NaN value returned.");
			value = Float.NaN;
		}
		return value;
	}

	/**
	 * readDouble method
	 * @return a double read from standard input.
	 */
	public static double readDouble() {
		String token = getNextToken();
		double value;
		try {
			//value = (new Double(token)).doubleValue();
			value = Double.parseDouble(token);
		} catch (Exception exception) {
			error("Error reading double data, NaN value returned.");
			value = Double.NaN;
		}
		return value;
	}
}
