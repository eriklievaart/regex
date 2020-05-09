package frame;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

public class FileChooser {

	public static String readFile(JFrame parent) {
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showOpenDialog(parent);
		if (result != JFileChooser.APPROVE_OPTION) {
			return "";
		}
		File file = chooser.getSelectedFile();
		System.out.println("Reading: " + file);

		return readFile(parent, file);
	}

	private static String readFile(JFrame parent, File file) {
		try {
			return FileUtils.readFileToString(file);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(parent, "Unable to read file: " + file);
			return "";
		}
	}

	public static void storeFile(JFrame parent, String text) {
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showSaveDialog(parent);
		if (result != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = chooser.getSelectedFile();
		System.out.println("Saving: " + file);
		saveFile(parent, file, text);
	}

	private static void saveFile(JFrame parent, File file, String text) {
		try {
			FileUtils.writeStringToFile(file, text);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(parent, "Unable to save file: " + file);
		}
	}
}
