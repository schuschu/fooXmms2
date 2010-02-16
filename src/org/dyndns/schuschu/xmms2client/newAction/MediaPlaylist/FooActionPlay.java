package org.dyndns.schuschu.xmms2client.newAction.MediaPlaylist;

import org.dyndns.schuschu.xmms2client.backend.FooBackendMediaPlaylist;
import org.dyndns.schuschu.xmms2client.newAction.FooAction;

public class FooActionPlay extends FooAction {

	private final FooBackendMediaPlaylist backend;

	public FooActionPlay(int code, FooBackendMediaPlaylist backend) {
		super(code);
		this.backend = backend;
	}

	@Override
	public void execute() {
		backend.playSelection();
	}

}
