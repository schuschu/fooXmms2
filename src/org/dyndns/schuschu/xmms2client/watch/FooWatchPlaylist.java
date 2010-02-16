package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;

public class FooWatchPlaylist extends Thread {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name = "FooWatchPlaylist";

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(FooColor.WHITE);
				FooDebug.setBackground(FooColor.MAGENTA);
			}
			System.out.println("debug: " + name + " " + message);
		}
	}

	private boolean running;
	private Command c;
	private Runnable r;

	public FooWatchPlaylist(final FooInterfaceBackend backend) {

		c = Collection.changeBroadcast();
		c.execute(FooLoader.CLIENT);

		r = new Runnable() {
			public void run() {
				debug("fire");
				backend.refresh();
			}
		};

	}

	public void done() {
		running = false;
	}

	public void run() {
		long x = 0;
		long y = 0;
		running = true;
		while (running) {
			try {
				x = System.currentTimeMillis();
				c.waitReply();
				y = System.currentTimeMillis();
				if (y - x > 100) {
					FooRunner.run(r);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
