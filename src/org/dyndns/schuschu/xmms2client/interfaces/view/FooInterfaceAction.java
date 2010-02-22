package org.dyndns.schuschu.xmms2client.interfaces.view;

import org.dyndns.schuschu.xmms2client.Action.base.FooAction;
import org.dyndns.schuschu.xmms2client.Action.base.FooSource;

public interface FooInterfaceAction {
	
	public void addAction(FooSource source, FooAction action);

	public void removeAction(FooSource source, FooAction action);
}
