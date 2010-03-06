package org.dyndns.schuschu.xmms2client.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.view.element.FooShell;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FooFactory {

	private static final boolean DEBUG = FooLoader.DEBUG;

	private static void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(FooColor.WHITE);
				FooDebug.setBackground(FooColor.DARK_BLUE);
			}
			System.out.println("debug: FooFactory " + message);
		}
	}
	
	private static final HashMap<String, Object> views = new HashMap<String, Object>();
	private static final HashMap<String, Object> backends = new HashMap<String, Object>();
	private static final HashMap<String, Object> watches = new HashMap<String, Object>();

	public static final HashMap<String, FooFactorySub> factories = new HashMap<String, FooFactorySub>();

	private static final Vector<Element> layoutdata = new Vector<Element>();

	public static void loadPlugins() {

		debug("loading plugins");
		
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
			
			debug("plugins loaded");

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
	
	public static void parse(){
		debug("parsing xml window");
		Element root = FooXML.getElement("GUI");
		parse(root);
		createLayout();
		finishLayout(root);
		debug("xml window parsed");
	}
	
	
	private static void parse(Element root) {
		
		Element views = root;

		NodeList nodes = views.getChildNodes();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			
			Node node = nodes.item(i);

			if (node instanceof Element) {

				Element child = (Element) node;

				try {
					FooFactory.create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				parse(child);
			}
		}
	}

	public static Object create(Element element) {

		// type is the name of the backend which contains the action. if none is
		// specified the next (hirachical up) backend will be taken
		String type = element.getAttribute("type");

		// TODO: dependencies
		if (element.getNodeName().equals("layoutdata")) {
			layoutdata.add(element);
			return null;
		}

		if (!element.hasAttribute("type")
				&& element.getNodeName().equals("action")) {
			type = getDefaultType(element);
		}

		// TODO: move debug
		FooFactorySub sub = factories.get(type);
		if (sub == null) {
			debug("no factory for "+ type);
			return null;
		}

		return sub.create(element);

	}

	public static void createLayout() {

		for (Element element : layoutdata) {

			String type = element.getAttribute("type");

			// TODO: move debug
			FooFactorySub sub = FooFactory.factories.get(type);
			if (sub != null) {
				sub.create(element);
			} else {
				debug("no factory for " + type);
			}
		}
	}
	
	private static void finishLayout(Element root) {
		
		Element views = root;

		NodeList nodes = views.getChildNodes();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			
			Node node = nodes.item(i);

			if (node instanceof Element) {

				Element child = (Element) node;

				if (child.getNodeName().equals("shell")){
					Object o = getView(child.getAttribute("name"));
					if (o instanceof FooShell){
						((FooShell) o).getShell().layout();
					}
				}

				finishLayout(child);
			}
		}		
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
