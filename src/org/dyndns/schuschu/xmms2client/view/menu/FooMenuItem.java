package org.dyndns.schuschu.xmms2client.view.menu;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;

public class FooMenuItem implements FooInterfaceClickable {

	private MenuItem item;

	public FooMenuItem(FooMenu parent, int style) {
		item = new MenuItem(parent.getMenu(), style);
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
