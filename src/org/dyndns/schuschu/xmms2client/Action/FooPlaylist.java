package org.dyndns.schuschu.xmms2client.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionNamespace;

public class FooPlaylist {

	public static FooAction ActionDelete(int code) {
		return new ActionPlaylist(code, PlaylistType.DELETE);
	}

	public static FooAction ActionNew(int code) {
		return new ActionPlaylist(code, PlaylistType.NEW);
	}

	public static FooAction ActionSava(int code) {
		return new ActionPlaylist(code, PlaylistType.SAVE);
	}

	public static FooAction ActionShuffle(int code) {
		return new ActionPlaylist(code, PlaylistType.SHUFFLE);
	}

	public static FooAction ActionSort(int code) {
		return new ActionPlaylist(code, PlaylistType.SORT);
	}

	private static class ActionPlaylist extends FooAction {

		private final PlaylistType type;

		public ActionPlaylist(int code, PlaylistType type) {
			super(code);
			this.type = type;
		}

		@Override
		public void execute() {
			switch (type) {
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
						.executeSync(FooLoader.client);

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
						"Delete Playlist", JOptionPane.PLAIN_MESSAGE, null,
						content.toArray(), content.get(0));

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			if (input != null) {
				Command c = Collection.remove(CollectionNamespace.PLAYLISTS,
						input);
				c.execute(FooLoader.client);
			}

		}

		private void newlist() {
			String input = JOptionPane
					.showInputDialog("Please enter the name of the new playlist");

			if (input != null) {

				Command c = Collection.save(CollectionNamespace.PLAYLISTS,
						input, CollectionBuilder.getEmptyExpression());

				c.execute(FooLoader.client);
			}
		}

		private void save() {
			String input = JOptionPane
					.showInputDialog("Please enter the name of the new playlist");

			if (input != null) {

				try {
					Command command = Playlist
							.listEntries(Playlist.ACTIVE_PLAYLIST);

					List<Integer> ids = command.executeSync(FooLoader.client);

					CollectionBuilder cb = new CollectionBuilder();
					cb.addIds(ids);
					CollectionExpression ce = cb.build();

					Command c = Collection.save(CollectionNamespace.PLAYLISTS,
							input, ce);

					c.execute(FooLoader.client);

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

		private void shuffle() {
			Command c = Playlist.shuffle(Playlist.ACTIVE_PLAYLIST);
			c.execute(FooLoader.client);
		}

		private void sort() {
			String input = JOptionPane
					.showInputDialog("Please enter sort order:\n(i.e.: artist album title");

			if (input != null) {
				List<String> properties = Arrays.asList(input.split(" "));
				Command c = Playlist.sort(Playlist.ACTIVE_PLAYLIST, properties);
				c.execute(FooLoader.client);
			}
		}

	}

}

enum PlaylistType {

	DELETE, NEW, SAVE, SHUFFLE, SORT;

}
