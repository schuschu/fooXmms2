package org.dyndns.schuschu.xmms2client.action.playlist;

import javax.swing.JOptionPane;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceClickable;

import se.fnord.xmms2.client.Client;

public class FooActionPlaylistDelete extends FooActionPlaylist {

	public FooActionPlaylistDelete(FooInterfaceClickable clickable,
			Client client) {
		super(clickable, client);
	}

	@Override
	public void clicked() {
		JOptionPane.showMessageDialog(null, "not implemented yet!");
		// TODO: The command
	}

}
