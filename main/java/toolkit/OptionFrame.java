package toolkit;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class OptionFrame extends JFrame {
	String[] classes;
	Option[][] options;

	public OptionFrame(String[] panels, Option[][] values) {
		super("OptionFrame");
		setName("OptionFrame");
		setLayout(new GridBagLayout());

		if (panels.length != values.length) {
			throw new IllegalArgumentException(
					"panels and values length don't match: " + panels.length + '-' + values.length);
		}

		int y = 0;
		for (int o = 0; o < panels.length; o++) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1, 0, 0));
			panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), panels[o]));

			int h = 2 * values[o].length + 1;
			addComponent(panel, 0, y, 1, h);
			y += h;

			KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
			for (int i = 0; i < values[o].length; i++) {
				panel.add(new JLabel(values[o][i].getName() + ':'));
				JTextField optionField = new JTextField(values[o][i].getValue());
				optionField.getDocument().addDocumentListener(new TextFieldListener(optionField, values[o][i]));
				optionField.getKeymap().addActionForKeyStroke(esc, new CancelAction());
				panel.add(optionField);
			}
		}
		JPanel buttonPanel = new JPanel();
		addComponent(buttonPanel, 0, y, 1, 1);
		buttonPanel.setLayout(new GridLayout(1, 0, 0, 0));
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new OkListener());
		buttonPanel.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelListener());
		buttonPanel.add(cancelButton);
		setVisible(true);

		classes = panels;
		options = values;
	}

	public class TextFieldListener implements DocumentListener {
		private Option option;
		private JTextField textField;

		public TextFieldListener(JTextField t, Option o) {
			option = o;
			textField = t;
		}//*/

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
				option.setValue(textField.getText());
				textField.setBackground(Color.green);
			} catch (IllegalArgumentException iae) {
				textField.setBackground(Color.orange);
			}
		}

	}

	private void addComponent(Component c, int x, int y, int w, int h) {
		add(c, new GridBagConstraints(x, y, w, h, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

	}

	private class OkListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ConfigurationManager.updateOptions(classes, options);
			dispose();
		}
	}

	private class CancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	private class CancelAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
