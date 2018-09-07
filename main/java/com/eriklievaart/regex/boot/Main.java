package com.eriklievaart.regex.boot;

import toolkit.WindowSaver;

public class Main {
	public static void main(String[] args) {
		//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandlerFrame());
		WindowSaver.initialize();
		new UI().init();
	}
}
