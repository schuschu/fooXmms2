package org.dyndns.schuschu.xmms2client.interfaces;

public interface FooInterfaceViewElement extends FooInterfaceView{
	/**
	 * sets the elements backend. this defines what information it will display
	 * and or filter
	 * 
	 * @param backend
	 */
	public void setBackend(FooInterfaceBackend backend);

	/**
	 * gets the elements backend. (no use for this found yet...)
	 * 
	 * @return
	 */
	public FooInterfaceBackend getBackend();
}
