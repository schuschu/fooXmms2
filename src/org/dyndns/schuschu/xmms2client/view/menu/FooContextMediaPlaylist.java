package org.dyndns.schuschu.xmms2client.view.menu;

import org.dyndns.schuschu.xmms2client.action.FooActionMenuFormat;
import org.dyndns.schuschu.xmms2client.backend.FooBackendMedia;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.eclipse.swt.SWT;

import se.fnord.xmms2.client.Client;

public class FooContextMediaPlaylist {

	FooInterfaceViewElement view;
	FooMenu menu;

	public FooContextMediaPlaylist(FooInterfaceViewElement view,
			FooBackendMedia backend, Client client) {
		this.view = view;

		menu = new FooMenu(view.getReal());

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		FooActionMenuFormat format = new FooActionMenuFormat(formatItem,
				backend, client);
		format.addListeners();
	}

	public void setMenu() {
		view.setMenu(menu);
	}

	public void removeMenu() {
		view.setMenu(new FooMenu(view.getReal()));
	}
}