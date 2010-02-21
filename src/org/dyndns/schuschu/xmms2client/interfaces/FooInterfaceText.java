package org.dyndns.schuschu.xmms2client.interfaces;

public interface FooInterfaceText {
	
	public void setText(String string);
	
	public void setBackend(FooInterfaceBackendText backend);
	
	public FooInterfaceBackendText getBackend();
}
