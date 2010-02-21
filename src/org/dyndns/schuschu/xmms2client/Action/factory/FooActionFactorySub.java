package org.dyndns.schuschu.xmms2client.Action.factory;

import org.dyndns.schuschu.xmms2client.Action.FooAction;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;

public abstract class FooActionFactorySub {

	protected FooWindow window;

	public void init(FooWindow window) {
		this.window = window;
	}

	public abstract FooAction create(String name,int code);

}
