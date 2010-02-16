package org.dyndns.schuschu.xmms2client.watch;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;

public class FooWatchPlaylistLoad extends Thread {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name = "FooWatchPlaylistLoad";

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(FooColor.WHITE);
				FooDebug.setBackground(FooColor.DARK_MAGENTA);
			}
			System.out.println("debug: " + name + " " + message);
		}
	}

	private boolean running;
	private Command c;
	private Runnable r;
	private final FooInterfaceBackend backend;
	private int current;

	public FooWatchPlaylistLoad(final FooInterfaceBackend backend) {
		this.backend = backend;
		c = Playlist.loadBroadcast();
		c.execute(FooLoader.CLIENT);

		r = new Runnable() {
			public void run() {
				debug("fire");
				backend.getView().setSelection(new int[] { current });
				backend.selectionChanged();
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

				Vector<String> content = backend.getContent();

				for (int i = 0; i < content.size(); i++) {
					if (s.equals(content.get(i))) {
						current = i;
						break;
					}
				}

				FooRunner.run(r);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
