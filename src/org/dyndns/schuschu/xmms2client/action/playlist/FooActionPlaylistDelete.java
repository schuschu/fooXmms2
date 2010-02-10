package org.dyndns.schuschu.xmms2client.action.playlist;

import javax.swing.JOptionPane;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.types.CollectionNamespace;

public class FooActionPlaylistDelete extends FooActionPlaylist {

	public FooActionPlaylistDelete(FooInterfaceClickable clickable,
			Client client) {
		super(clickable, client);
	}

	@Override
	public void clicked() {
		// TODO: Dropdown dialog

		String input = JOptionPane
				.showInputDialog("Please enter the name of the playlist you want to delete");

		if (input != null) {

			Command c = Collection.remove(CollectionNamespace.PLAYLISTS,input);

			c.execute(getClient());
		}
	}
}
