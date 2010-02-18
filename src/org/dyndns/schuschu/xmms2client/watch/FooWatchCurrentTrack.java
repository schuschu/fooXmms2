package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceTrackPosition;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;

public class FooWatchCurrentTrack extends Thread {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name = "FooWatchCurrentTrack";

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
	private int current;

	public FooWatchCurrentTrack(final FooInterfaceTrackPosition backend) {

		r = new Runnable() {
			public void run() {
				debug("fire");
				backend.setCurrent(current);
			}
		};
		
		// get current track
		try {
			Command init = Playback.currentId();
			current = init.executeSync(FooLoader.CLIENT);
			FooRunner.run(r);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		c = Playback.currentIdBroadcast();
		c.execute(FooLoader.CLIENT);



	}

	public void done() {
		running = false;
	}

	public void run() {

		running = true;
		while (running) {
			try {
				current = c.waitReply();
				FooRunner.run(r);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
