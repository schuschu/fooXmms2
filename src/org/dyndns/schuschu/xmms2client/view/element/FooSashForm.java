package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FooSashForm implements FooInterfaceControl,FooInterfaceComposite {

	private SashForm sash;

	public FooSashForm(Composite parent) {
		this(parent, SWT.NONE);
	}

	public FooSashForm(Composite parent, int style) {
		sash = new SashForm(parent, style);
	}

	@Override
	public Control getControl() {
		return sash;
	}

	@Override
	public Composite getComposite() {
		return sash;
	}

}
