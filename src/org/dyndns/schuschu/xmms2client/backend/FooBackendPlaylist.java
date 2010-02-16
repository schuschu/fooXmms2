package org.dyndns.schuschu.xmms2client.backend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.newAction.FooAction;
import org.eclipse.swt.SWT;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.Dict;
import se.fnord.xmms2.client.types.InfoQuery;

public class FooBackendPlaylist implements Serializable, FooInterfaceBackend {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name;

	private int debugForeground = SWT.COLOR_BLACK;
	private int debugBackground = SWT.COLOR_WHITE;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(getDebugForeground());
				FooDebug.setBackground(getDebugBackground());
			}
			System.out.println("debug: " + getName() + " " + message);
		}
	}

	/**
	 * this List contains all values which will be usable by this list it should
	 * by possible to be modified by all ViewElements in the client. Maybe a
	 * different position for this list is needed
	 */
	private Vector<String> possible_values = new Vector<String>();

	// TODO: find use for groups
	private List<String> order_by = Arrays.asList(new String[0]);
	private List<String> group_by = Arrays.asList(new String[0]);
	private List<String> query_fields = Arrays.asList(new String[0]);

	/**
	 * I have no idea what that stupid thing is for...
	 */
	private static final long serialVersionUID = 6791163548568077012L;

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

	private int current = -1;
	private int currentPos = -1;

	private Vector<String> content;

	public FooBackendPlaylist(String format, String filter, Client client,
			FooInterfaceViewElement view) {
		debug("FooBackendPlaylist");
		this.view = view;
		this.setFilter(filter);
		this.setFormat(format);
		this.setClient(client);

		// get current track
		try {
			Command init = Playback.currentId();
			int current = init.executeSync(client);
			this.current = current;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}

	public void playSelection() {
		debug("playSelection");

		int[] ids = getView().getIndices();

		if (ids.length == 1) {

			Command cs = Playlist.setPos(ids[0]);
			Command ct = Playback.tickle();
			Command cp = Playback.play();
			try {

				cs.executeSync(client);
				ct.executeSync(client);
				cp.executeSync(client);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}
	}

	public void removeSelection() {
		debug("removeSelection");

		int[] ids = getView().getIndices();
		if (ids.length > 0) {
			Command c = Playlist.removeEntries(Playlist.ACTIVE_PLAYLIST, ids);
			c.execute(client);

		}
	}

	private List<Integer> getPlaylistIds(Client client)
			throws InterruptedException, CommandErrorException {
		Command command = Playlist.listEntries(Playlist.ACTIVE_PLAYLIST);

		return command.executeSync(client);
	}

	private Map<Integer, Dict> getTrackInfo(Client client, List<Integer> ids)
			throws InterruptedException, CommandErrorException {
		CollectionBuilder builder = new CollectionBuilder();

		builder.setType(CollectionType.IDLIST);
		builder.addIds(ids);

		List<String> temp_query = query_fields;
		if (!temp_query.contains("id")) {
			temp_query.add("id");
		}

		InfoQuery query = new InfoQuery(builder.build(), 0, 0, Arrays
				.asList(new String[0]), temp_query, Arrays
				.asList(new String[0]));

		Command command = se.fnord.xmms2.client.commands.Collection
				.query(query);

		List<Dict> list = command.executeSync(client);

		Map<Integer, Dict> trackMap = new HashMap<Integer, Dict>();
		for (Dict track : list) {
			trackMap.put((Integer) track.get("id"), track);
		}

		return trackMap;
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
	protected String createTokenString(String format, Dict token) {
		debug("createTokenString");
		/*
		 * replace everything that stands between %% with the matching part of
		 * the Dict
		 */
		String tokenString = format;
		String current;

		for (String match : query_fields) {
			if (token == null) {
				return "error";
			}
			if (token.get(match) == null) {
				current = new String("no " + match);
			} else {
				current = token.get(match).toString();
			}

			tokenString = tokenString.replaceAll("%" + match + "%", current);

		}

		return tokenString;
	}

	protected Vector<String> createContent() {
		debug("createContent");
		Vector<String> Content = new Vector<String>();

		currentPos = -1;

		try {
			List<Integer> ids = getPlaylistIds(client);
			Map<Integer, Dict> tracks = getTrackInfo(client, ids);
			int i = 0;
			for (Integer id : ids) {

				if (id == current) {
					currentPos = i;
				}

				Dict track = tracks.get(id);
				Content.add(createTokenString(format, track));

				i++;
			}
		} catch (CommandErrorException e) {
			// TODO: think about this
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return Content;
	}

	@Override
	public void selectionChanged() {
		debug("selectionChanged");
		refresh();
	}

	@Override
	public void setCurrent(int current) {
		debug("setCurrent");
		this.current = current;

		try {
			List<Integer> ids = getPlaylistIds(client);
			for (int i = 0; i < ids.size(); i++) {

				if (ids.get(i) == current) {
					currentPos = i;
				}
			}
		} catch (CommandErrorException e) {
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		updatePos();
	}

	@Override
	public void refresh() {
		debug("refresh");

		content = createContent();

		view.setContent(content);
		updatePos();
	}

	public void updatePos() {
		debug("updatePos");
		view.highlight();
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

	/**
	 * getter function for the String-List order_by
	 * 
	 * @return
	 */
	public List<String> getOrderBy() {
		debug("getOrderBy");
		return order_by;
	}

	/**
	 * setter function for the String-List order_by
	 * 
	 * @param order_by
	 */
	public void setOrderBy(List<String> order_by) {
		debug("setOrderBy");
		this.order_by = order_by;
	}

	/**
	 * getter function for the String-List goup_by
	 * 
	 * @return
	 */
	public List<String> getGroupBy() {
		debug("getGroupBy");
		return group_by;
	}

	/**
	 * setter function for the String-List group_by
	 * 
	 * @param group_by
	 */
	public void setGroupBy(List<String> group_by) {
		debug("setGroupBy");
		this.group_by = group_by;
	}

	/**
	 * getter function for the String-List query_fields
	 * 
	 * @return
	 */
	public List<String> getQueryFields() {
		debug("getQueryFields");
		return query_fields;
	}

	/**
	 * setter function for the String-List query_fields
	 * 
	 * @param query_fields
	 */
	public void setQueryFields(List<String> query_fields) {
		debug("setQueryFields");
		this.query_fields = query_fields;
	}

	/**
	 * setter function for the String filter
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		debug("setFilter");
		this.filter = filter;
	}

	/**
	 * getter function for the String filter
	 * 
	 * @return
	 */
	public String getFilter() {
		debug("getFilter");
		return filter;
	}

	/**
	 * setter function for the FooInterfaceViewElement view
	 * 
	 * @param baseDatabase
	 */
	public void setView(FooInterfaceViewElement view) {
		debug("setView");
		this.view = view;
	}

	/**
	 * getter function for the FooInterfaceViewElement view
	 * 
	 * @return
	 */
	public FooInterfaceViewElement getView() {
		debug("getView");
		return view;
	}

	public void enqueuSelection() {
		debug("enqueuSelection");
		Command c = Playlist.insert(Playlist.ACTIVE_PLAYLIST,
				getFilteredConetent(), 0);
		try {
			c.executeSync(client);
		} catch (InterruptedException e1) {
			Thread.currentThread().interrupt();
		}
	}

	public int getCurrent() {
		debug("getCurrent");
		return current;
	}

	public void setCurrentPos(int currentPos) {
		debug("setCurrentPos");
		this.currentPos = currentPos;
	}

	public int getCurrentPos() {
		debug("getCurrentPos");
		return currentPos;
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

	public void setDebugForeground(int debugForeground) {
		this.debugForeground = debugForeground;
	}

	public int getDebugForeground() {
		return debugForeground;
	}

	public void setDebugBackground(int debugBackground) {
		this.debugBackground = debugBackground;
	}

	public int getDebugBackground() {
		return debugBackground;
	}

	public void evaluateFields(String format) {
		debug("evaluateFields");
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

		possible_values = possible;
	}

	/**
	 * setter function for the String format. It sets the format String and
	 * parses all placeholders from it to the List of query_fields
	 * 
	 * @param format
	 */
	public void setFormat(String format) {
		debug("setFormat");
		Vector<String> newQuery = new Vector<String>();

		evaluateFields(format);

		for (String match : possible_values) {
			if (format.contains("%" + match + "%")) {
				newQuery.add(match);
			}
		}
		this.format = format;

		setQueryFields(newQuery);
		setOrderBy(newQuery);
	}

	/**
	 * getter function for the String format
	 * 
	 * @return
	 */
	public String getFormat() {
		debug("getFormat");
		return format;
	}

	@Override
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateFilteredContent() {
		// TODO Auto-generated method stub

	}

	@Override
	public FooInterfaceBackend getContentProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollectionExpression getFilteredConetent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContentProvider(FooInterfaceBackend contentProvider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setToAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	/*
	 * ACTION SECTION
	 */

	public FooAction ActionPlay(int code) {
		return new ActionPlay(code, this);
	}

	public class ActionPlay extends FooAction {

		private final FooBackendPlaylist backend;

		public ActionPlay(int code, FooBackendPlaylist backend) {
			super(code);
			this.backend = backend;
		}

		@Override
		public void execute() {
			backend.playSelection();
		}

	}

	public FooAction ActionDeselect(int code) {
		return new ActionDeselect(code, this);
	}

	private class ActionDeselect extends FooAction {

		private final FooInterfaceBackend backend;

		public ActionDeselect(int code, FooInterfaceBackend backend) {
			super(code);
			this.backend = backend;
		}

		@Override
		public void execute() {
			backend.getView().setSelection(new int[0]);
			backend.selectionChanged();
		}

	}

	public FooAction ActionRemove(int code) {
		return new ActionRemove(code, this);
	}

	public class ActionRemove extends FooAction {

		private final FooBackendPlaylist backend;

		public ActionRemove(int code, FooBackendPlaylist backend) {
			super(code);
			this.backend = backend;
		}

		@Override
		public void execute() {
			backend.removeSelection();
		}

	}

}
