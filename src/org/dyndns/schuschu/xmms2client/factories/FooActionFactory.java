package org.dyndns.schuschu.xmms2client.factories;

import java.util.HashMap;

import org.dyndns.schuschu.xmms2client.Action.FooPlayback;
import org.dyndns.schuschu.xmms2client.Action.FooPlaylist;
import org.dyndns.schuschu.xmms2client.Action.FooSystem;
import org.dyndns.schuschu.xmms2client.Action.base.FooAction;
import org.dyndns.schuschu.xmms2client.Action.base.FooKey;
import org.dyndns.schuschu.xmms2client.Action.base.FooSource;
import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.w3c.dom.Element;

public class FooActionFactory {
	private static final boolean DEBUG = FooLoader.DEBUG;
	private FooColor debugForeground = FooColor.WHITE;
	private FooColor debugBackground = FooColor.BLUE;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(debugForeground);
				FooDebug.setBackground(debugBackground);
			}
			System.out.println("debug: ActionFactory " + message);
		}
	}

	public static HashMap<String, FooActionFactorySub> factories = new HashMap<String, FooActionFactorySub>();

	public FooActionFactory() {
		
		// TODO: dynamic vodoo
		FooPlayback.registerActionFactory();
		FooPlaylist.registerActionFactory();
		FooSystem.registerActionFactory();
		
	}

	public FooAction create(Element element) {

		String path = FooXML.getTagValue("path", element);
		String name = FooXML.getTagValue("name", element);
		String sourcestring = FooXML.getTagValue("source", element);
		String codestring = FooXML.getTagValue("code", element);
		
		FooSource source = FooSource.valueOf(sourcestring);
		
		int code = 0;
		switch (source) {
		case MOUSE:
			code = Integer.parseInt(codestring);
			break;
		case KEYBOARD:
			code = FooKey.valueOf(codestring).getCode();
			break;
		}

		String viewstring = FooXML.getTagValue("view", element);
		FooInterfaceAction view = getView(viewstring);

		debug("creating FooAction " + name + " in " + path);

		FooActionFactorySub sub = factories.get(path);

		FooAction action = sub.create(name, code);

		view.addAction(source, action);

		return action;

	}

	private FooInterfaceAction getView(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceAction) {
			return (FooInterfaceAction) o;
		}
		return null;
	}
}
