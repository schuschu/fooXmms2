package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.eclipse.swt.SWT;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;

public class FooWatchPlaylist extends Thread {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name = "FooWatchPlaylist";

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(SWT.COLOR_WHITE);
				FooDebug.setBackground(SWT.COLOR_RED);
			}
			System.out.println("debug: " + name + " " + message);
		}
	}

	private boolean running;
	private Command c;
	private Runnable r;
	private final FooInterfaceViewElement view;

	public FooWatchPlaylist(Client client, final FooInterfaceViewElement view) {
		this.view = view;

		c = Collection.changeBroadcast();
		c.execute(client);

		r = new Runnable() {
			public void run() {
				debug("fire");
				view.getBackend().refresh();
				view.getBackend().generateFilteredContent();
			}
		};

	}

	public void done() {
		running = false;
	}

	public void run() {
		// TODO: find better way to prevent refresh after the insertion/deletion
		// of each track (they are NOT removed at once!)
		long x = 0;
		long y = 0;
		running = true;
		while (running) {
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
