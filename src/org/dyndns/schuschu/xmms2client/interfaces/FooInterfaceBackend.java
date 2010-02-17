package org.dyndns.schuschu.xmms2client.interfaces;

import java.util.Vector;

/**
 * @author schuschu
 * 
 */
public interface FooInterfaceBackend {

	/**
	 * Used to refresh oneself and if necessary ones next element
	 */
	public void refresh();

	public FooInterfaceView getView();

	public void selectionChanged();

	public Vector<String> getContent();
}
