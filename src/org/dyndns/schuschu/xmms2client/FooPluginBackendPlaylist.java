package org.dyndns.schuschu.xmms2client;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionNamespace;
import se.fnord.xmms2.client.types.CollectionType;

/**
 * @author schuschu
 * 
 */
public class FooPluginBackendPlaylist extends FooPluginBackendBase implements
		Serializable {

	/**
	 * this List contains all values which will be usable by this list it should
	 * by possible to be modified by all ViewElements in the client. Maybe a
	 * different position for this list is needed
	 */
	static List<String> POSSIBLE_VALUES = Arrays.asList(new String[] {
			"artist", "album", "date", "title" });

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

	// TODO: unify these?
	private String[] playlistDatabase;

	/**
	 * This is the Backend which provides the baseContent.
	 * 
	 */
	private FooPluginBackendBase contentProvider;

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
	public void executeFilterCommand(int[] indices) throws InterruptedException {

		if (indices.length == 0 || indices[0] != -1) {

			Vector<CollectionExpression> ops = new Vector<CollectionExpression>();
			CollectionBuilder operand = new CollectionBuilder();
			CollectionBuilder master = new CollectionBuilder();
			master.setType(CollectionType.UNION);

			for (int index : indices) {
				if (playlistDatabase[index] != null) {
					Command command = Playlist
							.listEntries(playlistDatabase[index].toString());

					List<Integer> ids = command.executeSync(client);

					operand.setType(CollectionType.IDLIST);
					operand.addIds(ids);
					ops.add(operand.build());
				}
			}
			master.addOps(ops);

			setFilteredConetent(master.build());

		} else {
			setFilteredConetent(CollectionBuilder.getEmptyExpression());
		}
	}

	/**
	 * executeBaseCommand builds the xmms2-command for an database from
	 * baseContent and executes it, writing the result into baseDatabase
	 * 
	 * @throws InterruptedException
	 *             but i don't know why :)
	 */
	public void executeBaseCommand() throws InterruptedException {

		Command c = Playlist.listPlaylists();

		Map<CollectionNamespace, Set<String>> map = c.executeSync(client);

		Vector<String> content = new Vector<String>();

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

		try {
			executeFilterCommand(view.getIndices());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}

		setChanged();
		notifyObservers();

		/*
		 * if (next != null) {
		 * next.getBackend().setBaseConetent(filteredConetent); }
		 */
	}

	/**
	 * runs executeBaseCommand which generates the baseDatabase and displays the
	 * Vector created by createContend into the List (a.k.a. output)
	 */
	public void refresh() {
		try {
			executeBaseCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}

		view
				.setContent(new Vector<String>(Arrays
						.asList(getPlaylistDatabase())));

		setChanged();

		/*
		 * if (next != null) { next.getBackend().refresh(); }
		 */
	}

	/**
	 * setToAll sets the baseContent to all media in the medialib
	 */
	public void setToAll() {
		// TODO: what is all here?
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
	public FooPluginBackendPlaylist(Client client, FooInterfaceViewElement view) {
		this.view = view;
		this.playlistDatabase = null;
		this.setClient(client);
		
		// There can only be one...
		view.setSingleSelectionMode();
	}

	/**
	 * setter function for the CollectionExpression baseContent
	 * 
	 * @param baseConetent
	 */
	public void setBaseConetent(CollectionExpression baseConetent) {
		// Stub
	}

	/**
	 * getter function for the CollectionExpression filteredConent
	 * 
	 * @return
	 */
	public CollectionExpression getFilteredConetent() {
		return filteredConetent;
	}

	/**
	 * setter function for the CollectionExpression filteredContent
	 * 
	 * @param filteredConetent
	 */
	public void setFilteredConetent(CollectionExpression filteredConetent) {
		this.filteredConetent = filteredConetent;
	}

	/**
	 * getter function for the Client client
	 * 
	 * @return
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * setter function for the Client client
	 * 
	 * @param client
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public void enqueuSelection() {
		Command c = Playlist.load(playlistDatabase[view.getIndices()[0]]);

		try {
			c.executeSync(client);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String[] getPlaylistDatabase() {
		return playlistDatabase;
	}

	public void setPlaylistDatabase(String[] playlistDatabase) {
		this.playlistDatabase = playlistDatabase;
	}

	@Override
	public FooPluginBackendBase getContentProvider() {
		return contentProvider;
	}

	@Override
	public void setContentProvider(FooPluginBackendBase contentProvider) {
		this.contentProvider = contentProvider;
		contentProvider.addObserver(this);

	}

	@Override
	public void update(Observable o, Object arg) {
		contentProvider.getFilteredConetent();

	}
}
