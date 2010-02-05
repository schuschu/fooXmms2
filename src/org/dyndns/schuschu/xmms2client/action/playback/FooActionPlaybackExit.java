package org.dyndns.schuschu.xmms2client.action.playback;

import org.dyndns.schuschu.xmms2client.action.FooInterfaceViewClickable;

public class FooPluginActionPlaybackExit extends FooPluginActionPlayback {

	public FooPluginActionPlaybackExit(FooInterfaceViewClickable clickable) {
		super(clickable, null);
	}

	@Override
	public void clicked() {
		System.exit(0);
	}

}
