package org.dyndns.schuschu.xmms2client.view.element;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class FooButton implements FooInterfaceControl {

	private Button button;
	private Vector<FooAction> actions;

	public FooButton(Composite parent, int style) {

		actions = new Vector<FooAction>();

		setButton(new Button(parent, style));

		button.addListener(SWT.Selection, createListener());
	}

	private Listener createListener() {
		return new Listener() {
			// TODO: sudo make it good
			@Override
			public void handleEvent(Event arg0) {
				for (FooAction a : actions) {
					a.execute();
				}
			}
		};
	}

	public void addAction(FooAction action) {
		actions.add(action);
	}

	public void removeAction(FooAction action) {
		actions.remove(action);
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

	public void setText(String string) {
		button.setText(string);
	}

	@Override
	public Control getControl() {
		return button;
	}

}
