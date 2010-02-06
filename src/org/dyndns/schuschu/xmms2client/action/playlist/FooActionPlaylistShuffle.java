package org.dyndns.schuschu.xmms2client.action.playlist;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;

public class FooActionPlaylistShuffle extends FooActionPlaylist {

	public FooActionPlaylistShuffle(FooInterfaceClickable clickable,
			Client client) {
		super(clickable, client);
	}

	@Override
	public void clicked() {

		Command c = Playlist.shuffle(Playlist.ACTIVE_PLAYLIST);
		c.execute(getClient());
		// TODO: Broadcast here

	}

}
