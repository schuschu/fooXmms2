package org.dyndns.schuschu.xmms2client.action;

import javax.swing.JOptionPane;

import org.dyndns.schuschu.xmms2client.backend.FooBackendMedia;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;

import se.fnord.xmms2.client.Client;

public class FooActionMenuFormat extends FooActionMenu {

	public FooActionMenuFormat(FooInterfaceClickable clickable,
			FooBackendMedia backend, Client client) {
		super(clickable, backend, client);
	}

	@Override
	public void clicked() {
		String current = getBackend().getFormat();
		
		String input = JOptionPane.showInputDialog("Please enter new Format:\n(i.e.: %artist% - %album%: %title% ",current);
						
		if (input != null) {
			getBackend().setFormat(input);
			
			getBackend().getView().setSelection(new int[]{-1});
			getBackend().refresh();
			getBackend().generateFilteredContent();
		}
	}
}
