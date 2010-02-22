package org.dyndns.schuschu.xmms2client.view.menu.factory;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceMenu;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenuItem;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FooMenuFactory {
	private static final boolean DEBUG = FooLoader.DEBUG;
	private FooColor debugForeground = FooColor.WHITE;
	private FooColor debugBackground = FooColor.BLUE;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(debugForeground);
				FooDebug.setBackground(debugBackground);
			}
			System.out.println("debug: MenuFactory " + message);
		}
	}

	private FooWindow window;

	public FooMenuFactory(FooWindow window) {
		this.window = window;
	}

	public FooMenu create(Element element) {

		if (element.getNodeName().equals("menu")) {

			String view = FooXML.getTagValue("view", element);

			debug("creating menu for " + view);

			FooMenu menu = new FooMenu();

			getView(view).setMenu(menu);

			NodeList children = element.getChildNodes();

			for (int j = 0; j < children.getLength(); j++) {

				Node menunode = children.item(j);
				if (menunode instanceof Element) {

					Element child = (Element) menunode;

					if (child.getNodeName().equals("item")) {

						try {
							String text = FooXML.getTagValue("text", child);
							String name = FooXML.getTagValue("name", child);

							FooMenuItem item = new FooMenuItem(menu);
							item.setText(text);
							window.views.put(name, item);

							debug("created menuentry " + name);
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return menu;
		}
		return null;
	}

	private FooInterfaceMenu getView(String s) {
		Object o = window.views.get(s);
		if (o instanceof FooInterfaceMenu) {
			return (FooInterfaceMenu) o;
		}
		return null;
	}

}