package org.dyndns.schuschu.xmms2client.backend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.action.base.FooKey;
import org.dyndns.schuschu.xmms2client.action.base.FooSource;
import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.factories.FooActionFactory;
import org.dyndns.schuschu.xmms2client.factories.FooActionFactorySub;
import org.dyndns.schuschu.xmms2client.factories.FooBackendFactory;
import org.dyndns.schuschu.xmms2client.factories.FooBackendFactorySub;
import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackendFilter;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceView;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.view.dialog.FooInputDialog;
import org.dyndns.schuschu.xmms2client.view.dialog.FooMessageDialog;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
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
public class FooBackendFilter extends Observable implements Serializable,
		FooInterfaceBackendFilter {

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
	private static final long serialVersionUID = -4008765284221017544L;

	private FooInterfaceView view;

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

	private List<Dict> baseDatabase = Arrays.asList(new Dict[0]);

	/**
	 * This is the Backend which provides the baseContent.
	 * 
	 */
	private FooInterfaceBackendFilter contentProvider;

	private Vector<String> content;

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

		for (Dict token : Database) {
			Content.add(createTokenString(format, token));
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
	private void executeFilterCommand(int[] indices)
			throws InterruptedException {
		debug("executeFilterCommand");

		if (indices.length != 0 && indices[0] != -1) {

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
	private void executeBaseCommand() throws InterruptedException {
		debug("executeBaseCommand");
		if (baseConetent != null) {

			Command c = Collection.query(new InfoQuery(baseConetent, 0, 0,
					order_by, query_fields, group_by));
			try {
				List<Dict> list = c.executeSync(FooLoader.CLIENT);
				this.setBaseDatabase(list);
			} catch (CommandErrorException e) {
				this.setBaseDatabase(Arrays.asList(new Dict[0]));
			}

		}
	}

	/**
	 * runs executeFilterCommand which generates the filterdContent and then
	 * writes it into the FooPluginViewElementList next
	 */
	public void generateFilteredContent() {
		debug("generateFilteredContent");
		try {
			executeFilterCommand(view.getIndices());
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

		content = createContent(baseDatabase);
		view.setContent(content);

		generateFilteredContent();
	}

	/**
	 * setToAll sets the baseContent to all media in the medialib
	 */
	public void setToAll() {
		debug("setToAll");
		this.setBaseConetent(CollectionBuilder.getAllMediaReference());
	}

	/**
	 * The one and only constructor of FooPluginViewElementList
	 * 
	 * @param format
	 *            Formating String i.e.: %album% - %artist%
	 * @param filter
	 *            Which tag should be filtered by the list
	 * @param CLIENT
	 *            the xmms2-CLIENT
	 * @param view
	 *            the view element associated with this backend (wont crunch
	 *            numbers for nothing)
	 */
	public FooBackendFilter(String format, String filter, FooInterfaceView view) {
		debug("FooBackendFilter");
		this.setView(view);
		this.setFilter(filter);
		this.setFormat(format);
		this.baseConetent = CollectionBuilder.getEmptyExpression();
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

	/**
	 * getter function for the CollectionExpression baseContent
	 * 
	 * @return
	 */
	public CollectionExpression getBaseConetent() {
		debug("getBaseConetent");
		return baseConetent;
	}

	/**
	 * setter function for the CollectionExpression baseContent
	 * 
	 * @param baseConetent
	 */
	public void setBaseConetent(CollectionExpression baseConetent) {
		debug("setBaseConetent");
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
	 * setter function for the Dict-List baseDatabase
	 * 
	 * @param baseDatabase
	 */
	public void setBaseDatabase(List<Dict> baseDatabase) {
		debug("setBaseDatabase");
		this.baseDatabase = baseDatabase;
	}

	/**
	 * getter function for the Dict-List baseDatabase
	 * 
	 * @return
	 */
	public List<Dict> getBaseDatabase() {
		debug("getBaseDatabase");
		return baseDatabase;
	}

	/**
	 * setter function for the FooInterfaceView view
	 * 
	 * @param baseDatabase
	 */
	public void setView(FooInterfaceView view) {
		debug("setView");
		this.view = view;
		view.setBackend(this);
	}

	/**
	 * getter function for the FooInterfaceView view
	 * 
	 * @return
	 */
	public FooInterfaceView getView() {
		debug("getView");
		return view;
	}

	public void enqueuSelection() {
		debug("enqueuSelection");
		CollectionExpression filtered = getFilteredConetent();
		if (filtered != null) {
			Command c = Playlist.insert(Playlist.ACTIVE_PLAYLIST, filtered, 0);
			try {
				c.executeSync(FooLoader.CLIENT);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			} catch (CommandErrorException e) {
				FooMessageDialog.show(FooWindow.SHELL, "invalid selection",
						"Error");
			}
		}
	}

	@Override
	public FooInterfaceBackendFilter getContentProvider() {
		debug("getContentProvider");
		return contentProvider;
	}

	@Override
	public void setContentProvider(FooInterfaceBackendFilter contentProvider) {
		debug("setContentProvider");
		this.contentProvider = contentProvider;
		contentProvider.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		debug("update");
		this.setBaseConetent(contentProvider.getFilteredConetent());
	}

	@Override
	public void selectionChanged() {
		debug("selectionChanged");
		generateFilteredContent();
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
	
	public static void registerFactory(){
		//BACKEND
		
	FooBackendFactorySub factory = new FooBackendFactorySub() {
		
		@Override
		protected Object create(Element element) {
			
			// name equals variable name, no default
			String name = element.getAttribute("name");

			// TODO: docu
			// format (so documentation for further infos), no default
			String format = element.getAttribute("format");

			// filter defines which part of the selected items will be used to
			// filter the content for the next backend, no default
			String filter = element.getAttribute("filter");

			// defines which backend will be supplying the current backend with
			// data, default ALL (no input filtering)
			String contentprovider = element.hasAttribute("contentprovider") ? element
					.getAttribute("contentprovider")
					: "ALL";

			// TODO: think about these
			String debugForeground = FooXML.getTagValue("debugfg", element);
			String debugBackground = FooXML.getTagValue("debugbg", element);
			
			// get the parent nodes name for view (since backends are always direct
			// below (hirachical) their view element)
			Element father = (Element) element.getParentNode();
			String view = father.getAttribute("name");
			
			debug("creating FooBackendFilter " + name);

			FooBackendFilter filterBackend = new FooBackendFilter(format,
					filter, getView(view));
			filterBackend.setName(name);
			filterBackend.setDebugForeground(FooColor.valueOf(debugForeground));
			filterBackend.setDebugBackground(FooColor.valueOf(debugBackground));

			if (contentprovider.equals("ALL")) {
				filterBackend.setToAll();
			} else {
				filterBackend
						.setContentProvider((FooInterfaceBackendFilter) FooFactory
								.getBackend(contentprovider));
			}

			filterBackend.registerActionFactory();

			FooFactory.putBackend(name, filterBackend);
			return filterBackend;
		}
		
		private FooInterfaceView getView(String s) {
			Object o = FooFactory.getView(s);
			if (o instanceof FooInterfaceView) {
				return (FooInterfaceView) o;
			}
			return null;
		}
	};
	
	FooBackendFactory.factories.put("FooBackendFilter", factory);
	
	}

	/*
	 * ACTION SECTION
	 */

	public void registerActionFactory() {
		FooActionFactorySub factory = new FooActionFactorySub() {

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
				case enqueue:
					action= ActionEnqueu(code); break;
				case deselect:
					action= ActionDeselect(code); break;
				case format:
					action= ActionFormat(code); break;
				case order:
					action= ActionOrder(code); break;
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

		FooActionFactory.factories.put(name, factory);
	}

	private enum ActionType {
		enqueue, deselect, format, order;
	}

	public FooAction ActionEnqueu(int code) {
		return new ActionEnqueu(code, this);
	}

	private class ActionEnqueu extends FooAction {

		private final FooBackendFilter backend;

		public ActionEnqueu(int code, FooBackendFilter backend) {
			super("enqueu", code);
			this.backend = backend;
		}

		@Override
		public void execute() {
			backend.enqueuSelection();
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

	public FooAction ActionOrder(int code) {
		return new ActionOrder(code, this);
	}

	// TODO: rename to sort...
	public class ActionOrder extends FooAction {

		private FooBackendFilter backend;

		public ActionOrder(int code, FooBackendFilter backend) {
			super("order", code);
			this.backend = backend;
		}

		@Override
		public void execute() {
			List<String> listCurrent = backend.getOrderBy();
			StringBuffer buffer = new StringBuffer();
			for (String s : listCurrent) {
				buffer.append(s + " ");
			}
			buffer.deleteCharAt(buffer.length() - 1);
			String current = buffer.toString();

			String input = FooInputDialog.show(FooWindow.SHELL,
					"Please enter new order:\n(i.e.: artist album title",
					"change order", current);

			if (input != null) {
				List<String> newOrder = Arrays.asList(input.split(" "));
				backend.setOrderBy(newOrder);

				backend.getView().setSelection(new int[] { -1 });
				backend.refresh();
				backend.generateFilteredContent();
			}

		}

	}

	public FooAction ActionFormat(int code) {
		return new ActionFormat(code, this);
	}

	public class ActionFormat extends FooAction {

		private final FooBackendFilter backend;

		public ActionFormat(int code, FooBackendFilter backend) {
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
				backend.generateFilteredContent();
			}

		}

	}

}
