package org.dyndns.schuschu.xmms2client.action.playback;

import org.dyndns.schuschu.xmms2client.action.FooInterfaceClickable;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Playback;

public class FooActionPlaybackStop extends FooActionPlayback {

	public FooActionPlaybackStop(FooInterfaceClickable clickable,
			Client client) {
		super(clickable, client);
	}

	@Override
	public void clicked() {
		try {
			Playback.stop().executeSync(getClient());
		} catch (InterruptedException e1) {
			Thread.currentThread().interrupt();
			e1.printStackTrace();
		}
	}

}
