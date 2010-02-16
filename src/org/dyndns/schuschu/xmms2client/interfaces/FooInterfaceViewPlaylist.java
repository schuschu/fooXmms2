package org.dyndns.schuschu.xmms2client.interfaces;

public interface FooInterfaceViewPlaylist extends FooInterfaceView{


	/**
	 * sets the elements backend. this defines what information it will display
	 * and or filter
	 * 
	 * @param backend
	 */
	public void setBackend(FooInterFaceBackendPlaylist backend);

	/**
	 * gets the elements backend. (no use for this found yet...)
	 * 
	 * @return
	 */
	public FooInterFaceBackendPlaylist getBackend();
	
}
