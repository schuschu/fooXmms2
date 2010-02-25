package org.dyndns.schuschu.xmms2client.factories;

import org.dyndns.schuschu.xmms2client.backend.FooBackendFilter;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylist;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylistSwitch;
import org.dyndns.schuschu.xmms2client.backend.FooBackendText;
import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackendFilter;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceText;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceView;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceViewPlaylist;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.w3c.dom.Element;

public class FooBackendFactory {
	private static final boolean DEBUG = FooLoader.DEBUG;
	private FooColor debugForeground = FooColor.WHITE;
	private FooColor debugBackground = FooColor.BLUE;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(debugForeground);
				FooDebug.setBackground(debugBackground);
			}
			System.out.println("debug: BackendFactory " + message);
		}
	}

	public FooBackendFactory() {
	}

	public Object create(Element element) {

		// type of the backend (class), no default
		String type = element.getAttribute("type");

		// name equals variable name, no default
		String name = element.getAttribute("name");

		// TODO: docu
		// format (so documentation for further infos), no default
		String format = element.getAttribute("format");

		// filter defines which part of the selected items will be used to
		// filter the content for the next backend, no default
		String filter = element.getAttribute("filter");

		// defines which backend will be supplying the current backend with
		// data, default ALL (no input filtering)
		String contentprovider = element.hasAttribute("contentprovider") ? element
				.getAttribute("contentprovider")
				: "ALL";

		// TODO: think about these
		String debugForeground = FooXML.getTagValue("debugfg", element);
		String debugBackground = FooXML.getTagValue("debugbg", element);

		// get the parent nodes name for view (since backends are always direct
		// below (hirachical) their view element)
		Element father = (Element) element.getParentNode();
		String view = father.getAttribute("name");

		switch (FooBackendType.valueOf(type)) {
		case FooBackendFilter:
			debug("creating FooBackendFilter " + name);

			FooBackendFilter filterBackend = new FooBackendFilter(format,
					filter, getView(view));
			filterBackend.setName(name);
			filterBackend.setDebugForeground(FooColor.valueOf(debugForeground));
			filterBackend.setDebugBackground(FooColor.valueOf(debugBackground));

			if (contentprovider.equals("ALL")) {
				filterBackend.setToAll();
			} else {
				filterBackend
						.setContentProvider((FooInterfaceBackendFilter) FooFactory
								.getBackend(contentprovider));
			}

			filterBackend.registerActionFactory();

			FooFactory.putBackend(name, filterBackend);
			return filterBackend;

		case FooBackendPlaylist:
			debug("creating FooBackendPlaylist " + name);

			FooBackendPlaylist playlistBackend = new FooBackendPlaylist(format,
					getViewPlaylist(view));
			playlistBackend.setName(name);
			playlistBackend.setDebugForeground(FooColor
					.valueOf(debugForeground));
			playlistBackend.setDebugBackground(FooColor
					.valueOf(debugBackground));

			playlistBackend.registerActionFactory();

			FooFactory.putBackend(name, playlistBackend);
			return playlistBackend;

		case FooBackendPlaylistSwitch:
			debug("creating FooBackendPlaylistSwitch " + name);

			FooBackendPlaylistSwitch playlistSwitchBackend = new FooBackendPlaylistSwitch(
					getView(view));
			playlistSwitchBackend.setName(name);
			playlistSwitchBackend.setDebugForeground(FooColor
					.valueOf(debugForeground));
			playlistSwitchBackend.setDebugBackground(FooColor
					.valueOf(debugBackground));

			FooFactory.putBackend(name, playlistSwitchBackend);
			return playlistSwitchBackend;

		case FooBackendText:
			debug("creating FooBackendText " + name);

			FooBackendText textBackend = new FooBackendText(format,
					getViewText(view));
			textBackend.setName(name);
			textBackend.setDebugForeground(FooColor.valueOf(debugForeground));
			textBackend.setDebugBackground(FooColor.valueOf(debugBackground));

			FooFactory.putBackend(name, textBackend);
			return textBackend;
		}
		return null;
	}

	private FooInterfaceView getView(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceView) {
			return (FooInterfaceView) o;
		}
		return null;
	}

	private FooInterfaceText getViewText(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceText) {
			return (FooInterfaceText) o;
		}
		return null;
	}

	private FooInterfaceViewPlaylist getViewPlaylist(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceViewPlaylist) {
			return (FooInterfaceViewPlaylist) o;
		}
		return null;
	}

}
