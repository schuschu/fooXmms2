package org.dyndns.schuschu.xmms2client.action.backend;

import org.dyndns.schuschu.xmms2client.backend.FooBackendMedia;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import se.fnord.xmms2.client.Client;

abstract public class FooActionBackendMedia implements FooInterfaceAction {

	// TODO: find better way to change format
	private FooBackendMedia backend;
	private Client client;
	private Listener action;
	private FooInterfaceClickable clickClickable;

	public FooActionBackendMedia(FooInterfaceClickable clickable,
			FooBackendMedia backend, Client client) {
		initialize(clickable, backend, client);
	}

	public void initialize(FooInterfaceClickable clickable,
			FooBackendMedia backend, Client client) {
		setClickClickable(clickable);
		setClient(client);
		setBackend(backend);

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

	public void setBackend(FooBackendMedia backend) {
		this.backend = backend;
	}

	public FooBackendMedia getBackend() {
		return backend;
	}

}
