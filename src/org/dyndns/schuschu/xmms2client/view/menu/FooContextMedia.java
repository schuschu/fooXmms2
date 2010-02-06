package org.dyndns.schuschu.xmms2client.view.menu;

import org.dyndns.schuschu.xmms2client.action.backend.FooActionBackendMediaFormat;
import org.dyndns.schuschu.xmms2client.action.backend.FooActionBackendMediaOrder;
import org.dyndns.schuschu.xmms2client.backend.FooBackendMedia;
import org.dyndns.schuschu.xmms2client.view.element.FooList;
import org.eclipse.swt.SWT;

import se.fnord.xmms2.client.Client;

public class FooContextMedia {

	FooList list;
	FooMenu menu;

	public FooContextMedia(FooList list, FooBackendMedia backend, Client client) {
		this.list = list;

		menu = new FooMenu(list.getList());

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		FooActionBackendMediaFormat format = new FooActionBackendMediaFormat(
				formatItem, backend, client);
		format.addListeners();

		FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		orderItem.setText("change order");
		FooActionBackendMediaOrder order = new FooActionBackendMediaOrder(
				orderItem, backend, client);
		order.addListeners();
	}

	public void setMenu() {
		list.setMenu(menu);
	}

	public void removeMenu() {
		list.setMenu(new FooMenu(list.getList()));
	}
}