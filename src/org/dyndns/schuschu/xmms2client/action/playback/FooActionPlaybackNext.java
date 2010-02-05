package org.dyndns.schuschu.xmms2client.action.playback;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Playback;

public class FooActionPlaybackNext extends FooActionPlayback {

	public FooActionPlaybackNext(FooInterfaceClickable clickable,
			Client client) {
		super(clickable, client);
	}

	@Override
	public void clicked() {
			Playback.next().execute(getClient());
	}

}
