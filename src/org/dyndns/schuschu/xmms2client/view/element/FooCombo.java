package org.dyndns.schuschu.xmms2client.view.element;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.Action.FooAction;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceView;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FooCombo implements FooInterfaceView,FooInterfaceControl {

	private Vector<FooAction> mouseActions;
	private Vector<FooAction> keyboardActions;
	private Combo combo;
	private FooInterfaceBackend backend;

	public FooCombo(Composite parent, int style) {

		mouseActions = new Vector<FooAction>();
		keyboardActions = new Vector<FooAction>();

		setCombo(new Combo(parent, style));

		getCombo().addMouseListener(createMouseListener());
		getCombo().addKeyListener(createKeyListener());
		getCombo().addSelectionListener(createSelectionListener());
	}

	public FooCombo(Composite parent) {
		this(parent, SWT.READ_ONLY);
	}

	private SelectionListener createSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				backend.selectionChanged();
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

	@Override
	public FooInterfaceBackend getBackend() {
		return backend;
	}

	@Override
	public int[] getIndices() {
		return (new int[] { combo.getSelectionIndex() });
	}

	@Override
	public void setBackend(FooInterfaceBackend backend) {
		this.backend = backend;

	}

	@Override
	public void setContent(Vector<String> content) {
		String[] items = new String[content.size()];
		content.toArray(items);
		combo.setItems(items);
	}

	@Override
	public void setSelection(int[] indices) {
		combo.select(indices[0]);

	}

	public void setLayoutData(Object layoutData) {
		this.combo.setLayoutData(layoutData);
	}

	public void setCombo(Combo combo) {
		this.combo = combo;
	}

	public Combo getCombo() {
		return combo;
	}

	@Override
	public void setMenu(FooMenu menu) {
		combo.setMenu(menu.getMenu());
	}

	@Override
	public Control getControl() {
		return combo;
	}
}
