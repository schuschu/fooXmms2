package org.dyndns.schuschu.xmms2client.interfaces;

import java.util.Observer;

import se.fnord.xmms2.client.types.CollectionExpression;

public interface FooInterfaceBackendFilter extends FooInterfaceBackend, Observer {

	/**
	 * Sets backend baseContent to all media
	 */
	public void setToAll();

	/**
	 * getter function for the CollectionExpression filteredConent
	 * 
	 * @return
	 */
	public CollectionExpression getFilteredConetent();

	/**
	 * getter function for the FooInterfaceBackend contentProvider
	 * 
	 * @return
	 */
	public FooInterfaceBackendFilter getContentProvider();

	/**
	 * setter function for the FooInterfaceBackend contentProvider
	 * 
	 * @param contentProvider
	 */
	public void setContentProvider(FooInterfaceBackendFilter contentProvider);
	
	public void addObserver(Observer o);

}
