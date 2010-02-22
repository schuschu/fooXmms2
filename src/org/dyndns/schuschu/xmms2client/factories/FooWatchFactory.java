package org.dyndns.schuschu.xmms2client.factories;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceCurrentTrack;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfacePlaybackPos;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfacePlaybackStatus;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.watch.FooWatchCurrentTrack;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaybackPos;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaybackStatus;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaylist;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaylistLoad;
import org.w3c.dom.Element;

public class FooWatchFactory {
	private static final boolean DEBUG = FooLoader.DEBUG;
	private FooColor debugForeground = FooColor.WHITE;
	private FooColor debugBackground = FooColor.BLUE;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(debugForeground);
				FooDebug.setBackground(debugBackground);
			}
			System.out.println("debug: WatchFactory " + message);
		}
	}

	public FooWatchFactory() {
	}

	public Object create(Element element) {
		String type = FooXML.getTagValue("type", element);
		String name = FooXML.getTagValue("name", element);

		String backend, debugForeground, debugBackground;

		try {
			switch (FooWatchType.valueOf(type)) {
			case FooWatchCurrentTrack:
				debug("creating FooWatchCurrentTrack " + name);

				backend = FooXML.getTagValue("backend", element);

				debugForeground = FooXML
						.getTagValue("debugForeground", element);
				debugBackground = FooXML
						.getTagValue("debugBackground", element);

				FooWatchCurrentTrack currentTrack = new FooWatchCurrentTrack(getBackendCurTrack(backend));
				
				currentTrack.setName(name);
				currentTrack.setDebugForeground(FooColor.valueOf(debugForeground));
				currentTrack.setDebugBackground(FooColor.valueOf(debugBackground));
				
				currentTrack.start();
				
				FooFactory.putWatch(name, currentTrack);
				return currentTrack;
			case FooWatchPlaybackPos:
				debug("creating FooWatchPlaybackPos " + name);

				backend = FooXML.getTagValue("backend", element);

				debugForeground = FooXML
						.getTagValue("debugForeground", element);
				debugBackground = FooXML
						.getTagValue("debugBackground", element);

				FooWatchPlaybackPos playbackPos = new FooWatchPlaybackPos(getBackendPlayPos(backend));
				
				playbackPos.setName(name);
				playbackPos.setDebugForeground(FooColor.valueOf(debugForeground));
				playbackPos.setDebugBackground(FooColor.valueOf(debugBackground));
				
				playbackPos.start();
				
				FooFactory.putWatch(name, playbackPos);
				return playbackPos;
			case FooWatchPlaybackStatus:
				debug("creating FooWatchPlaybackStatus " + name);

				backend = FooXML.getTagValue("backend", element);

				debugForeground = FooXML
						.getTagValue("debugForeground", element);
				debugBackground = FooXML
						.getTagValue("debugBackground", element);

				FooWatchPlaybackStatus playbackStatus = new FooWatchPlaybackStatus(getBackendPlayStatus(backend));
				
				playbackStatus.setName(name);
				playbackStatus.setDebugForeground(FooColor.valueOf(debugForeground));
				playbackStatus.setDebugBackground(FooColor.valueOf(debugBackground));
				
				playbackStatus.start();
				
				FooFactory.putWatch(name, playbackStatus);
				return playbackStatus;
			case FooWatchPlaylist:
				debug("creating FooWatchPlaylist " + name);

				backend = FooXML.getTagValue("backend", element);

				debugForeground = FooXML
						.getTagValue("debugForeground", element);
				debugBackground = FooXML
						.getTagValue("debugBackground", element);

				FooWatchPlaylist playlist = new FooWatchPlaylist(getBackend(backend));
				
				playlist.setName(name);
				playlist.setDebugForeground(FooColor.valueOf(debugForeground));
				playlist.setDebugBackground(FooColor.valueOf(debugBackground));
				
				playlist.start();
				
				FooFactory.putWatch(name, playlist);
				return playlist;
			case FooWatchPlaylistLoad:
				debug("creating FooWatchPlaylistLoad " + name);

				backend = FooXML.getTagValue("backend", element);

				debugForeground = FooXML
						.getTagValue("debugForeground", element);
				debugBackground = FooXML
						.getTagValue("debugBackground", element);

				FooWatchPlaylistLoad playlistLoad = new FooWatchPlaylistLoad(getBackend(backend));
				
				playlistLoad.setName(name);
				playlistLoad.setDebugForeground(FooColor.valueOf(debugForeground));
				playlistLoad.setDebugBackground(FooColor.valueOf(debugBackground));
				
				playlistLoad.start();
				
				FooFactory.putWatch(name, playlistLoad);
				return playlistLoad;
			}
		} catch (NullPointerException e) {
			// TODO: this is bad you know...
		} catch (IllegalArgumentException e) {
			// Thats not an enum!
		}
		return null;
	}
	
	private FooInterfaceBackend getBackend(String s) {
		Object o = FooFactory.getBackend(s);
		if (o instanceof FooInterfaceBackend) {
			return (FooInterfaceBackend) o;
		}
		return null;
	}
		
	private FooInterfaceCurrentTrack getBackendCurTrack(String s) {
		Object o = FooFactory.getBackend(s);
		if (o instanceof FooInterfaceCurrentTrack) {
			return (FooInterfaceCurrentTrack) o;
		}
		return null;
	}
	
	private FooInterfacePlaybackPos getBackendPlayPos(String s) {
		Object o = FooFactory.getBackend(s);
		if (o instanceof FooInterfacePlaybackPos) {
			return (FooInterfacePlaybackPos) o;
		}
		return null;
	}
	
	private FooInterfacePlaybackStatus getBackendPlayStatus(String s) {
		Object o = FooFactory.getBackend(s);
		if (o instanceof FooInterfacePlaybackStatus) {
			return (FooInterfacePlaybackStatus) o;
		}
		return null;
	}
	
}
