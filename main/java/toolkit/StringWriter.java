package toolkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class StringWriter {
	/** This writer will perform the actual IO */
	private PrintWriter pout;

	public StringWriter(String file) {
		this(new File(file));
	}

	public StringWriter(File file) {
		try {
			pout = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		} catch (Exception e) {
			// only for testing purposes
			System.out.println(e.toString());
		}
	}

	/** Write a String */
	public synchronized void println(String arg) {
		if (pout != null) {
			pout.println(arg);
			pout.flush();
		}
	}

	/** Write multiple Strings */
	public synchronized void printlns(String... arg) {
		if (pout != null) {
			for (String str : arg) {
				pout.println(str);
			}
			pout.flush();
		}
	}

	public synchronized void close() {
		pout.close();
	}
}
