package org.dyndns.schuschu.xmms2client.action.playback;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Playback;

public class FooActionPlayback implements FooInterfaceAction {

	private Client client;
	private Listener action;
	private FooInterfaceClickable clickClickable;
	private FooPlaybackType type;

	public FooActionPlayback(FooPlaybackType type,
			FooInterfaceClickable clickable, Client client) {
		initialize(type, clickable, client);
	}

	public void initialize(FooPlaybackType type,
			FooInterfaceClickable clickable, Client client) {
		setType(type);
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

	public void clicked() {
		switch (type) {
		case NEXT:
			Playback.next().execute(getClient());
			break;
		case PAUSE:
			Playback.togglePlay().execute(getClient());
			break;
		case PLAY:
			Playback.play().execute(getClient());
			break;
		case PREV:
			Playback.prev().execute(getClient());
			break;
		case STOP:
			Playback.stop().execute(getClient());
			break;
		}

	}

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

	public void setType(FooPlaybackType type) {
		this.type = type;
	}

	public FooPlaybackType getType() {
		return type;
	}

}
