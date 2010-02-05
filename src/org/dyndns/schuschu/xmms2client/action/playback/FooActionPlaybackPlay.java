package org.dyndns.schuschu.xmms2client.action.playback;

import org.dyndns.schuschu.xmms2client.action.FooInterfaceViewClickable;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Playback;

public class FooPluginActionPlaybackPlay extends FooPluginActionPlayback {

	public FooPluginActionPlaybackPlay(FooInterfaceViewClickable clickable,
			Client client) {
		super(clickable, client);
	}

	@Override
	public void clicked() {
		try {
			Playback.play().executeSync(getClient());
		} catch (InterruptedException e1) {
			Thread.currentThread().interrupt();
			e1.printStackTrace();
		}
	}

}
