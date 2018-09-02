package frame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class ListFrame extends JFrame {
	JList list = new JList();

	public ListFrame(String title, boolean focusable) {
		super(title);
		setName("ListFrame");
		getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
		setFocusableWindowState(focusable);
	}

	public void setListData(String[] data) {
		assert data != null : "data=null";
		list.setListData(data);
		setVisible(true);
	}
}
