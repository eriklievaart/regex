package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import regex.RegexQueue;
import toolkit.ConfigurationManager;
import toolkit.Option;
import toolkit.OptionAction;

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
		registerOptions();
		addComponents();
	}

	private void addComponents() {
		add(new JLabel("Input"));
		add(input);
		add(new JLabel("Regex"));
		add(regex);
		regex.setFont(new Font("lucida console", Font.PLAIN, 16));
		regex.getDocument().addDocumentListener(new TextFieldListener());
		add(new JLabel("as String:"));
		add(escaped);
		add(new JLabel("Replace"));
		add(replace);
		add(new JLabel("Output"));
		add(output);
		add(queueButton);
		add(goButton);
		queueButton.addActionListener(new QueueListener());
		goButton.addActionListener(new GoListener());
	}

	private void registerOptions() {
		ConfigurationManager.registerConfigurable(this);
		ConfigurationManager.registerOption(this, new Option("regex", ".*+", ""), new RegexAction());
		ConfigurationManager.registerOption(this, new Option("replace", ".*+", ""), new ReplaceAction());
		ConfigurationManager.registerOption(this, new Option("input", ".*+", ""), new InputAction());
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

	private class RegexAction implements OptionAction {
		@Override
		public void actionPerformed(String value) {
			if (regex.getText().equals("") && value != null && !value.equals("")) {
				regex.setText(value);
			}
		}
	}

	private class ReplaceAction implements OptionAction {
		@Override
		public void actionPerformed(String value) {
			if (replace.getText().equals("") && value != null && !value.equals("")) {
				replace.setText(value);
			}
		}
	}

	private class InputAction implements OptionAction {
		@Override
		public void actionPerformed(String value) {
			if (input.getText().equals("") && value != null && !value.equals("")) {
				input.setText(value);
			}
		}
	}

	private class GoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String r = regex.getText();
			String x = replace.getText();
			String i = input.getText();

			Pattern pattern = Pattern.compile(r);
			Matcher matcher = pattern.matcher(i);
			if (matcher.matches()) {
				output.setText("Success");
				String[] groups = new String[matcher.groupCount() + 1];
				for (int g = 0; g < groups.length; g++) {
					groups[g] = g + ") " + matcher.group(g);
				}
				groupList.setListData(groups);
			} else {
				output.setText("Failure");
				groupList.setListData(new String[0]);
			}
			if (!x.equals("")) {
				String replace = i.replaceAll(r, x);
				if (!i.equals(replace)) {
					output.setText(replace);
				}
			}
			ConfigurationManager.updateOption(MainPanel.this, new Option("regex", ".*+", regex.getText()));
			ConfigurationManager.updateOption(MainPanel.this, new Option("replace", ".*+", replace.getText()));
			ConfigurationManager.updateOption(MainPanel.this, new Option("input", ".*+", input.getText()));
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

	public JList<String> getGroups() {
		return groupList;
	}
}
