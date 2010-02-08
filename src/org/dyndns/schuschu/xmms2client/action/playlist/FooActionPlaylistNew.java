package org.dyndns.schuschu.xmms2client.action.playlist;

import javax.swing.JOptionPane;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;

public class FooActionPlaylistNew extends FooActionPlaylist {

	public FooActionPlaylistNew(FooInterfaceClickable clickable, Client client) {
		super(clickable, client);
	}

	@Override
	public void clicked() {
		String input = JOptionPane
				.showInputDialog("Please enter the name of the new playlist");

		if (input != null) {
			Command c = Playlist.save(input, CollectionBuilder
					.getEmptyExpression());

			c.execute(getClient());
			// TODO: Broadcast here
		}
	}

}
