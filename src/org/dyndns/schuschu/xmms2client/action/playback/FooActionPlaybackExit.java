package org.dyndns.schuschu.xmms2client.action.playback;

import org.dyndns.schuschu.xmms2client.action.FooInterfaceClickable;

public class FooActionPlaybackExit extends FooActionPlayback {

	public FooActionPlaybackExit(FooInterfaceClickable clickable) {
		super(clickable, null);
	}

	@Override
	public void clicked() {
		System.exit(0);
	}

}
