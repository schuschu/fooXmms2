package org.dyndns.schuschu.xmms2client.interfaces;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;

/**
 * @author schuschu
 * 
 */
public interface FooInterfaceView{

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
	
	public void highlight();
	
	public void setMenu(FooMenu menu);
}
