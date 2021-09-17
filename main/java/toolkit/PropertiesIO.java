package toolkit;

import java.io.File;
import java.util.Map;

public class PropertiesIO {

	private static final File FILE = new File(System.getProperty("user.home"), "Applications/regex/data/data.ini");

	public static String getStringOrFallback(String key, String fallback) {
		if (FILE.isDirectory()) {
			throw new RuntimeException("expecting directory " + FILE + " to be a file");
		}
		if (!FILE.exists()) {
			return fallback;
		}
		checkKey(key);
		for (String line : FileTool.readLines(FILE)) {
			if (line.startsWith(key + "=")) {
				return line.replaceFirst("^[^=]++=", "");
			}
		}
		return fallback;
	}

	public static void store(Map<String, String> settings) {
		StringBuilder builder = new StringBuilder();
		for (String key : settings.keySet()) {
			checkKey(key);
		}
		settings.forEach((k, v) -> builder.append(k).append("=").append(v).append("\n"));
		FILE.getParentFile().mkdirs();
		FileTool.writeStringToFile(builder.toString(), FILE);
	}

	private static void checkKey(String key) {
		String regex = "\\w++";
		if (key == null || !key.matches(regex)) {
			throw new IllegalArgumentException("key [" + key + "] does not match regex: " + regex);
		}
	}
}
