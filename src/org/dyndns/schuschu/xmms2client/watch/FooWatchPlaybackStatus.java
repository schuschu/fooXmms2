package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfacePlaybackStatus;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;
import se.fnord.xmms2.client.types.PlaybackStatus;

public class FooWatchPlaybackStatus extends Thread {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name = "FooWatchPlaybackStatus";

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(FooColor.WHITE);
				FooDebug.setBackground(FooColor.RED);
			}
			System.out.println("debug: " + name + " " + message);
		}
	}

	private boolean running;
	private Command c;
	private Runnable r;
	private PlaybackStatus status;

	public FooWatchPlaybackStatus(final FooInterfacePlaybackStatus backend) {

		r = new Runnable() {
			public void run() {
				debug("fire");
				backend.setStatus(status);
			}
		};

		// get current status
		try {
			Command init = Playback.status();
			status = init.executeSync(FooLoader.CLIENT);
			FooRunner.run(r);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		c = Playback.statusBroadcast();
		c.execute(FooLoader.CLIENT);

	}

	public void done() {
		running = false;
	}

	public void run() {

		running = true;
		while (running) {
			try {
				status = c.waitReply();
				FooRunner.run(r);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
