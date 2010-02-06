package org.dyndns.schuschu.xmms2client.action;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class FooActionExit implements FooInterfaceAction {

	private Listener action;
	private FooInterfaceClickable clickable;

	public FooActionExit(FooInterfaceClickable clickable) {
		initialize(clickable);
	}

	public void initialize(FooInterfaceClickable clickable) {
		this.clickable = clickable;
		action = new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				clicked();

			}
		};
	}

	@Override
	public void addListeners() {
		clickable.addListener(action);
	}

	public void clicked() {
		System.exit(0);
	}

	@Override
	public void removeListeners() {
		clickable.removeListener(action);

	}
}
