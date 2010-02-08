package org.dyndns.schuschu.xmms2client.action.playlist;

import java.util.List;

import javax.swing.JOptionPane;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionNamespace;

public class FooActionPlaylistSave extends FooActionPlaylist {

	public FooActionPlaylistSave(FooInterfaceClickable clickable, Client client) {
		super(clickable, client);
	}

	@Override
	public void clicked() {
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
				// TODO: Broadcast here

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}

		}
	}

}
