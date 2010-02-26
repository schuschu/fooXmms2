package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;

public class FooWatchPlaylist extends Thread {

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
	
	public static void registerFactory(){
		//VIEW
		FooFactorySub factory = new FooFactorySub() {
			
			@Override
			public Object create(Element element) {

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for backend (since watches are always
				// direct below (hirachical) their backend)
				Element father = (Element) element.getParentNode();
				String backend = father.getAttribute("name");

				// TODO: think about these
				String debugForeground = FooXML.getTagValue("debugfg", element);
				String debugBackground = FooXML.getTagValue("debugbg", element);

				debug("creating FooWatchPlaylist " + name);

				FooWatchPlaylist playlist = new FooWatchPlaylist(
						getBackend(backend));

				playlist.setName(name);
				playlist.setDebugForeground(FooColor.valueOf(debugForeground));
				playlist.setDebugBackground(FooColor.valueOf(debugBackground));

				playlist.start();

				FooFactory.putWatch(name, playlist);
				return playlist;
			}
			
			private FooInterfaceBackend getBackend(String s) {
				Object o = FooFactory.getBackend(s);
				if (o instanceof FooInterfaceBackend) {
					return (FooInterfaceBackend) o;
				}
				return null;
			}
		};
		FooFactory.factories.put("FooWatchPlaylist", factory);
	}
}
