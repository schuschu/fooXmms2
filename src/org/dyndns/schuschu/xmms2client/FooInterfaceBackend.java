package org.dyndns.schuschu.xmms2client;

import se.fnord.xmms2.client.types.CollectionExpression;

/**
 * @author schuschu
 * 
 */
public interface FooInterfaceBackend {

	/**
	 * Used to refresh oneself and if necessary ones next element
	 */
	public void refresh();

	// TODO: more unified way in case of queuing content and playlist (is this
	// even possible?)
	/**
	 * Yet to fix, currently needed only (there is no other) in MediaBackend
	 * sets the base content and possibly (make it so!) refreshes the next
	 * element
	 * 
	 * @param baseConetent
	 */
	public void setBaseConetent(CollectionExpression baseConetent);

	/**
	 * Used to apply all filters to baseContent and push it to next element
	 */
	public void generateFilteredContent();

	/**
	 * Sets backend baseContent to all media/playlists whatever the content is
	 */
	public void setToAll();

	/**
	 * used to set next element
	 * 
	 * @param next
	 */
	public void setNext(FooInterfaceViewElement next);

	/**
	 * getter function for the CollectionExpression filteredConent
	 * 
	 * @return
	 */
	public CollectionExpression getFilteredConetent();

	/**
	 * setter function for the CollectionExpression filteredContent
	 * 
	 * @param filteredConetent
	 */
	public void setFilteredConetent(CollectionExpression filteredConetent);

	/**
	 * enqueues the current selection (filteredContent) to active playlist
	 */
	public void enqueuSelection();

}
