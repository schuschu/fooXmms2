package org.dyndns.schuschu.xmms2client.factories;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FooViewFactory {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private FooColor debugForeground = FooColor.WHITE;
	private FooColor debugBackground = FooColor.BLUE;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(debugForeground);
				FooDebug.setBackground(debugBackground);
			}
			System.out.println("debug: ViewFactory " + message);
		}
	}

	public static HashMap<String, FooViewFactorySub> factories = new HashMap<String, FooViewFactorySub>();

	public FooViewFactory() {
		// TODO: sudo make it good
		try {
			Element plugins = FooXML.getElement("plugins");

			NodeList children = plugins.getChildNodes();

			for (int i = 0; i < children.getLength(); i++) {
				Node node = children.item(i);

				if (node.getNodeName().equals("plugin")) {

					Element element = (Element) node;
					if (element.getAttribute("type").equals("view")) {
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

	public Object create(Element element) {

		// type of the view element (class), no default
		String type = element.getAttribute("type");

		return factories.containsKey(type) ? factories.get(type)
				.create(element) : null;
	}

	// TODO: get layout out of here (layoutfactory?)
	private Control getControl(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceControl) {
			return ((FooInterfaceControl) o).getControl();
		}
		return null;
	}

	public Object createLayoutData(Element item) {
		Element layout = FooXML.getElement(item, "layoutdata");

		if (layout != null) {
			// TODO: other layouts
			FormData data = new FormData();
			String top = layout.getAttribute("top");
			String bottom = layout.getAttribute("bottom");
			String left = layout.getAttribute("left");
			String right = layout.getAttribute("right");

			if (getControl(top) != null) {
				data.top = new FormAttachment(getControl(top));
			} else {
				try {
					data.top = new FormAttachment(Integer.parseInt(top));
				} catch (NumberFormatException e) {

				}
			}

			if (getControl(bottom) != null) {
				data.bottom = new FormAttachment(getControl(bottom));
			} else {
				try {
					data.bottom = new FormAttachment(Integer.parseInt(bottom));
				} catch (NumberFormatException e) {

				}
			}

			if (getControl(left) != null) {
				data.left = new FormAttachment(getControl(left));
			} else {
				try {
					data.left = new FormAttachment(Integer.parseInt(left));
				} catch (NumberFormatException e) {

				}
			}

			if (getControl(right) != null) {
				data.right = new FormAttachment(getControl(right));
			} else {
				try {
					data.right = new FormAttachment(Integer.parseInt(right));
				} catch (NumberFormatException e) {

				}
			}

			debug("layout for " + item.getAttribute("name") + " created!");

			Control c = getControl(item.getAttribute("name"));

			if (c == null) {
				debug("THIS SHOULD NOT HAPPEN!");
				return null;
			}
			c.setLayoutData(data);
			return data;
		}
		return null;
	}
}
