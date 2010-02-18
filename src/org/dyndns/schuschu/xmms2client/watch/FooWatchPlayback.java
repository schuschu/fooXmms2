package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfacePlaybackPos;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;

public class FooWatchPlayback extends Thread {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name = "FooWatchPlayback";

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(FooColor.WHITE);
				FooDebug.setBackground(FooColor.BLACK);
			}
			System.out.println("debug: " + name + " " + message);
		}
	}

	private boolean running;
	private Command c;
	private Runnable r;
	private int time;

	private final int DELAY;

	public FooWatchPlayback(final FooInterfacePlaybackPos backend) {

		if (DEBUG) {
			DELAY = 5000;
		} else {
			DELAY = 100;
		}

		c = Playback.playtimeSignal();

		r = new Runnable() {
			public void run() {
				debug("fire");
				backend.setCurrentTime(time);
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
				c.execute(FooLoader.CLIENT);
				time = c.waitReply();
				FooRunner.run(r);
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
