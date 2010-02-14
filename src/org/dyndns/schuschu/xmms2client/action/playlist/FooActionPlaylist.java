package org.dyndns.schuschu.xmms2client.action.playlist;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionNamespace;

public class FooActionPlaylist implements FooInterfaceAction {

	private Client client;
	private Listener action;
	private FooInterfaceClickable clickClickable;
	private FooPlaylistType type;

	public FooActionPlaylist(FooPlaylistType type,
			FooInterfaceClickable clickable, Client client) {
		initialize(type, clickable, client);
	}

	public void initialize(FooPlaylistType type,
			FooInterfaceClickable clickable, Client client) {
		setType(type);
		setClickClickable(clickable);
		setClient(client);
		setAction(new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				clicked();

			}
		});
	}

	@Override
	public void addListeners() {
		clickClickable.addListener(getAction());
	}

	private void clicked() {
		switch (getType()) {
		case DELETE:
			delete();
			break;
		case NEW:
			newlist();
			break;
		case SAVE:
			save();
			break;
		case SHUFFLE:
			shuffle();
			break;
		case SORT:
			sort();
			break;
		}
	}

	private void delete() {
		Command get = Playlist.listPlaylists();

		String input = null;

		try {

			Map<CollectionNamespace, Set<String>> map = get
					.executeSync(getClient());

			Vector<String> content = new Vector<String>();

			for (Set<String> names : map.values()) {
				for (String name : names) {
					if (!name.startsWith("_")) {
						content.add(name);
					}
				}
			}

			input = (String) JOptionPane.showInputDialog(null,
					"Please choose the playlist you want to delete",
					"Delete Playlist", JOptionPane.PLAIN_MESSAGE, null, content
							.toArray(), content.get(0));

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		if (input != null) {
			Command c = Collection.remove(CollectionNamespace.PLAYLISTS, input);
			c.execute(getClient());
		}

	}

	private void newlist() {
		String input = JOptionPane
				.showInputDialog("Please enter the name of the new playlist");

		if (input != null) {

			Command c = Collection.save(CollectionNamespace.PLAYLISTS, input,
					CollectionBuilder.getEmptyExpression());

			c.execute(getClient());
		}
	}

	private void save() {
		String input = JOptionPane
				.showInputDialog("Please enter the name of the new playlist");

		if (input != null) {

			try {
				Command command = Playlist
						.listEntries(Playlist.ACTIVE_PLAYLIST);

				List<Integer> ids = command.executeSync(getClient());

				CollectionBuilder cb = new CollectionBuilder();
				cb.addIds(ids);
				CollectionExpression ce = cb.build();

				Command c = Collection.save(CollectionNamespace.PLAYLISTS,
						input, ce);

				c.execute(getClient());

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void shuffle() {
		Command c = Playlist.shuffle(Playlist.ACTIVE_PLAYLIST);
		c.execute(getClient());
	}

	private void sort() {
		String input = JOptionPane
				.showInputDialog("Please enter sort order:\n(i.e.: artist album title");

		if (input != null) {
			List<String> properties = Arrays.asList(input.split(" "));
			Command c = Playlist.sort(Playlist.ACTIVE_PLAYLIST, properties);
			c.execute(getClient());
		}
	}

	@Override
	public void removeListeners() {
		clickClickable.removeListener(getAction());

	}

	public void setAction(Listener action) {
		this.action = action;
	}

	public Listener getAction() {
		return action;
	}

	public void setClickClickable(FooInterfaceClickable clickClickable) {
		this.clickClickable = clickClickable;
	}

	public FooInterfaceClickable getClickClickable() {
		return clickClickable;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	private void setType(FooPlaylistType type) {
		this.type = type;
	}

	private FooPlaylistType getType() {
		return type;
	}

}
