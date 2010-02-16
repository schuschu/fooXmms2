package org.dyndns.schuschu.xmms2client.view.menu;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;
import org.dyndns.schuschu.xmms2client.newAction.FooAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;

public class FooMenuItem implements FooInterfaceClickable {

	private MenuItem item;
	private Vector<FooAction> actions;

	public FooMenuItem(FooMenu parent, int style) {

		actions = new Vector<FooAction>();

		item = new MenuItem(parent.getMenu(), style);

		item.addListener(SWT.Selection, createListener());
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

	@Override
	public void addListener(Listener listener) {
		item.addListener(SWT.Selection, listener);

	}

	@Override
	public void removeListener(Listener listener) {
		item.removeListener(SWT.Selection, listener);

	}

	public void setText(String string) {
		item.setText(string);
	}

}
