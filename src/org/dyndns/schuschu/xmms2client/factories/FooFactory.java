package org.dyndns.schuschu.xmms2client.factories;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FooFactory {

	private static final HashMap<String, Object> views = new HashMap<String, Object>();
	private static final HashMap<String, Object> backends = new HashMap<String, Object>();
	private static final HashMap<String, Object> watches = new HashMap<String, Object>();

	public static final HashMap<String, FooFactorySub> factories = new HashMap<String, FooFactorySub>();

	public static void loadPlugins() {

		// TODO: sudo make it good
		try {
			Element plugins = FooXML.getElement("plugins");

			NodeList children = plugins.getChildNodes();

			for (int i = 0; i < children.getLength(); i++) {
				Node node = children.item(i);

				if (node.getNodeName().equals("plugin")) {

					Element element = (Element) node;

					String plugin = element.getAttribute("name");

					Class<?> clazz = Class.forName(plugin);
					Method meth = clazz.getMethod("registerFactory");
					meth.invoke(clazz);

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

	public static void putView(String name, Object object) {
		views.put(name, object);
	}

	public static Object getView(String name) {
		return views.get(name);
	}

	public static void putBackend(String name, Object object) {
		backends.put(name, object);
	}

	public static Object getBackend(String name) {
		return backends.get(name);
	}

	public static void putWatch(String name, Object object) {
		watches.put(name, object);
	}

	public static Object getWatch(String name) {
		return watches.get(name);
	}

	public static Object create(Element element) {

		// type is the name of the backend which contains the action. if none is
		// specified the next (hirachical up) backend will be taken
		String type = element.getAttribute("type");

		// TODO: dependencies
		if (element.getNodeName().equals("layoutdata")) {
			return null;
		}

		if (!element.hasAttribute("type")
				&& element.getNodeName().equals("action")) {
			type = getDefaultType(element);
		}

		// TODO: move debug
		FooFactorySub sub = factories.get(type);
		if (sub == null) {
			return null;
		}

		return sub.create(element);

	}
	
	private static String getDefaultType(Element element) {
		Element root = element;
		do {
			root = (Element) root.getParentNode();
		} while (FooXML.getElement(root, "backend") == null);

		Element back = FooXML.getElement(root, "backend");

		return back.getAttribute("name");
	}
}
