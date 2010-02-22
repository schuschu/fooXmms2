package org.dyndns.schuschu.xmms2client.Action.base;

public abstract class FooAction {

	public final int code;
	public final String name;

	public FooAction(String name, int code) {
		this.code = code;
		this.name = name;
	}

	abstract public void execute();
}
