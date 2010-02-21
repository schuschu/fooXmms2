package org.dyndns.schuschu.xmms2client.Action.factory;

import java.util.HashMap;

import org.dyndns.schuschu.xmms2client.Action.FooAction;
import org.dyndns.schuschu.xmms2client.Action.FooKey;
import org.dyndns.schuschu.xmms2client.Action.FooSource;
import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
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
	private FooWindow window;

	public FooActionFactory(FooWindow window) {
		this.window = window;
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
		sub.init(window);

		FooAction action = sub.create(name, code);

		view.addAction(source, action);

		return action;

	}

	private FooInterfaceAction getView(String s) {
		Object o = window.views.get(s);
		if (o instanceof FooInterfaceAction) {
			return (FooInterfaceAction) o;
		}
		return null;
	}
}
