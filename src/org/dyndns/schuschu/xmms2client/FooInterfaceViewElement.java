package org.dyndns.schuschu.xmms2client;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Vector;

/**
 * @author schuschu
 * 
 */
public interface FooInterfaceViewElement{

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
	
	public void setActionManager(FooInterfaceAction action);
	
	public FooInterfaceAction getActionManager();
	
	public void addKeyListener(KeyListener key);
	
	public void removeKeyListener(KeyListener key);
	
	public void addMouseListener(MouseListener mouse);
	
	public void removeMouseListener(MouseListener mouse);
		
	public void setSingleSelectionMode();

	public void setSelection(int[] indices);
}
