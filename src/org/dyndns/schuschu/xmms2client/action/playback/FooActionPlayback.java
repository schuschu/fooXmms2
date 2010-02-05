package org.dyndns.schuschu.xmms2client.action.playback;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.dyndns.schuschu.xmms2client.action.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.action.FooInterfaceViewClickable;

import se.fnord.xmms2.client.Client;

abstract public class FooPluginActionPlayback implements FooInterfaceAction {

	private Client client;
	private ActionListener action;
	private FooInterfaceViewClickable clickClickable;

	public FooPluginActionPlayback(FooInterfaceViewClickable clickable,
			Client client) {
		initialize(clickable, client);
	}

	public void initialize(FooInterfaceViewClickable clickable, Client client) {
		setClickClickable(clickable);
		setClient(client);
		setAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clicked();

			}
		});
	}

	@Override
	public void addListeners() {
		clickClickable.addActionListener(getAction());
	}

	abstract public void clicked();

	@Override
	public void removeListeners() {
		clickClickable.removeActionListener(getAction());

	}

	public void setAction(ActionListener action) {
		this.action = action;
	}

	public ActionListener getAction() {
		return action;
	}

	public void setClickClickable(FooInterfaceViewClickable clickClickable) {
		this.clickClickable = clickClickable;
	}

	public FooInterfaceViewClickable getClickClickable() {
		return clickClickable;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
