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
		if(!element.getNodeName().equals("backend")){
			return null;
		}
		
		String type = FooXML.getTagValue("type", element);
		String name = FooXML.getTagValue("name", element);

		String format, filter, view, contentprovider, debugForeground, debugBackground;

		try {
			switch (FooBackendType.valueOf(type)) {
			case FooBackendFilter:
				debug("creating FooBackendFilter " + name);

				/*
				 * MENU
				 * 
				 * FooMenu menu = new FooMenu(SHELL);
				 * 
				 * FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
				 * orderItem.setText("change order");
				 * orderItem.addAction(trackBackend.ActionOrder(0));
				 * 
				 * FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
				 * formatItem.setText("change format");
				 * formatItem.addAction(trackBackend.ActionFormat(0));
				 * 
				 * listTrack.setMenu(menu);
				 */

				format = FooXML.getTagValue("format", element);
				filter = FooXML.getTagValue("filter", element);
				view = FooXML.getTagValue("view", element);

				contentprovider = FooXML
						.getTagValue("contentprovider", element);

				debugForeground = FooXML
						.getTagValue("debugForeground", element);
				debugBackground = FooXML
						.getTagValue("debugBackground", element);

				FooBackendFilter filterBackend = new FooBackendFilter(format,
						filter, getView(view));
				filterBackend.setName(name);
				filterBackend.setDebugForeground(FooColor
						.valueOf(debugForeground));
				filterBackend.setDebugBackground(FooColor
						.valueOf(debugBackground));

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

				format = FooXML.getTagValue("format", element);
				view = FooXML.getTagValue("view", element);

				debugForeground = FooXML
						.getTagValue("debugForeground", element);
				debugBackground = FooXML
						.getTagValue("debugBackground", element);

				FooBackendPlaylist playlistBackend = new FooBackendPlaylist(
						format, getViewPlaylist(view));
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

				view = FooXML.getTagValue("view", element);

				debugForeground = FooXML
						.getTagValue("debugForeground", element);
				debugBackground = FooXML
						.getTagValue("debugBackground", element);

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

				view = FooXML.getTagValue("view", element);
				format = FooXML.getTagValue("format", element);

				debugForeground = FooXML
						.getTagValue("debugForeground", element);
				debugBackground = FooXML
						.getTagValue("debugBackground", element);

				FooBackendText textBackend = new FooBackendText(format,
						getViewText(view));
				textBackend.setName(name);
				textBackend.setDebugForeground(FooColor
						.valueOf(debugForeground));
				textBackend.setDebugBackground(FooColor
						.valueOf(debugBackground));

				FooFactory.putBackend(name, textBackend);
				return textBackend;
			}
		} catch (IllegalArgumentException e) {
			// Thats not an enum!
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
