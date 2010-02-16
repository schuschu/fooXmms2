package org.dyndns.schuschu.xmms2client.newAction.Media;

import org.dyndns.schuschu.xmms2client.backend.FooBackendMedia;
import org.dyndns.schuschu.xmms2client.newAction.FooAction;

public class FooActionEnqueu extends FooAction {

	private final FooBackendMedia backend;

	public FooActionEnqueu(int code, FooBackendMedia backend) {
		super(code);
		this.backend = backend;
	}

	@Override
	public void execute() {
		backend.enqueuSelection();
	}

}
