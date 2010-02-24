package org.dyndns.schuschu.xmms2client.factories;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;

public abstract class FooActionFactorySub {

	protected abstract FooAction create(String name,int code);

}
