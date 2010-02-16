package org.dyndns.schuschu.xmms2client.newAction;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;

public class FooActionDeselect extends FooAction {

	private final FooInterfaceBackend backend;

	public FooActionDeselect(int code, FooInterfaceBackend backend) {
		super(code);
		this.backend = backend;
	}

	@Override
	public void execute() {
		backend.getView().setSelection(new int[0]);
		backend.selectionChanged();
	}

}
