package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;

public class FooWatchPlaylist extends Thread {

	private Command c;
	private FooInterfaceBackend backend;

	public FooWatchPlaylist(Client client, FooInterfaceBackend backend) {
		this.backend = backend;
		c = Playlist.changeBroadcast();
		c.execute(client);
	}

	public void run() {
		while (!this.isInterrupted()) {
			try {
				System.out.println(c.waitReply().toString());
				backend.refresh();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}
}
