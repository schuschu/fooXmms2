package org.dyndns.schuschu.xmms2client.backend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceView;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionNamespace;

/**
 * @author schuschu
 * 
 */
public class FooBackendPlaylistSwitch extends Observable implements
		Serializable, FooInterfaceBackend {

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
				.executeSync(FooLoader.client);

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
	public FooBackendPlaylistSwitch(FooInterfaceView view) {
		debug("FooBackendPlaylistSwitch");
		this.view = view;
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

		String name = c.executeSync(FooLoader.client);

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

		int selection = view.getIndices()[0];

		if (selection >= 0) {

			Command c = Playlist.load(playlistDatabase[selection]);

			try {
				c.executeSync(FooLoader.client);
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
}
