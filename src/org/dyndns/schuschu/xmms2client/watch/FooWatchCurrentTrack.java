package org.dyndns.schuschu.xmms2client.watch;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceCurrentTrack;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceDebug;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;

public class FooWatchCurrentTrack extends Thread implements FooInterfaceDebug{

	private static final boolean DEBUG = FooLoader.DEBUG;
	private FooColor debugForeground = FooColor.WHITE;
	private FooColor debugBackground = FooColor.RED;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.DOUTPUT!=null) {
				FooLoader.DOUTPUT.setForeground(debugForeground);
				FooLoader.DOUTPUT.setBackground(debugBackground);
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
	private int current;

	public FooWatchCurrentTrack(final FooInterfaceCurrentTrack backend) {

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

				debug("creating FooWatchCurrentTrack " + name);

				FooWatchCurrentTrack currentTrack = new FooWatchCurrentTrack(
						getBackendCurTrack(backend));

				currentTrack.setName(name);
				
				currentTrack.start();

				FooFactory.putWatch(name, currentTrack);
				return currentTrack;
			}
			
			private FooInterfaceCurrentTrack getBackendCurTrack(String s) {
				Object o = FooFactory.getBackend(s);
				if (o instanceof FooInterfaceCurrentTrack) {
					return (FooInterfaceCurrentTrack) o;
				}
				return null;
			}
		};
		
		FooFactory.factories.put("FooWatchCurrentTrack", factory);
	}
}
