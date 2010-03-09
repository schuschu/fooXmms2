package org.dyndns.schuschu.xmms2client.backend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceDebug;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceView;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionNamespace;

/**
 * @author schuschu
 * 
 */
public class FooBackendPlaylistSwitch extends Observable implements
		Serializable, FooInterfaceBackend,FooInterfaceDebug {

	private static final boolean DEBUG = FooLoader.getBooleanArg("debug");
	private String name;

	private FooColor debugForeground = FooColor.BLACK;
	private FooColor debugBackground = FooColor.WHITE;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.DOUTPUT!=null) {
				FooLoader.DOUTPUT.setForeground(getDebugForeground());
				FooLoader.DOUTPUT.setBackground(getDebugBackground());
			}
			System.out.println("debug: " + getName() + " " + message);
		}
	}

	/**
	 * I have no idea what that stupid thing is for...
	 */
	private static final long serialVersionUID = -2744778880227415342L;

	private FooInterfaceView view;

	private String[] playlistDatabase;

	private Vector<String> content;

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

		Map<CollectionNamespace, Set<String>> map = c
				.executeSync(FooLoader.CLIENT);

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

		getView()
				.setContent(new Vector<String>(Arrays
						.asList(getPlaylistDatabase())));

		try {
			getView().setSelection(new int[] { getActivePlaylistId() });
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		setChanged();
		notifyObservers();
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
	public FooBackendPlaylistSwitch(FooInterfaceView view) {
		debug("FooBackendPlaylistSwitch");
		this.setView(view);
		this.playlistDatabase = null;

		refresh();

		try {
			view.setSelection(new int[] { getActivePlaylistId() });
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}

	public int getActivePlaylistId() throws InterruptedException {
		debug("getActivePlaylistId");

		Command c = Playlist.currentActive();

		String name = c.executeSync(FooLoader.CLIENT);

		for (int i = 0; i < playlistDatabase.length; i++) {
			if (name.equals(playlistDatabase[i])) {
				return i;
			}
		}
		// you should NEVER get here!
		return 0;
	}

	public void loadPlaylist() {
		debug("loadPlaylist");

		int selection = getView().getIndices()[0];

		if (selection >= 0) {

			Command c = Playlist.load(playlistDatabase[selection]);

			try {
				c.executeSync(FooLoader.CLIENT);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
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
	public FooInterfaceView getView() {
		debug("getView");
		return view;
	}

	@Override
	public void selectionChanged() {
		debug("selectionChanged");
		loadPlaylist();
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

	public void setView(FooInterfaceView view) {
		debug("setView");
		this.view = view;
		view.setBackend(this);
	}
	
	public static void registerFactory(){
		//BACKEND
		
		FooFactorySub factory = new FooFactorySub() {
			
			@Override
			public Object create(Element element) {

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for view (since backends are always direct
				// below (hirachical) their view element)
				Element father = (Element) element.getParentNode();
				String view = father.getAttribute("name");

				debug("creating FooBackendPlaylistSwitch " + name);
				
				FooBackendPlaylistSwitch playlistSwitchBackend = new FooBackendPlaylistSwitch(
						getView(view));
				playlistSwitchBackend.setName(name);

				FooFactory.putBackend(name, playlistSwitchBackend);
				return playlistSwitchBackend;

			}
			
			private FooInterfaceView getView(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceView) {
					return (FooInterfaceView) o;
				}
				return null;
			}
		};
		
		FooFactory.factories.put("FooBackendPlaylistSwitch", factory);
	}
}
