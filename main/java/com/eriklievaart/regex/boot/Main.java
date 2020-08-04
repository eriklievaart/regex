package com.eriklievaart.regex.boot;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import toolkit.WindowSaver;

public class Main {
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		WindowSaver.initialize();
		SwingUtilities.invokeAndWait(() -> {
			LookAndFeel.init();
			new UI().init();
		});
	}
}
