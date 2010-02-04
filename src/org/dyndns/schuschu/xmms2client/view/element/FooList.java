package org.dyndns.schuschu.xmms2client.view.element;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.backend.FooInterfaceBackend;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class FooList implements FooInterfaceViewElement {

	private FooInterfaceBackend backend;
	private List list;

	public FooList(Composite parent, int style) {
		list = new List(parent, style);
		list.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				backend.selectionChanged();				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void addKeyListener(KeyListener key) {
		list.addKeyListener(key);

	}

	@Override
	public void addMouseListener(MouseListener mouse) {
		list.addMouseListener(mouse);

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
		return list.getSelectionIndices();
	}

	@Override
	public void removeKeyListener(KeyListener key) {
		list.removeKeyListener(key);

	}

	@Override
	public void removeMouseListener(MouseListener mouse) {
		list.removeMouseListener(mouse);

	}

	@Override
	public void setContent(Vector<String> content) {
		list.removeAll();
		for (String s : content) {
			list.add(s);
		}
	}

	@Override
	public void setSingleSelectionMode() {
		// TODO singleselectionmode

	}

	@Override
	public void setSelection(int[] indices) {
		list.setSelection(indices);

	}
}
