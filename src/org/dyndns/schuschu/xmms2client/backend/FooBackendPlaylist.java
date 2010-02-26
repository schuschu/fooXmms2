package org.dyndns.schuschu.xmms2client.backend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.action.base.FooKey;
import org.dyndns.schuschu.xmms2client.action.base.FooSource;
import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackendPlaylist;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceViewPlaylist;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.view.dialog.FooInputDialog;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.Dict;
import se.fnord.xmms2.client.types.InfoQuery;

public class FooBackendPlaylist implements Serializable,
		FooInterfaceBackendPlaylist {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name;

	private FooColor debugForeground = FooColor.BLACK;
	private FooColor debugBackground = FooColor.WHITE;

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
	 * by possible to be modified by all ViewElements in the CLIENT. Maybe a
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

	private FooInterfaceViewPlaylist view;

	/**
	 * This String is used to specify the displayed text in the list
	 * 
	 * currently the possibles values are: %artist%, %album%, %title%
	 * 
	 * i.e.: %artist% by %album%
	 */
	private String format;

	private int current = -1;
	private int currentPos = -1;

	private Vector<String> content;

	public FooBackendPlaylist(String format, FooInterfaceViewPlaylist view) {
		debug("FooBackendPlaylist");
		this.setView(view);
		this.setFormat(format);
		refresh();

	}

	public void playSelection() {
		debug("playSelection");

		int[] ids = getView().getIndices();

		if (ids.length == 1) {

			Command cs = Playlist.setPos(ids[0]);
			Command ct = Playback.tickle();
			Command cp = Playback.play();
			try {

				cs.executeSync(FooLoader.CLIENT);
				ct.executeSync(FooLoader.CLIENT);
				cp.executeSync(FooLoader.CLIENT);

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
			c.execute(FooLoader.CLIENT);

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
			List<Integer> ids = getPlaylistIds(FooLoader.CLIENT);
			Map<Integer, Dict> tracks = getTrackInfo(FooLoader.CLIENT, ids);
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
	}

	@Override
	public void setCurrent(int current) {
		debug("setCurrent");
		this.current = current;

		try {
			List<Integer> ids = getPlaylistIds(FooLoader.CLIENT);
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
		view.highlight(getCurrentPos());
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
	 * setter function for the FooInterfaceView view
	 * 
	 * @param baseDatabase
	 */
	public void setView(FooInterfaceViewPlaylist view) {
		debug("setView");
		this.view = view;
		view.setBackend(this);
	}

	/**
	 * getter function for the FooInterfaceView view
	 * 
	 * @return
	 */
	public FooInterfaceViewPlaylist getView() {
		debug("getView");
		return view;
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

	public void setDebugForeground(FooColor debugForeground) {
		this.debugForeground = debugForeground;
	}

	public FooColor getDebugForeground() {
		return debugForeground;
	}

	public void setDebugBackground(FooColor debugBackground) {
		this.debugBackground = debugBackground;
	}

	public FooColor getDebugBackground() {
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

	public static void registerFactory() {
		// BACKEND

		FooFactorySub factory = new FooFactorySub() {

			@Override
			protected Object create(Element element) {

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// TODO: docu
				// format (so documentation for further infos), no default
				String format = element.getAttribute("format");

				// TODO: think about these
				String debugForeground = FooXML.getTagValue("debugfg", element);
				String debugBackground = FooXML.getTagValue("debugbg", element);

				// get the parent nodes name for view (since backends are always
				// direct
				// below (hirachical) their view element)
				Element father = (Element) element.getParentNode();
				String view = father.getAttribute("name");

				debug("creating FooBackendPlaylist " + name);

				FooBackendPlaylist playlistBackend = new FooBackendPlaylist(
						format, getViewPlaylist(view));
				playlistBackend.setName(name);
				playlistBackend.setDebugForeground(FooColor
						.valueOf(debugForeground));
				playlistBackend.setDebugBackground(FooColor
						.valueOf(debugBackground));

				playlistBackend.registerActionFactory();

				FooFactory.putBackend(name, playlistBackend);
				return playlistBackend;

			}

			private FooInterfaceViewPlaylist getViewPlaylist(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceViewPlaylist) {
					return (FooInterfaceViewPlaylist) o;
				}
				return null;
			}

		};

		FooFactory.factories.put("FooBackendPlaylist", factory);

	}

	/*
	 * ACTION SECTION
	 */

	// TODO: move FooPlaylist actions here

	public void registerActionFactory() {
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public FooAction create(Element element) {
				
				// the name of the action within the backend , no default possible
				String name = element.getAttribute("name");

				// Source of the event that triggers the event, default is KEYBOARD
				String sourcestring = element.hasAttribute("source") ? element
						.getAttribute("source") : "KEYBOARD";
				FooSource source = FooSource.valueOf(sourcestring);

				// TODO: mousecode
				// Code (keycode, mousecode) that triggers the event, default is NONE
				String codestring = element.hasAttribute("code") ? element
						.getAttribute("code") : "NONE";

				int code = 0;
				switch (source) {
				case MOUSE:
					code = Integer.parseInt(codestring);
					break;
				case KEYBOARD:
					code = FooKey.valueOf(codestring).getCode();
					break;
				}

				// get the parent nodes name for view (since actions are always direct
				// below (hirachical) their view element)
				Element father = (Element) element.getParentNode();
				String viewstring = father.getAttribute("name");
				FooInterfaceAction view = getView(viewstring);
				
				FooAction action=null;

				switch (ActionType.valueOf(name)) {
				case play:
					action= ActionPlay(code); break;
				case deselect:
					action= ActionDeselect(code); break;
				case format:
					action= ActionFormat(code); break;
				case remove:
					action= ActionRemove(code); break;
				}
				
				view.addAction(source, action);
				return action;
			}
			
			private FooInterfaceAction getView(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceAction) {
					return (FooInterfaceAction) o;
				}
				return null;
			}
		};

		FooFactory.factories.put(name, factory);
	}

	private enum ActionType {
		play, deselect, remove, format;
	}

	public FooAction ActionPlay(int code) {
		return new ActionPlay(code, this);
	}

	public class ActionPlay extends FooAction {

		private final FooBackendPlaylist backend;

		public ActionPlay(int code, FooBackendPlaylist backend) {
			super("play", code);
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
			super("deselect", code);
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
			super("remove", code);
			this.backend = backend;
		}

		@Override
		public void execute() {
			backend.removeSelection();
		}
	}

	public FooAction ActionFormat(int code) {
		return new ActionFormat(code, this);
	}

	public class ActionFormat extends FooAction {

		private final FooBackendPlaylist backend;

		public ActionFormat(int code, FooBackendPlaylist backend) {
			super("format", code);
			this.backend = backend;
		}

		@Override
		public void execute() {
			String current = backend.getFormat();

			String input = FooInputDialog
					.show(
							FooWindow.SHELL,
							"Please enter new format:\n(i.e.: %artist% - %album%: %title% ",
							"change format", current);

			if (input != null) {
				backend.setFormat(input);
				backend.getView().setSelection(new int[] { -1 });
				backend.refresh();
			}
		}
	}
}
