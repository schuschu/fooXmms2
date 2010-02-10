package org.dyndns.schuschu.xmms2client.backend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionNamespace;

/**
 * @author schuschu
 * 
 */
public class FooBackendPlaylist extends Observable implements Serializable,
		FooInterfaceBackend {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name;

	protected void debug(String message) {
		if (DEBUG) {
			System.out.println("debug: " + getName() + " " + message);
		}
	}

	/**
	 * I have no idea what that stupid thing is for...
	 */
	private static final long serialVersionUID = -2744778880227415342L;

	/**
	 * Specifies the XMMS2 client (the instance of the object not THIS client
	 * ... I didn't name it ...)
	 */
	private Client client;

	private FooInterfaceViewElement view;

	private CollectionExpression filteredConetent;

	private String[] playlistDatabase;

	private List<Integer> playListOrder;

	/**
	 * This is the Backend which provides the baseContent.
	 * 
	 */
	private FooInterfaceBackend contentProvider;

	private Vector<String> content;

	/**
	 * executeFilterCommand builds the xmms2-command for filtering the
	 * xmms2-database by the values gotten from the selected values and executes
	 * it, writing the result into filteredConetent
	 * 
	 * @param indices
	 *            the indices which are currently selected in view
	 * @throws InterruptedException
	 *             but i don't know why :)
	 */
	public void executeFilterCommand() throws InterruptedException {
		debug("executeFilterCommand");

		Command command = Playlist.listEntries(Playlist.ACTIVE_PLAYLIST);

		List<Integer> ids = command.executeSync(client);

		CollectionBuilder cb = new CollectionBuilder();
		cb.addIds(ids);
		CollectionExpression ce = cb.build();

		playListOrder = ids;

		setFilteredConetent(ce);

	}

	/**
	 * executeBaseCommand builds the xmms2-command for an database from
	 * baseContent and executes it, writing the result into baseDatabase
	 * 
	 * @throws InterruptedException
	 *             but i don't know why :)
	 */
	public void executeBaseCommand() throws InterruptedException {
		debug("executeBaseCommand");

		Command c = Playlist.listPlaylists();

		Map<CollectionNamespace, Set<String>> map = c.executeSync(client);

		content = new Vector<String>();

		// TODO: some sort of append but i don't know what i get here
		for (Set<String> names : map.values()) {
			for (String name : names) {
				if (!name.startsWith("_")) {
					content.add(name);
				}
			}
			// TODO: find better way to do this
			this.setPlaylistDatabase(content
					.toArray(new String[content.size()]));
		}

	}

	/**
	 * runs executeFilterCommand which generates the filterdContent and then
	 * writes it into the FooPluginViewElementList next
	 */
	public void generateFilteredContent() {
		debug("generateFilteredContent");

		try {
			executeFilterCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * runs executeBaseCommand which generates the baseDatabase and displays the
	 * Vector created by createContend into the List (a.k.a. output)
	 */
	public void refresh() {
		debug("refresh");

		try {
			executeBaseCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		view
				.setContent(new Vector<String>(Arrays
						.asList(getPlaylistDatabase())));

		try {
			view.setSelection(new int[] { getActivePlaylistId() });
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * setToAll sets the baseContent to all media in the medialib
	 */
	public void setToAll() {
		debug("setToAll");
		// TODO: what is all here? Do I need this?
	}

	/**
	 * The one and only constructor of FooPluginViewElementList
	 * 
	 * @param format
	 *            Formating String i.e.: %album% - %artist%
	 * @param filter
	 *            Which tag should be filtered by the list
	 * @param client
	 *            the xmms2-client
	 * @param view
	 *            the view element associated with this backend (wont crunch
	 *            numbers for nothing)
	 */
	public FooBackendPlaylist(Client client, FooInterfaceViewElement view) {
		debug("FooBackendPlaylist");
		this.view = view;
		this.playlistDatabase = null;
		this.setClient(client);

		// TODO: Rethink this
		// There can only be one...
		view.setSingleSelectionMode();

		refresh();

		try {
			view.setSelection(new int[] { getActivePlaylistId() });
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}
	
	// TODO: rethink this thing

	/*
	 * public int getPlaylistId(String name) throws InterruptedException {
	 * debug("getPlaylistId");
	 * 
	 * Command c = Playlist.listPlaylists(); Map<CollectionNamespace,
	 * Set<String>> map = c.executeSync(client);
	 * 
	 * // TODO: find out what to do with namespaces here Set<String> names =
	 * map.get(CollectionNamespace.PLAYLISTS); int i = 0; for (String n : names)
	 * { if (!n.startsWith("_") && n.equals(name)) { return i; } i++; } // you
	 * should NEVER get here! return 0; }
	 */

	public int getActivePlaylistId() throws InterruptedException {
		debug("getActivePlaylistId");

		Command c = Playlist.currentActive();

		String name = c.executeSync(client);

		for (int i = 0; i < playlistDatabase.length; i++) {
			if (name.equals(playlistDatabase[i])) {
				return i;
			}
		}
		// you should NEVER get here!
		return 0;
	}

	/**
	 * setter function for the CollectionExpression baseContent
	 * 
	 * @param baseConetent
	 */
	public void setBaseConetent(CollectionExpression baseConetent) {
		debug("setBaseConetent");
		// Stub
	}

	/**
	 * getter function for the CollectionExpression filteredConent
	 * 
	 * @return
	 */
	public CollectionExpression getFilteredConetent() {
		debug("getFilteredConetent");
		return filteredConetent;
	}

	/**
	 * setter function for the CollectionExpression filteredContent
	 * 
	 * @param filteredConetent
	 */
	public void setFilteredConetent(CollectionExpression filteredConetent) {
		debug("setFilteredConetent");
		this.filteredConetent = filteredConetent;
	}

	/**
	 * getter function for the Client client
	 * 
	 * @return
	 */
	public Client getClient() {
		debug("getClient");
		return client;
	}

	/**
	 * setter function for the Client client
	 * 
	 * @param client
	 */
	public void setClient(Client client) {
		debug("setClient");
		this.client = client;
	}

	public void loadPlaylist() {
		debug("loadPlaylist");

		int selection = view.getIndices()[0];

		if (selection >= 0) {

			Command c = Playlist.load(playlistDatabase[selection]);

			try {
				c.executeSync(client);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			generateFilteredContent();
		}
	}

	public String[] getPlaylistDatabase() {
		debug("getPlaylistDatabase");
		return playlistDatabase;
	}

	public void setPlaylistDatabase(String[] playlistDatabase) {
		debug("setPlaylistDatabase");
		this.playlistDatabase = playlistDatabase;
	}

	@Override
	public FooInterfaceBackend getContentProvider() {
		debug("getContentProvider");
		return contentProvider;
	}

	@Override
	public void setContentProvider(FooInterfaceBackend contentProvider) {
		debug("setContentProvider");
		this.contentProvider = contentProvider;
		contentProvider.addObserver(this);

	}

	@Override
	public void update(Observable o, Object arg) {
		debug("update");
		contentProvider.getFilteredConetent();

	}

	public void setPlayListOrder(List<Integer> playListOrder) {
		debug("setPlayListOrder");
		this.playListOrder = playListOrder;
	}

	public List<Integer> getPlayListOrder() {
		debug("getPlayListOrder");
		return playListOrder;
	}

	@Override
	public FooInterfaceViewElement getView() {
		debug("getView");
		return view;
	}

	@Override
	public void selectionChanged() {
		debug("selectionChanged");
		loadPlaylist();
	}

	@Override
	public void setCurrent(int current) {
		debug("setCurrent");
		// TODO implement this
	}

	@Override
	public int getCurrentPos() {
		debug("getCurrentPos");
		return -1;
	}

	@Override
	public Vector<String> getContent() {
		debug("getContent");
		return content;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
