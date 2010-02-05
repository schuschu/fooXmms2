package org.dyndns.schuschu.xmms2client.view.menu;

import org.dyndns.schuschu.xmms2client.action.FooInterfaceClickable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class FooMenuItem implements FooInterfaceClickable {

	private MenuItem item;

	public FooMenuItem(Menu parent, int style) {
		item = new MenuItem(parent, style);
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
