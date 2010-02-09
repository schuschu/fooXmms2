package org.dyndns.schuschu.xmms2client.view.element;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FooCombo implements FooInterfaceViewElement {

	private Combo combo;
	private FooInterfaceBackend backend;

	public FooCombo(Composite parent, int style) {
		setCombo(new Combo(parent, style));
		getCombo().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				backend.selectionChanged();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		getCombo().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (arg0.button == 1) {
					backend.refresh();
				}
			}
		});
	}

	@Override
	public void addKeyListener(KeyListener key) {
		combo.addKeyListener(key);

	}

	@Override
	public void addMouseListener(MouseListener mouse) {
		combo.addMouseListener(mouse);

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
	public void removeKeyListener(KeyListener key) {
		combo.removeKeyListener(key);

	}

	@Override
	public void removeMouseListener(MouseListener mouse) {
		combo.removeMouseListener(mouse);

	}

	@Override
	public void setBackend(FooInterfaceBackend backend) {
		this.backend = backend;

	}

	@Override
	public void setContent(Vector<String> content) {
		getCombo().removeAll();
		for (String s : content) {
			getCombo().add(s);
		}
	}

	@Override
	public void setSelection(int[] indices) {
		combo.select(indices[0]);

	}

	@Override
	public void setSingleSelectionMode() {
		// TODO Rethink this
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
	public Control getReal() {
		return combo;
	}
}
