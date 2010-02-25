package org.dyndns.schuschu.xmms2client.factories;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceMenu;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenuItem;
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

	public FooMenuFactory() {
	}

	public FooMenu create(Element element) {
		//creating menu for view from here
		
		// get the parent nodes name for view (since menu are always direct
		// below (hirachical) their view element)
		Element father =(Element) element.getParentNode();
		String view = father.getAttribute("name");

		debug("creating menu for " + view);

		FooMenu menu = new FooMenu();

		getView(view).setMenu(menu);

		NodeList children = element.getChildNodes();

		//creating menuitems for menu from here
		for (int j = 0; j < children.getLength(); j++) {

			Node menunode = children.item(j);
			if (menunode instanceof Element) {

				Element child = (Element) menunode;

				if (child.getNodeName().equals("item")) {

					//text of the menuitem, no default
					String text = child.getAttribute("text");
					
					// name equals variable name, no default
					String name = child.getAttribute("name");

					FooMenuItem item = new FooMenuItem(menu);
					item.setText(text);
					FooFactory.putView(name, item);

					debug("created menuentry " + name);
				}
			}
		}
		return menu;
	}

	private FooInterfaceMenu getView(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceMenu) {
			return (FooInterfaceMenu) o;
		}
		return null;
	}

}
