package org.dyndns.schuschu.xmms2client.watch;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.dyndns.schuschu.xmms2client.loader.Loader;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;

public class FooWatchPlaylistLoad extends Thread {
	
	private static final boolean DEBUG = Loader.DEBUG;
	private String name="FooWatchPlaylistLoad";
	
	private void debug(String message){
		if(DEBUG){
			System.out.println("debug: " + name + " " + message);
		}
	}

	private boolean running;
	private Command c;
	private Runnable r;
	private final FooInterfaceViewElement view;
	private int current;

	public FooWatchPlaylistLoad(Client client,
			final FooInterfaceViewElement view) {
		this.view = view;
		c = Playlist.loadBroadcast();
		c.execute(client);

		r = new Runnable() {
			public void run() {
				debug("fire");
				view.setSelection(new int[] { current });
				view.getBackend().selectionChanged();
			}
		};

	}

	public void done() {
		running = false;
	}

	public void run() {
		running = true;
		while (running) {
			try {
				String s = c.waitReply();

				Vector<String> content = view.getBackend().getContent();

				for (int i = 0; i < content.size(); i++) {
					if(s.equals(content.get(i))){
						current = i;
						break;
					}
				}

				view.getReal().getDisplay().asyncExec(r);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
