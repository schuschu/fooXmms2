package org.dyndns.schuschu.xmms2client.view.element;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.action.base.FooSource;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceView;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;

public class FooList implements FooInterfaceView,FooInterfaceControl,FooInterfaceAction {

	private Vector<FooAction> mouseActions;
	private Vector<FooAction> keyboardActions;
	private FooInterfaceBackend backend;
	private List list;

	public FooList(Composite parent, int style) {

		mouseActions = new Vector<FooAction>();
		keyboardActions = new Vector<FooAction>();

		setList(new List(parent, style));

		getList().addSelectionListener(createSelectionListener());
		getList().addMouseListener(createMouseListener());
		getList().addKeyListener(createKeyListener());

	}

	public FooList(Composite parent) {
		this(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	}

	private SelectionListener createSelectionListener() {
		return new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				backend.selectionChanged();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		};
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
	public FooInterfaceBackend getBackend() {
		return backend;
	}

	@Override
	public void setBackend(FooInterfaceBackend backend) {
		this.backend = backend;

	}

	@Override
	public int[] getIndices() {
		return getList().getSelectionIndices();
	}

	@Override
	public void setContent(Vector<String> content) {
		String[] items = new String[content.size()];
		content.toArray(items);
		list.setItems(items);
	}

	@Override
	public void setSelection(int[] indices) {
		getList().setSelection(indices);

	}

	public void setLayoutData(Object layoutData) {
		this.list.setLayoutData(layoutData);
	}

	public void setList(List list) {
		this.list = list;
	}

	public List getList() {
		return list;
	}

	public void setMenu(FooMenu menu) {
		list.setMenu(menu.getMenu());
	}

	@Override
	public Control getControl() {
		return list;
	}
}
