package toolkit;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class ExceptionHandlerFrame extends JFrame implements Thread.UncaughtExceptionHandler {
	private Vector listItems = new Vector();

	public ExceptionHandlerFrame() {
		super("EXCEPTION");
		setName("ExceptionHandlerFrame");
		GridLayout layout = new GridLayout(0, 1, 0, 0);
		getContentPane().setLayout(layout);
	}

	@Override
	public void uncaughtException(Thread thr, Throwable exc) {
		//		items.add(thr.getStackTrace());
		listItems.add(0, new NamedVector(exc.toString(), exc.getStackTrace()));
		showError();
	}

	public void showError() {
		getContentPane().removeAll();
		JTree tree = new JTree(listItems);
		getContentPane().add(new JScrollPane(tree));
		tree.expandRow(0);
		setVisible(true);
	}

	private class NamedVector<E> extends Vector<E> {
		private String name;

		private NamedVector(String n) {
			name = n;
		}

		private NamedVector(String n, E[] elements) {
			this(n);
			for (int i = 0; i < elements.length; i++) {
				add(elements[i]);
			}
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
