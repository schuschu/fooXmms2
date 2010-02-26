package org.dyndns.schuschu.xmms2client.factories;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FooActionFactory {

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

	public Object create(Element element) {

		// type is the name of the backend which contains the action. if none is
		// specified the next (hirachical up) backend will be taken
		String type = element.hasAttribute("type") ? element
				.getAttribute("type") : getDefaultPath(element);

		return factories.containsKey(type) ? factories.get(type)
				.create(element) : null;
	}

	private String getDefaultPath(Element element) {
		Element root = element;
		do {
			root = (Element) root.getParentNode();
		} while (FooXML.getElement(root, "backend") == null);

		Element back = FooXML.getElement(root, "backend");

		return back.getAttribute("name");
	}
}
