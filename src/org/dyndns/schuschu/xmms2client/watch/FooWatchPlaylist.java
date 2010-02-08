package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;

public class FooWatchPlaylist extends Thread {

	Command c;
	private Runnable r;
	private final FooInterfaceViewElement view;

	public FooWatchPlaylist(Client client, final FooInterfaceViewElement view) {
		this.view = view;
		c = Playlist.changeBroadcast();
		c.execute(client);

		r = new Runnable() {
			public void run() {
				view.getBackend().generateFilteredContent();
			}
		};

	}

	public void run() {
		// TODO: find better way to prevent refresh after the insertion/deletion
		// of each track (they are NOT removed at once!)
		long x = 0;
		long y = 0;
		while (!isInterrupted()) {
			try {
				x = System.currentTimeMillis();
				c.waitReply();
				y = System.currentTimeMillis();
				if (y - x > 100) {
					view.getReal().getDisplay().asyncExec(r);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
