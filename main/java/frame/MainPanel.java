package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import regex.RegexQueue;
import toolkit.PropertiesIO;

public class MainPanel extends JPanel {
	JTextField regex = new JTextField();
	JTextField replace = new JTextField();
	JTextField escaped = new JTextField();
	JTextField input = new JTextField();
	JTextField output = new JTextField();
	JButton queueButton = new JButton("Queue Regex");
	JButton goButton = new JButton("Go");
	RegexQueue queue;
	JList<String> groupList = new JList<>();

	/**
	 * constructor
	 */
	public MainPanel(RegexQueue queue) {
		this.queue = queue;

		setLayout(new GridLayout(0, 1, 0, 0));
		fillTextFields();
		addComponents();
	}

	private void addComponents() {
		regex.setFont(new Font("lucida console", Font.PLAIN, 16));
		regex.getDocument().addDocumentListener(new TextFieldListener());

		addWithLabel("Input", input);
		addWithLabel("Regex", regex);
		addWithLabel("as String:", escaped);
		addWithLabel("Replace", replace);
		addWithLabel("Output", output);
		add(queueButton);
		add(goButton);

		queueButton.addActionListener(new QueueListener());
		goButton.addActionListener(new GoListener());
	}

	private void addWithLabel(String label, JComponent component) {
		add(new JLabel(label));
		add(component);
	}

	private void fillTextFields() {
		input.setText(PropertiesIO.getStringOrFallback("input", "some text"));
		regex.setText(PropertiesIO.getStringOrFallback("regex", ".*+"));
		replace.setText(PropertiesIO.getStringOrFallback("replace", "some replacement"));
	}

	public JButton getDefaultButton() {
		return goButton;
	}

	public boolean matchingBackSlashes(String in) {
		return in.matches("[^\\\\]*+(?:(?:\\\\\\\\)(?:\\\\\\\\)*+([^\\\\]++|$))*+");
	}

	public String escape(String in) {
		return in.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
	}

	public JList<String> getGroups() {
		return groupList;
	}

	private class GoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Matcher matcher = Pattern.compile(regex.getText()).matcher(input.getText());
			if (matcher.matches()) {
				output.setText("Success");
				showGroups(matcher);
			} else {
				output.setText("Failure");
				groupList.setListData(new String[0]);
			}
			Map<String, String> settings = new Hashtable<>();
			settings.put("input", input.getText());
			settings.put("regex", regex.getText());
			settings.put("replace", replace.getText());
			PropertiesIO.store(settings);
		}

		private void showGroups(Matcher matcher) {
			String[] groups = new String[matcher.groupCount() + 1];
			for (int g = 0; g < groups.length; g++) {
				groups[g] = g + ") " + matcher.group(g);
			}
			groupList.setListData(groups);
			if (!replace.getText().equals("")) {
				String replaced = input.getText().replaceAll(regex.getText(), replace.getText());
				if (!input.getText().equals(replaced)) {
					output.setText(replaced);
				}
			}
		}
	}

	private class QueueListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			queue.add(regex.getText(), replace.getText());
		}
	}

	public class TextFieldListener implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent e) {
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			update();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			update();
		}

		private void update() {
			try {
				Pattern.compile(regex.getText());
				escaped.setText(escape(regex.getText()));
				regex.setBackground(Color.green);
				regex.setForeground(Color.black);
			} catch (IllegalArgumentException iae) {
				regex.setBackground(Color.orange);
				regex.setForeground(Color.black);
			}
		}
	}
}
