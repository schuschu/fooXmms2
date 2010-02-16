package org.dyndns.schuschu.xmms2client.Action;

public abstract class FooAction {

	public final int code;

	public FooAction(int code) {
		this.code = code;
	}

	abstract public void execute();
}
