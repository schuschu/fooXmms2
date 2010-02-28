package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceDebug;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfacePlaybackPos;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;

public class FooWatchPlaybackPos extends Thread implements FooInterfaceDebug{

	private static final boolean DEBUG = FooLoader.DEBUG;
	private FooColor debugForeground = FooColor.WHITE;
	private FooColor debugBackground = FooColor.RED;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(debugForeground);
				FooDebug.setBackground(debugBackground);
			}
			System.out.println("debug: " +  super.getName() +" " + message);
		}
	}
	
	public void setDebugBackground(FooColor debugBackground) {
		this.debugBackground = debugBackground;
	}


	public void setDebugForeground(FooColor debugForeground) {
		this.debugForeground = debugForeground;
	}

	private boolean running;
	private Command c;
	private Runnable r;
	private int time;

	private final int DELAY;

	public FooWatchPlaybackPos(final FooInterfacePlaybackPos backend) {

		if (DEBUG) {
			DELAY = 5000;
		} else {
			DELAY = 100;
		}

		r = new Runnable() {
			public void run() {
				debug("fire");
				backend.setCurrentTime(time);
			}
		};

		Command t = Playback.playtime();

		try {
			time = t.executeSync(FooLoader.CLIENT);
			FooRunner.run(r);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		c = Playback.playtimeSignal();

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
	
	public static void registerFactory(){
		//WATCH
		FooFactorySub factory = new FooFactorySub() {
			
			@Override
			public Object create(Element element) {
				
				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for backend (since watches are always
				// direct below (hirachical) their backend)
				Element father = (Element) element.getParentNode();
				String backend = father.getAttribute("name");

				debug("creating FooWatchPlaybackPos " + name);

				FooWatchPlaybackPos playbackPos = new FooWatchPlaybackPos(
						getBackendPlayPos(backend));

				playbackPos.setName(name);
				
				playbackPos.start();

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
