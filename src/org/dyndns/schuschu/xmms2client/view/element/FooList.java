package org.dyndns.schuschu.xmms2client.view.element;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;

public class FooList implements FooInterfaceViewElement {

	private FooInterfaceBackend backend;
	private List list;

	public FooList(Composite parent, int style) {
		setList(new List(parent, style));

		getList().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				backend.selectionChanged();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

	@Override
	public void addKeyListener(KeyListener key) {
		getList().addKeyListener(key);

	}

	@Override
	public void addMouseListener(MouseListener mouse) {
		getList().addMouseListener(mouse);

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
	public void removeKeyListener(KeyListener key) {
		getList().removeKeyListener(key);

	}

	@Override
	public void removeMouseListener(MouseListener mouse) {
		getList().removeMouseListener(mouse);

	}

	@Override
	public void setContent(Vector<String> content) {

		getList().removeAll();
		for (String s : content) {
			getList().add(s);
		}
	}

	@Override
	public void setSingleSelectionMode() {
		// TODO Find a way to change style while running or remove

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
	public Control getReal() {
		return list;
	}

	@Override
	public void highlight() {
		// TODO: find something to highlight		
	}
}
