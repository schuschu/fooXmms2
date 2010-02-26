package org.dyndns.schuschu.xmms2client.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.action.base.FooKey;
import org.dyndns.schuschu.xmms2client.action.base.FooSource;
import org.dyndns.schuschu.xmms2client.factories.FooActionFactory;
import org.dyndns.schuschu.xmms2client.factories.FooActionFactorySub;
import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.view.dialog.FooComboDialog;
import org.dyndns.schuschu.xmms2client.view.dialog.FooInputDialog;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionNamespace;

public class FooPlaylist  {

	public static void registerFactory() {
		// ACTION
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
				case delete:
					action= ActionDelete(code); break;
				case newlist:
					action= ActionNew(code); break;
				case save:
					action= ActionSava(code); break;
				case shuffle:
					action= ActionShuffle(code); break;
				case sort:
					action= ActionSort(code); break;
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
		FooActionFactory.factories.put("Playlist", factory);
	}

	private enum ActionType {
		delete, newlist, save, shuffle, sort;
	}

	public static FooAction ActionDelete(int code) {
		return new ActionPlaylist(code, "delete", PlaylistType.DELETE);
	}

	public static FooAction ActionNew(int code) {
		return new ActionPlaylist(code, "new", PlaylistType.NEW);
	}

	public static FooAction ActionSava(int code) {
		return new ActionPlaylist(code, "save", PlaylistType.SAVE);
	}

	public static FooAction ActionShuffle(int code) {
		return new ActionPlaylist(code, "shuffle", PlaylistType.SHUFFLE);
	}

	public static FooAction ActionSort(int code) {
		return new ActionPlaylist(code, "sort", PlaylistType.SORT);
	}

	private static class ActionPlaylist extends FooAction {

		private final PlaylistType type;

		public ActionPlaylist(int code, String name, PlaylistType type) {
			super(name, code);
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
						.executeSync(FooLoader.CLIENT);

				Vector<String> content = new Vector<String>();

				for (Set<String> names : map.values()) {
					for (String name : names) {
						if (!name.startsWith("_")) {
							content.add(name);
						}
					}
				}

				String[] values = new String[content.size()];
				content.toArray(values);

				input = FooComboDialog.show(FooWindow.SHELL.getShell(),
						"Please choose the playlist you want to delete",
						"delete playlist", values);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			if (input != null) {
				Command c = Collection.remove(CollectionNamespace.PLAYLISTS,
						input);
				c.execute(FooLoader.CLIENT);
			}

		}

		private void newlist() {

			String input = FooInputDialog.show(FooWindow.SHELL,
					"Please enter the name of the new playlist",
					"create playlist");

			if (input != null) {

				Command c = Collection.save(CollectionNamespace.PLAYLISTS,
						input, CollectionBuilder.getEmptyExpression());

				c.execute(FooLoader.CLIENT);
			}
		}

		private void save() {

			String input = FooInputDialog.show(FooWindow.SHELL,
					"Please enter the name of the new playlist",
					"save playlist");

			if (input != null) {

				try {
					Command command = Playlist
							.listEntries(Playlist.ACTIVE_PLAYLIST);

					List<Integer> ids = command.executeSync(FooLoader.CLIENT);

					CollectionBuilder cb = new CollectionBuilder();
					cb.addIds(ids);
					CollectionExpression ce = cb.build();

					Command c = Collection.save(CollectionNamespace.PLAYLISTS,
							input, ce);

					c.execute(FooLoader.CLIENT);

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

		private void shuffle() {
			Command c = Playlist.shuffle(Playlist.ACTIVE_PLAYLIST);
			c.execute(FooLoader.CLIENT);
		}

		private void sort() {

			String input = FooInputDialog.show(FooWindow.SHELL,
					"Please enter sort order:\n(i.e.: artist album title)",
					"change order");

			if (input != null) {
				List<String> properties = Arrays.asList(input.split(" "));
				Command c = Playlist.sort(Playlist.ACTIVE_PLAYLIST, properties);
				c.execute(FooLoader.CLIENT);
			}
		}
	}

}

enum PlaylistType {

	DELETE, NEW, SAVE, SHUFFLE, SORT;

}
