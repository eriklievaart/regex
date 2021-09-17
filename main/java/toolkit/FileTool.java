package toolkit;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileTool {

	public static List<String> readLines(File file) {
		try {
			try (InputStream is = new FileInputStream(file)) {
				return readLines(is);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> readLines(InputStream is) {
		try {
			List<String> lines = new ArrayList<>();

			try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
				String line = null;
				while ((line = in.readLine()) != null) {
					lines.add(line);
				}
			}
			return lines;

		} catch (IOException e) {
			throw new RuntimeException(e);

		} finally {
			close(is);
		}
	}

	public static void writeStringToFile(String data, File file) {
		try {
			file.getParentFile().mkdirs();
			try (PrintWriter writer = new PrintWriter(new FileOutputStream(file))) {
				writer.write(data);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeString(String data, OutputStream os) {
		try (PrintWriter writer = new PrintWriter(os)) {
			writer.write(data);
		}
	}

	public static void close(Closeable value) {
		try {
			if (value != null) {
				value.close();
			}
		} catch (IOException ignore) {
		}
	}
}
