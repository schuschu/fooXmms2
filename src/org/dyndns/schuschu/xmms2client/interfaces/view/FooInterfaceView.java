package org.dyndns.schuschu.xmms2client.interfaces.view;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceMenu;

/**
 * @author schuschu
 * 
 */
public interface FooInterfaceView extends FooInterfaceMenu{

	/**
	 * gets the currently selected indices(or index) normally by calling the
	 * superclass's function for it
	 * 
	 * @return
	 */
	public int[] getIndices();

	/**
	 * sets the visible content to the passed string-vector normally by calling
	 * the superclass's function for it
	 * 
	 * @param content
	 */
	public void setContent(Vector<String> content);

	public void setSelection(int[] indices);

	public void setLayoutData(Object layoutData);

	

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