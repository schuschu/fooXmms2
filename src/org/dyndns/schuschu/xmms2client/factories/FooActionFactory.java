package org.dyndns.schuschu.xmms2client.factories;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.action.base.FooKey;
import org.dyndns.schuschu.xmms2client.action.base.FooSource;
import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

		// TODO: sudo make it good
		try {
			Element plugins = FooXML.getElement("plugins");

			NodeList children = plugins.getChildNodes();

			for (int i = 0; i < children.getLength(); i++) {
				Node node = children.item(i);

				if (node.getNodeName().equals("plugin")) {

					Element element = (Element) node;
					if (element.getAttribute("type").equals("action")) {
						String plugin = element.getAttribute("name");

						Class<?> clazz = Class.forName(plugin);
						Method meth = clazz.getMethod("registerFactory");
						meth.invoke(clazz);
					}
				}
			}

		} catch (SecurityException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public FooAction create(Element element) {

		// the name of the action within the backend , no default possible
		String name = element.getAttribute("name");

		// path is the name of the backend which contains the action. if none is
		// specified the next (hirachical up) backend will be taken
		String path = element.hasAttribute("path") ? element
				.getAttribute("path") : getDefaultPath(element);

		// Source of the event that triggers the event, default is KEYBOARD
		String sourcestring = element.hasAttribute("source") ? element
				.getAttribute("source") : "KEYBOARD";
		FooSource source = FooSource.valueOf(sourcestring);

		// TODO: mousecode
		// Code (keycode, mousecode) that triggers the event, default is NONE
		String codestring = element.hasAttribute("code") ? element
				.getAttribute("code") : "NONE";

		int code = 0;
		switch (source) {
		case MOUSE:
			code = Integer.parseInt(codestring);
			break;
		case KEYBOARD:
			code = FooKey.valueOf(codestring).getCode();
			break;
		}

		// get the parent nodes name for view (since actions are always direct
		// below (hirachical) their view element)
		Element father = (Element) element.getParentNode();
		String viewstring = father.getAttribute("name");
		FooInterfaceAction view = getView(viewstring);

		debug("creating FooAction " + name + " in " + path);

		FooActionFactorySub sub = factories.get(path);

		FooAction action = sub.create(name, code);

		view.addAction(source, action);

		return action;

	}

	private String getDefaultPath(Element element) {
		Element root = element;
		do {
			root = (Element) root.getParentNode();
		} while (FooXML.getElement(root, "backend") == null);

		Element back = FooXML.getElement(root, "backend");

		return back.getAttribute("name");
	}

	private FooInterfaceAction getView(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceAction) {
			return (FooInterfaceAction) o;
		}
		return null;
	}
}
