package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackendText;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class FooLabel implements FooInterfaceText,FooInterfaceControl {

	private Label label;
	private FooInterfaceBackendText backend;
	
	public FooLabel(Composite parent) {
		this(parent,SWT.NONE);
	}

	public FooLabel(Composite parent, int style) {
		setLabel(new Label(parent, style));
	}

	@Override
	public void setText(String string) {
		if (!getLabel().isDisposed()) {
			getLabel().setText(string);
		}

	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public Label getLabel() {
		return label;
	}

	public void setLayoutData(Object layoutData) {
		label.setLayoutData(layoutData);
	}

	@Override
	public Control getControl() {
		return label;
	}

	@Override
	public void setBackend(FooInterfaceBackendText backend) {
		this.backend = backend;
		
	}

	@Override
	public FooInterfaceBackendText getBackend() {
		return backend;
	}

}
