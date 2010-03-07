package org.dyndns.schuschu.xmms2client.watch;

import java.util.HashMap;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceDebug;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfacePlaybackPos;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;

public class FooWatchPlaybackPos extends Thread implements FooInterfaceDebug {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private HashMap<FooInterfacePlaybackPos,FooColor> debugForeground;
	private HashMap<FooInterfacePlaybackPos,FooColor> debugBackground;

	private void debug(String message, FooInterfacePlaybackPos backend) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooColor fg = debugForeground.containsKey(backend) ? debugForeground.get(backend) : FooColor.WHITE;
				FooColor bg = debugBackground.containsKey(backend) ? debugBackground.get(backend) : FooColor.RED;
				FooDebug.setForeground(fg);
				FooDebug.setBackground(bg);
			}
			System.out.println("debug: " + super.getName() + " " + message);
		}
	}

	public void setDebugBackground(FooColor debugBackground) {
		this.debugBackground.put(backends.lastElement(),debugBackground);
	}

	public void setDebugForeground(FooColor debugForeground) {
		this.debugForeground.put(backends.lastElement(),debugForeground);
	}

	private boolean running;
	private static Command c = null;
	private Runnable r;
	private int time;
	private static FooWatchPlaybackPos instance = null;
	private Vector<FooInterfacePlaybackPos> backends;
	private final int DELAY;

	public static FooWatchPlaybackPos create(
			final FooInterfacePlaybackPos backend) {
		if (instance == null) {
			instance = new FooWatchPlaybackPos(backend);
			instance.start();
		} else {
			instance.backends.add(backend);
		}
		return instance;
	}

	public FooWatchPlaybackPos(final FooInterfacePlaybackPos backend) {

		backends = new Vector<FooInterfacePlaybackPos>();
		debugBackground = new HashMap<FooInterfacePlaybackPos, FooColor>();
		debugForeground = new HashMap<FooInterfacePlaybackPos, FooColor>();

		backends.add(backend);

		if (DEBUG) {
			DELAY = 5000;
		} else {
			DELAY = 100;
		}

		r = new Runnable() {
			public void run() {
				for (FooInterfacePlaybackPos back : backends) {
					debug("fire",back);
					back.setCurrentTime(time);
				}
			}
		};

		Command t = Playback.playtime();

		try {
			time = t.executeSync(FooLoader.CLIENT);
			FooRunner.run(r);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		if (c == null) {
			c = Playback.playtimeSignal();
		}

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

	public static void registerFactory() {
		// WATCH
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public Object create(Element element) {

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for backend (since watches are
				// always
				// direct below (hirachical) their backend)
				Element father = (Element) element.getParentNode();
				String backend = father.getAttribute("name");

				debug("creating FooWatchPlaybackPos " + name);

				FooWatchPlaybackPos playbackPos = FooWatchPlaybackPos
						.create(getBackendPlayPos(backend));

				playbackPos.setName(name);

				FooFactory.putWatch(name, playbackPos);
				return playbackPos;
			}

			private FooInterfacePlaybackPos getBackendPlayPos(String s) {
				Object o = FooFactory.getBackend(s);
				if (o instanceof FooInterfacePlaybackPos) {
					return (FooInterfacePlaybackPos) o;
				}
				return null;
			}
		};
		FooFactory.factories.put("FooWatchPlaybackPos", factory);
	}
}
