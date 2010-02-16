package org.dyndns.schuschu.xmms2client.view.element;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.Action.FooAction;
import org.dyndns.schuschu.xmms2client.Action.FooSource;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class FooTable implements FooInterfaceViewElement {

	private Vector<FooAction> mouseActions;
	private Vector<FooAction> keyboardActions;
	private Table table;
	private FooInterfaceBackend backend;
	private int highlight = -1;

	public FooTable(Composite parent, int style) {
		table = new Table(parent, style);

		mouseActions = new Vector<FooAction>();
		keyboardActions = new Vector<FooAction>();

		getReal().addMouseListener(createMouseListener());
		getReal().addKeyListener(createKeyListener());
	}

	@Override
	public void setContent(Vector<String> content) {

		table.removeAll();
		// TODO: adapt for multiple columns via constructor etc
		TableColumn column;
		if (table.getColumnCount() == 0) {
			new TableColumn(table, SWT.NONE);
		}
		column = table.getColumn(0);

		// column.setWidth(table.getSize().x);

		for (String s : content) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(s);
		}
		column.pack();

		highlight();
	}

	@Override
	public void highlight() {

		int index = backend.getCurrentPos();

		final Color hlcolor = table.getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_NORMAL_SHADOW);
		final Color defcolor = table.getDisplay().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND);

		FontData boldData = table.getFont().getFontData()[0];
		boldData.setStyle(SWT.BOLD);
		Font boldFont = new Font(table.getDisplay(), boldData);

		FontData fontData = table.getFont().getFontData()[0];
		Font defFont = new Font(table.getDisplay(), fontData);

		if (table.getItemCount() != 0) {

			if (index != highlight) {
				if (highlight >= 0 && highlight < table.getItemCount()) {
					table.getItem(highlight).setBackground(defcolor);
					table.getItem(highlight).setFont(defFont);
				}

				highlight = index;
			}

			if (highlight >= 0 && highlight < table.getItemCount()) {
				table.getItem(highlight).setBackground(hlcolor);
				table.getItem(highlight).setFont(boldFont);
			}

			table.getColumn(0).pack();
		}
	}

	// TODO: ENUMS!
	private MouseListener createMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent arg0) {
				for (FooAction a : mouseActions) {
					if (a.code == 1) {
						a.execute();
					}
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				for (FooAction a : mouseActions) {
					if (a.code == 2) {
						a.execute();
					}
				}
			}
		};
	}

	// TODO: ENUMS!
	private KeyListener createKeyListener() {
		return new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				for (FooAction a : keyboardActions) {
					if (arg0.keyCode == a.code) {
						a.execute();
					}
				}

			}
		};
	}

	public void addAction(FooSource source, FooAction action) {
		switch (source) {
		case MOUSE:
			mouseActions.add(action);
			break;
		case KEYBOARD:
			keyboardActions.add(action);
			break;
		}
	}

	public void removeAction(FooSource source, FooAction action) {
		switch (source) {
		case MOUSE:
			mouseActions.remove(action);
			break;
		case KEYBOARD:
			keyboardActions.remove(action);
			break;
		}
	}

	@Override
	public void setMenu(FooMenu menu) {
		table.setMenu(menu.getMenu());

	}

	@Override
	public void setSelection(int[] indices) {
		table.setSelection(indices);

	}

	@Override
	public void setSingleSelectionMode() {
		// TODO find out if swt can do this

	}

	@Override
	public void setLayoutData(Object layoutData) {
		table.setLayoutData(layoutData);
	}

	@Override
	public void addKeyListener(KeyListener key) {
		table.addKeyListener(key);
	}

	@Override
	public void addMouseListener(MouseListener mouse) {
		table.addMouseListener(mouse);
	}

	@Override
	public FooInterfaceBackend getBackend() {
		return backend;
	}

	@Override
	public int[] getIndices() {
		return table.getSelectionIndices();
	}

	@Override
	public Control getReal() {
		return table;
	}

	@Override
	public void removeKeyListener(KeyListener key) {
		table.removeKeyListener(key);

	}

	@Override
	public void removeMouseListener(MouseListener mouse) {
		table.removeMouseListener(mouse);

	}

	@Override
	public void setBackend(FooInterfaceBackend backend) {
		this.backend = backend;

	}

}
