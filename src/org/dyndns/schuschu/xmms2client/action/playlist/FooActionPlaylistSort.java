package org.dyndns.schuschu.xmms2client.action.playlist;

import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;

public class FooActionPlaylistSort extends FooActionPlaylist {

	public FooActionPlaylistSort(FooInterfaceClickable clickable, Client client) {
		super(clickable, client);
	}

	@Override
	public void clicked() {
		String input = JOptionPane
				.showInputDialog("Please enter sort order:\n(i.e.: artist album title");

		if (input != null) {
			List<String> properties = Arrays.asList(input.split(" "));
			Command c = Playlist.sort(Playlist.ACTIVE_PLAYLIST, properties);
			c.execute(getClient());
		}
	}

}
