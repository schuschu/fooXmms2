package org.dyndns.schuschu.xmms2client.view.menu;

import org.dyndns.schuschu.xmms2client.action.FooActionMenuFormat;
import org.dyndns.schuschu.xmms2client.action.FooActionMenuOrder;
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
		FooActionMenuFormat format = new FooActionMenuFormat(
				formatItem, backend, client);
		format.addListeners();

		FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		orderItem.setText("change order");
		FooActionMenuOrder order = new FooActionMenuOrder(
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