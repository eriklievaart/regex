package toolkit;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;

public class WindowSaver implements AWTEventListener {
	private static final String FILE_NAME = "WindowSaver.ini";
	private static WindowSaver saver;
	private Map<String, JFrame> framemap = new HashMap<String, JFrame>();

	public static WindowSaver getInstance() {
		if (saver == null) {
			saver = new WindowSaver();
		}
		return saver;
	}

	public static void initialize() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		tk.addAWTEventListener(getInstance(), AWTEvent.WINDOW_EVENT_MASK);
	}

	@Override
	public void eventDispatched(AWTEvent evt) {
		if (evt.getID() == WindowEvent.WINDOW_OPENED) {
			ComponentEvent cev = (ComponentEvent) evt;
			if (cev.getComponent() instanceof JFrame) {
				loadSettings((JFrame) cev.getComponent());
			}
		}
		if (evt.getID() == WindowEvent.WINDOW_CLOSED) {
			saveSettings();
		}
		if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
			saveSettings();
		}
	}

	public static void loadSettings(JFrame frame) {
		Properties settings = loadProperties();
		String name = frame.getName();
		if (name.matches("frame\\d")) {
			System.out.println("Please call setName(String) on all JFrames");
		}
		int x = getInt(settings, name + ".x", 100);
		int y = getInt(settings, name + ".y", 100);
		int w = getInt(settings, name + ".w", 440);
		int h = getInt(settings, name + ".h", 440);
		frame.setBounds(x, y, w, h);
		saver.framemap.put(name, frame);
		frame.validate();
	}

	public static int getInt(Properties props, String name, int def) {
		String v = props.getProperty(name);
		if (v == null) {
			return def;
		}
		return Integer.parseInt(v);
	}

	public static void saveSettings() {
		Properties settings = loadProperties();
		Iterator<String> it = saver.framemap.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (name.matches("frame\\d")) {
				continue;
			}
			JFrame frame = saver.framemap.get(name);
			settings.setProperty(name + ".x", "" + frame.getX());
			settings.setProperty(name + ".y", "" + frame.getY());
			settings.setProperty(name + ".w", "" + frame.getWidth());
			settings.setProperty(name + ".h", "" + frame.getHeight());
		}
		storeProperties(settings);
	}

	private static void storeProperties(Properties settings) {
		try {
			settings.store(new FileOutputStream(FILE_NAME), null);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private static Properties loadProperties() {
		Properties settings = new Properties();
		try {
			settings.load(new FileInputStream(FILE_NAME));
		} catch (IOException ioe) {
		}
		return settings;
	}
}
