package org.dyndns.schuschu.xmms2client.view.element;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.Action.base.FooAction;
import org.dyndns.schuschu.xmms2client.Action.base.FooSource;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceViewPlaylist;
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

public class FooTable implements FooInterfaceViewPlaylist,FooInterfaceControl,FooInterfaceAction {

	private Vector<FooAction> mouseActions;
	private Vector<FooAction> keyboardActions;
	private Table table;
	private FooInterfaceBackend backend;
	private int highlight = -1;

	public FooTable(Composite parent, int style) {
		setTable(new Table(parent, style));

		mouseActions = new Vector<FooAction>();
		keyboardActions = new Vector<FooAction>();

		getTable().addMouseListener(createMouseListener());
		getTable().addKeyListener(createKeyListener());
	}

	public FooTable(Composite parent) {
		this(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
	}

	@Override
	public void setContent(Vector<String> content) {

		getTable().removeAll();
		// TODO: adapt for multiple columns via constructor etc
		TableColumn column;
		if (getTable().getColumnCount() == 0) {
			new TableColumn(getTable(), SWT.NONE);
		}
		column = getTable().getColumn(0);

		for (String s : content) {
			TableItem item = new TableItem(getTable(), SWT.NONE);
			item.setText(s);
		}
		column.pack();
	}

	@Override
	public void highlight(int index) {
			
		final Color hlcolor = getTable().getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_NORMAL_SHADOW);
		final Color defcolor = getTable().getDisplay().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND);

		FontData boldData = getTable().getFont().getFontData()[0];
		boldData.setStyle(SWT.BOLD);
		Font boldFont = new Font(getTable().getDisplay(), boldData);

		FontData fontData = getTable().getFont().getFontData()[0];
		Font defFont = new Font(getTable().getDisplay(), fontData);

		if (getTable().getItemCount() != 0) {

			if (index != highlight) {
				if (highlight >= 0 && highlight < getTable().getItemCount()) {
					getTable().getItem(highlight).setBackground(defcolor);
					getTable().getItem(highlight).setFont(defFont);
				}

				highlight = index;
			}

			if (highlight >= 0 && highlight < getTable().getItemCount()) {
				getTable().getItem(highlight).setBackground(hlcolor);
				getTable().getItem(highlight).setFont(boldFont);
			}

			getTable().getColumn(0).pack();
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
		getTable().setMenu(menu.getMenu());

	}

	@Override
	public void setSelection(int[] indices) {
		getTable().setSelection(indices);

	}

	@Override
	public void setLayoutData(Object layoutData) {
		getTable().setLayoutData(layoutData);
	}

	@Override
	public FooInterfaceBackend getBackend() {
		return backend;
	}

	@Override
	public int[] getIndices() {
		return getTable().getSelectionIndices();
	}

	@Override
	public void setBackend(FooInterfaceBackend backend) {
		this.backend = backend;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Table getTable() {
		return table;
	}

	@Override
	public Control getControl() {
		return table;
	}
}
