package org.dyndns.schuschu.xmms2client;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.Dict;
import se.fnord.xmms2.client.types.InfoQuery;

/**
 * @author schuschu
 * 
 */
public class FooPluginBackendMedia extends FooPluginBackendBase implements
		Serializable {

	/**
	 * this List contains all values which will be usable by this list it should
	 * by possible to be modified by all ViewElements in the client. Maybe a
	 * different position for this list is needed
	 */
	static Vector<String> POSSIBLE_VALUES = new Vector<String>();

	/*
	 * TODO: need to modify constructor so a grouping and a order become
	 * mandatory or at least set a useful default value (filter maybe)
	 */
	private List<String> order_by = Arrays.asList(new String[0]);
	private List<String> group_by = Arrays.asList(new String[0]);
	private List<String> query_fields = Arrays.asList(new String[0]);

	/**
	 * I have no idea what that stupid thing is for...
	 */
	private static final long serialVersionUID = -4008765284221017544L;

	/**
	 * Specifies the XMMS2 client (the instance of the object not THIS client
	 * ... I didn't name it ...)
	 */
	private Client client;

	private FooInterfaceViewElement view;

	/**
	 * This String is used to specify the displayed text in the list
	 * 
	 * currently the possibles values are: %artist%, %album%, %title%
	 * 
	 * i.e.: %artist% by %album%
	 */
	private String format;

	/**
	 * This String is used to specify the value which will be filtered by this
	 * List
	 * 
	 * currently possible values are: artist, album, title
	 * 
	 * i.e.: album
	 */
	private String filter;

	private CollectionExpression baseConetent;
	private CollectionExpression filteredConetent;

	// TODO: remove this dirty hack
	private boolean playlist_mode;

	private List<Dict> baseDatabase = Arrays.asList(new Dict[0]);

	/**
	 * This is the Backend which provides the baseContent.
	 * 
	 */
	private FooPluginBackendBase contentProvider;

	/**
	 * createContent converts a List of Dicts to a Vector of Strings using a
	 * pattern specified in format.
	 * 
	 * @param Database
	 *            form which the List (returned by executeCommand) will be
	 *            generated.
	 * @return A vector containing the Strings as specified in the String format
	 */
	private Vector<String> createContent(List<Dict> Database) {
		Vector<String> Content = new Vector<String>();

		// TODO: fix this hell of a hack
		if (playlist_mode) {
			FooPluginBackendPlaylist hack = (FooPluginBackendPlaylist) contentProvider;

			List<Integer> ids = hack.getPlayListOrder();

			try {

				for (int id : ids) {

					CollectionBuilder cb = new CollectionBuilder();
					cb.setType(CollectionType.IDLIST);
					cb.addId(id);

					Command c = Collection.query(new InfoQuery(cb.build(), 0,
							0, Arrays.asList(new String[0]), query_fields,
							Arrays.asList(new String[0])));

					List<Dict> all = c.executeSync(client);

					for (Dict token : all) {
						Content.add(createTokenString(format, token));
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			for (Dict token : Database) {
				Content.add(createTokenString(format, token));
			}
		}

		return Content;
	}

	/**
	 * createTokenString creates a String from the values in Dict by iterating
	 * through all values in query_fields and replace all occurrences of that
	 * String in the format String
	 * 
	 * @param format
	 * @param token
	 * @return
	 */
	private String createTokenString(String format, Dict token) {
		/*
		 * replace everything that stands between %% with the matching part of
		 * the Dict
		 */
		String tokenString = format;
		String current;

		for (String match : query_fields) {
			if (token.get(match) == null) {
				current = new String("no " + match);
			} else {
				current = token.get(match).toString();
			}

			tokenString = tokenString.replaceAll("%" + match + "%", current);

		}

		return tokenString;
	}

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

			for (int id : indices) {
				Dict match = baseDatabase.get(id);
				Object result = match.get(filter);
				if (result != null) {
					operand.clear();
					operand.setType(CollectionType.EQUALS);
					operand.addAttribute("field", filter);
					operand.addAttribute("value", result.toString());
					operand.addOp(baseConetent);
					ops.add(operand.build());
				}
			}

			CollectionBuilder master = new CollectionBuilder();
			master.setType(CollectionType.UNION);

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
		if (baseConetent != null) {

			Command c = Collection.query(new InfoQuery(baseConetent, 0, 0,
					order_by, query_fields, group_by));
			try {
				List<Dict> list = c.executeSync(client);
				this.setBaseDatabase(list);
			} catch (CommandErrorException e) {
				// TODO: this would throw an error if called if no entry in
				// field
				this.setBaseDatabase(Arrays.asList(new Dict[0]));
			}

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

		Vector<String> content = createContent(baseDatabase);

		view.setContent(content);

		setChanged();
		notifyObservers();

		/*
		 * if (next != null) { next.getBackend().refresh(); }
		 */
	}

	/**
	 * setToAll sets the baseContent to all media in the medialib
	 */
	public void setToAll() {
		this.setBaseConetent(CollectionBuilder.getAllMediaReference());
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
	public FooPluginBackendMedia(String format, String filter, Client client,
			FooInterfaceViewElement view) {
		this.view = view;
		this.setFilter(filter);
		this.setFormat(format);
		this.baseConetent = CollectionBuilder.getEmptyExpression();
		this.setClient(client);
		this.setOrderBy(query_fields);
		// this.setOrderBy(Arrays.asList(new String[] { filter }));
	}

	public void evaluateFields(String format) {
		// TODO: need to find a better way to do this! Don't understand regex
		// that well
		Vector<String> possible = new Vector<String>();
		boolean found = false;
		StringBuffer temp = new StringBuffer();

		for (int i = 0; i < format.length(); i++) {
			if (format.charAt(i) == '%') {
				found = !found;
				if (!found) {
					possible.add(temp.toString());
					temp = new StringBuffer();
				}
				i++;
			}
			if (found) {
				temp.append(format.charAt(i));
			}
		}
		FooPluginBackendMedia.POSSIBLE_VALUES.addAll(possible);
	}

	/**
	 * setter function for the String format. It sets the format String and
	 * parses all placeholders from it to the List of query_fields
	 * 
	 * @param format
	 */
	public void setFormat(String format) {
		Vector<String> newQuery = new Vector<String>();

		evaluateFields(format);

		for (String match : POSSIBLE_VALUES) {
			if (format.contains("%" + match + "%")) {
				newQuery.add(match);
			}
		}
		this.format = format;
		setQueryFields(newQuery);
	}

	/**
	 * getter function for the String format
	 * 
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * getter function for the CollectionExpression baseContent
	 * 
	 * @return
	 */
	public CollectionExpression getBaseConetent() {
		return baseConetent;
	}

	/**
	 * setter function for the CollectionExpression baseContent
	 * 
	 * @param baseConetent
	 */
	public void setBaseConetent(CollectionExpression baseConetent) {
		this.baseConetent = baseConetent;
		refresh();
		generateFilteredContent();
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

	/**
	 * getter function for the String-List order_by
	 * 
	 * @return
	 */
	public List<String> getOrderBy() {
		return order_by;
	}

	/**
	 * setter function for the String-List order_by
	 * 
	 * @param order_by
	 */
	public void setOrderBy(List<String> order_by) {
		this.order_by = order_by;
	}

	/**
	 * getter function for the String-List goup_by
	 * 
	 * @return
	 */
	public List<String> getGroupBy() {
		return group_by;
	}

	/**
	 * setter function for the String-List group_by
	 * 
	 * @param group_by
	 */
	public void setGroupBy(List<String> group_by) {
		this.group_by = group_by;
	}

	/**
	 * getter function for the String-List query_fields
	 * 
	 * @return
	 */
	public List<String> getQueryFields() {
		return query_fields;
	}

	/**
	 * setter function for the String-List query_fields
	 * 
	 * @param query_fields
	 */
	public void setQueryFields(List<String> query_fields) {
		this.query_fields = query_fields;
	}

	/**
	 * setter function for the String filter
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * getter function for the String filter
	 * 
	 * @return
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * setter function for the Dict-List baseDatabase
	 * 
	 * @param baseDatabase
	 */
	public void setBaseDatabase(List<Dict> baseDatabase) {
		this.baseDatabase = baseDatabase;
	}

	/**
	 * getter function for the Dict-List baseDatabase
	 * 
	 * @return
	 */
	public List<Dict> getBaseDatabase() {
		return baseDatabase;
	}

	/**
	 * setter function for the FooInterfaceViewElement view
	 * 
	 * @param baseDatabase
	 */
	public void setView(FooInterfaceViewElement view) {
		this.view = view;
	}

	/**
	 * getter function for the FooInterfaceViewElement view
	 * 
	 * @return
	 */
	public FooInterfaceViewElement getView() {
		return view;
	}

	@Override
	public void enqueuSelection() {
		Command c = Playlist.insert(Playlist.ACTIVE_PLAYLIST,
				getFilteredConetent(), 0);
		try {
			c.executeSync(client);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void playSelection() {

		int[] ids = view.getIndices();

		if (ids.length == 1) {

			Command cs = Playlist.setPos(ids[0]);
			Command cp = Playback.tickle();
			try {

				cs.executeSync(client);
				cp.executeSync(client);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
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
		this.setBaseConetent(contentProvider.getFilteredConetent());
	}

	public void setPlaylist_mode(boolean playlist_mode) {
		this.playlist_mode = playlist_mode;
	}

	public boolean isPlaylist_mode() {
		return playlist_mode;
	}
}
