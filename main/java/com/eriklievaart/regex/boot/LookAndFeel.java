package com.eriklievaart.regex.boot;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class LookAndFeel {

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
		Color dark = new Color(64, 64, 64);
		Color text = new Color(236, 236, 236);
		Color caret = new Color(255, 255, 0);
		Color input = new Color(48, 71, 94);

		defaults.put("List.background", dark);
		defaults.put("List.foreground", text);
		defaults.put("TextField.background", input);
		defaults.put("TextField.foreground", text);
		defaults.put("TextField.caretForeground", caret);
		defaults.put("Label.background", dark);
		defaults.put("Label.foreground", text);
		defaults.put("Panel.background", dark);
		defaults.put("Panel.foreground", text);
		defaults.put("Button.background", Color.gray);
	}
}
