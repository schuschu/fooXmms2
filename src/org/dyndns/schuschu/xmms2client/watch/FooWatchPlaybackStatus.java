package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooWatchFactory;
import org.dyndns.schuschu.xmms2client.factories.FooWatchFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfacePlaybackStatus;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;
import se.fnord.xmms2.client.types.PlaybackStatus;

public class FooWatchPlaybackStatus extends Thread {

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
	
	public static void registerFactory(){
		//VIEW
		FooWatchFactorySub factory = new FooWatchFactorySub() {
			
			@Override
			protected Object create(Element element) {
				
				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for backend (since watches are always
				// direct below (hirachical) their backend)
				Element father = (Element) element.getParentNode();
				String backend = father.getAttribute("name");

				// TODO: think about these
				String debugForeground = FooXML.getTagValue("debugfg", element);
				String debugBackground = FooXML.getTagValue("debugbg", element);

				debug("creating FooWatchPlaybackStatus " + name);

				FooWatchPlaybackStatus playbackStatus = new FooWatchPlaybackStatus(
						getBackendPlayStatus(backend));

				playbackStatus.setName(name);
				playbackStatus
						.setDebugForeground(FooColor.valueOf(debugForeground));
				playbackStatus
						.setDebugBackground(FooColor.valueOf(debugBackground));

				playbackStatus.start();

				FooFactory.putWatch(name, playbackStatus);
				return playbackStatus;
			}
			
			private FooInterfacePlaybackStatus getBackendPlayStatus(String s) {
				Object o = FooFactory.getBackend(s);
				if (o instanceof FooInterfacePlaybackStatus) {
					return (FooInterfacePlaybackStatus) o;
				}
				return null;
			}
		};
		FooWatchFactory.factories.put("FooWatchPlaybackStatus", factory);
	}
}
