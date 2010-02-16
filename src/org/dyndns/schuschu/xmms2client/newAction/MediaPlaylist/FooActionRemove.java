package org.dyndns.schuschu.xmms2client.newAction.MediaPlaylist;

import org.dyndns.schuschu.xmms2client.backend.FooBackendMediaPlaylist;
import org.dyndns.schuschu.xmms2client.newAction.FooAction;

public class FooActionRemove extends FooAction {

	private final FooBackendMediaPlaylist backend;

	public FooActionRemove(int code, FooBackendMediaPlaylist backend) {
		super(code);
		this.backend = backend;
	}

	@Override
	public void execute() {
		backend.removeSelection();
	}

}
