package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

public class FooButton implements FooInterfaceClickable {

	private Button button;

	public FooButton(Composite parent, int style) {
		setButton(new Button(parent, style));
	}

	@Override
	public void addListener(Listener listener) {
		button.addListener(SWT.Selection, listener);
	}

	@Override
	public void removeListener(Listener listener) {
		button.removeListener(SWT.Selection, listener);
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public Button getButton() {
		return button;
	}

	public void setLayoutData(Object layoutData) {
		button.setLayoutData(layoutData);
	}
	
	public void setText(String string){
		button.setText(string);
	}

}
