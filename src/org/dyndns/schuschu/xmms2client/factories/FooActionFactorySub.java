package org.dyndns.schuschu.xmms2client.factories;

import org.dyndns.schuschu.xmms2client.Action.base.FooAction;

public abstract class FooActionFactorySub {

	public abstract FooAction create(String name,int code);

}
