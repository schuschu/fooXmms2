package org.dyndns.schuschu.xmms2client.action.playlist;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import se.fnord.xmms2.client.Client;

abstract public class FooActionPlaylist implements FooInterfaceAction {

	// TODO: enum vodoo?
	
	private Client client;
	private Listener action;
	private FooInterfaceClickable clickClickable;

	public FooActionPlaylist(FooInterfaceClickable clickable,
			Client client) {
		initialize(clickable, client);
	}

	public void initialize(FooInterfaceClickable clickable, Client client) {
		setClickClickable(clickable);
		setClient(client);
		setAction(new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				clicked();

			}
		});
	}

	@Override
	public void addListeners() {
		clickClickable.addListener(getAction());
	}

	abstract public void clicked();

	@Override
	public void removeListeners() {
		clickClickable.removeListener(getAction());

	}

	public void setAction(Listener action) {
		this.action = action;
	}

	public Listener getAction() {
		return action;
	}

	public void setClickClickable(FooInterfaceClickable clickClickable) {
		this.clickClickable = clickClickable;
	}

	public FooInterfaceClickable getClickClickable() {
		return clickClickable;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
