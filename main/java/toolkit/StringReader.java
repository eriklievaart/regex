package toolkit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class StringReader {
	protected BufferedReader in;

	public StringReader(String file) {
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			System.out.println("Error StringReader.StringReader(): Could not find " + file);
		}
	}

	public String readln() {
		String raw = "";

		try {
			raw = in.readLine();
		} catch (Exception e) {
			return null;
		}
		return raw;
	}

	public String[] readlns() {
		if (in == null) {
			return null;
		}

		Vector<String> out = new Vector<String>();
		String raw = "";

		while (raw != null) {
			try {
				raw = in.readLine();
				if (raw != null) {
					out.add(raw);
				}
			} catch (IOException e) {
				raw = null;
			}
		}
		return out.toArray(new String[] {});
	}

}
