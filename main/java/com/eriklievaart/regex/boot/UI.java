package com.eriklievaart.regex.boot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import frame.FileChooser;
import frame.MainPanel;
import regex.RegexQueue;
import regex.RegexReplace;

public class UI {

	private JFrame main = new JFrame();
	private JTabbedPane tabs = new JTabbedPane();

	private RegexQueue queue = new RegexQueue();
	private JList<RegexReplace> queueList = new JList<>();

	private MainPanel builder = new MainPanel(queue);

	private JTextArea from = new JTextArea();
	private JTextArea to = new JTextArea();

	public void init() {

		main.setTitle("Regex Tool");
		main.setName("main");
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.getContentPane().add(createTabs());
		main.getRootPane().setDefaultButton(builder.getDefaultButton());

		main.setVisible(true);
	}

	private JTabbedPane createTabs() {
		tabs.addTab("Regex Builder", createBuilder());
		tabs.addTab("Regex Queue", createQueue());
		tabs.addTab("Mass Input", createMassInput());
		return tabs;
	}

	private Component createBuilder() {
		JPanel top = new JPanel(new GridLayout(1, 0));
		top.add(builder);
		top.add(new JScrollPane(builder.getGroups()));

		JPanel bottom = new JPanel();

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(top, BorderLayout.CENTER);
		panel.add(bottom, BorderLayout.SOUTH);
		return panel;
	}

	private Component createQueue() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.BLACK);

		panel.add(new JScrollPane(queueList), BorderLayout.CENTER);
		queue.addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object data) {
				queueList.setListData((RegexReplace[]) data);
				updateOutput();
			}
		});
		return panel;
	}

	private Component createMassInput() {
		JPanel outer = new JPanel(new BorderLayout());

		JButton browse = new JButton("Browse");
		outer.add(browse, BorderLayout.NORTH);

		JButton save = new JButton("Save");
		outer.add(save, BorderLayout.SOUTH);

		addBrowseActionListener(browse);
		addSaveActionListener(save);
		addFromCaretListener();

		outer.add(createReplacePanel(), BorderLayout.CENTER);
		return outer;
	}

	private JPanel createReplacePanel() {
		JPanel replacePanel = new JPanel(new GridLayout(1, 0, 2, 2));
		replacePanel.setBackground(Color.BLACK);

		replacePanel.add(new JScrollPane(from));
		replacePanel.add(new JScrollPane(to));
		return replacePanel;
	}

	private void addFromCaretListener() {
		from.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent arg0) {
				updateOutput();
			}
		});
	}

	private void addSaveActionListener(JButton save) {
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileChooser.storeFile(main, to.getText());
			}
		});
	}

	private void addBrowseActionListener(JButton browse) {
		browse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String body = FileChooser.readFile(main);
				from.setText(body);
				updateOutput();
			}
		});
	}

	private void updateOutput() {
		to.setText(queue.process(from.getText()));
	}
}
