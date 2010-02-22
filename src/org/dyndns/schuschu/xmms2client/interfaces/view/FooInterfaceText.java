package org.dyndns.schuschu.xmms2client.interfaces.view;

import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackendText;

public interface FooInterfaceText {
	
	public void setText(String string);
	
	public void setBackend(FooInterfaceBackendText backend);
	
	public FooInterfaceBackendText getBackend();
}
