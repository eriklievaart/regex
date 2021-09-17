package com.eriklievaart.regex.boot;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class LookAndFeel {

	private static final Color DARK = new Color(64, 64, 64);
	private static final Color TEXT = new Color(236, 236, 236);
	private static final Color CARET = new Color(255, 255, 0);
	private static final Color INPUT = new Color(48, 71, 94);
	private static final Color BUTTON = Color.gray;

	public static void init() {
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		setupColors(defaults);
		setupFonts(defaults);
	}

	private static void setupFonts(UIDefaults defaults) {
		Font font = Font.decode("ubuntu-18");
		defaults.put("Button.font", font);
		defaults.put("TextField.font", font);
		defaults.put("Label.font", font);
	}

	private static void setupColors(UIDefaults defaults) {
		defaults.put("List.background", DARK);
		defaults.put("List.foreground", TEXT);
		defaults.put("TextField.background", INPUT);
		defaults.put("TextField.foreground", TEXT);
		defaults.put("TextField.caretForeground", CARET);
		defaults.put("Label.background", DARK);
		defaults.put("Label.foreground", TEXT);
		defaults.put("Panel.background", DARK);
		defaults.put("Panel.foreground", TEXT);
		defaults.put("Button.background", BUTTON);
	}
}
