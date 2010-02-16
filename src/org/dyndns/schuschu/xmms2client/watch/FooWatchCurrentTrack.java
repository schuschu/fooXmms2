package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;

public class FooWatchCurrentTrack extends Thread {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name = "FooWatchCurrentTrack";

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
	private int current;

	public FooWatchCurrentTrack(final FooInterfaceViewElement view) {

		c = Playback.currentIdBroadcast();
		c.execute(FooLoader.client);

		r = new Runnable() {
			public void run() {
				debug("fire");
				view.getBackend().setCurrent(current);
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
				current = c.waitReply();
				Display.getDefault().asyncExec(r);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
