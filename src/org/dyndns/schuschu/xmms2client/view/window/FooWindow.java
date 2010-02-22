package org.dyndns.schuschu.xmms2client.view.window;

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.Action.factory.FooActionFactory;
import org.dyndns.schuschu.xmms2client.backend.factory.FooBackendFactory;
import org.dyndns.schuschu.xmms2client.view.element.factory.FooViewFactory;
import org.dyndns.schuschu.xmms2client.view.menu.factory.FooMenuFactory;
import org.dyndns.schuschu.xmms2client.watch.factory.FooWatchFactory;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FooWindow implements FooInterfaceWindow {

	final static int WIDTH = 1000;
	final static int HEIGHT = 600;

	public static Shell SHELL = null;

	private Display display;

	private FooViewFactory viewFactory = null;
	private FooBackendFactory backendFactory = null;
	private FooWatchFactory watchFactory = null;
	private FooActionFactory actionFactory = null;
	private FooMenuFactory menuFactory = null;

	private Point location;

	public HashMap<String, Object> views = null;
	public HashMap<String, Object> backends = null;
	public HashMap<String, Object> watches = null;

	/**
	 * This method initializes SHELL
	 */

	public FooWindow(boolean maximized) {
		initalize();
		if (maximized) {
			SHELL.setMaximized(true);
		}
	}

	public void setVisible(boolean visible) {
		SHELL.setVisible(visible);
	}

	public void toggleVisible() {
		if (SHELL.getVisible()) {
			location = SHELL.getLocation();
		} else {
			if (location != null) {
				SHELL.setLocation(location);
			}
		}
		setVisible(!SHELL.getVisible());
	}

	public void initalize() {

		views = new HashMap<String, Object>();
		backends = new HashMap<String, Object>();
		watches = new HashMap<String, Object>();

		createSShell();

		createViews();

		createBackends();

		createWatches();

		createActions();
		
		createMenus();
	}

	private void createSShell() {
		SHELL = new Shell(getDisplay());

		views.put("SHELL", SHELL);

		SHELL.setText("fooXmms2");
		SHELL.setSize(new Point(WIDTH, HEIGHT));

		FormLayout layout = new FormLayout();
		SHELL.setLayout(layout);

		Image image = null;

		InputStream stream = this.getClass().getResourceAsStream(
				"/pixmaps/xmms2-128.png");
		if (stream != null) {
			try {
				image = new Image(getDisplay(), stream);
			} catch (IllegalArgumentException e) {
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		} else {
			// TODO: find better way to do this
			image = new Image(getDisplay(), "pixmaps/xmms2-128.png");
		}

		SHELL.setImage(image);

	}

	private void createViews() {
		createViewElements(FooXML.getElement("views"));
		createLayout(FooXML.getElement("views"));
	}

	private void createViewElements(Element root) {
		Element views = root;

		NodeList nodes = views.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {

			Node node = nodes.item(i);

			if (node instanceof Element) {

				Element child = (Element) node;

				try {
					getViewFactory().create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				createViewElements(child);
			}

		}
	}

	private void createLayout(Element root) {

		NodeList nodes = root.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {

			Node node = nodes.item(i);

			if (node instanceof Element) {

				Element child = (Element) node;

				try {
					getViewFactory().createLayoutData(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				createLayout(child);
			}

		}

	}

	private void createBackends() {
		createBackendElements(FooXML.getElement("backends"));
	}

	private void createBackendElements(Element root) {

		NodeList nodes = root.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {

			Node node = nodes.item(i);

			if (node instanceof Element) {

				Element child = (Element) node;

				try {
					getBackendFactory().create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				// createBackendElements(child);
			}

		}
	}

	private void createActions() {
		createActionElements(FooXML.getElement("actions"));
	}

	private void createActionElements(Element root) {

		NodeList nodes = root.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {

			Node node = nodes.item(i);

			if (node instanceof Element) {

				Element child = (Element) node;

				try {
					getActionFactory().create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				// createBackendElements(child);
			}

		}
	}
	
	private void createMenus() {
		getMenuFactory().create(FooXML.getElement("menus"));
	}

	private void createWatches() {
		createWatchElements(FooXML.getElement("watches"));
	}

	private void createWatchElements(Element root) {

		NodeList nodes = root.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {

			Node node = nodes.item(i);

			if (node instanceof Element) {

				Element child = (Element) node;

				try {
					getWatchFactory().create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				// createWatchElements(child);
			}

		}
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public Display getDisplay() {
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	public FooViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = new FooViewFactory(this);
		}
		return viewFactory;
	}

	public FooBackendFactory getBackendFactory() {
		if (backendFactory == null) {
			backendFactory = new FooBackendFactory(this);
		}
		return backendFactory;
	}

	public FooWatchFactory getWatchFactory() {
		if (watchFactory == null) {
			watchFactory = new FooWatchFactory(this);
		}
		return watchFactory;
	}

	public FooActionFactory getActionFactory() {
		if (actionFactory == null) {
			actionFactory = new FooActionFactory(this);
		}
		return actionFactory;
	}
	
	public FooMenuFactory getMenuFactory() {
		if (menuFactory == null) {
			menuFactory = new FooMenuFactory(this);
		}
		return menuFactory;
	}

	@Override
	public void loop() {
		while (!SHELL.isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
		getDisplay().dispose();
		FooLoader.CLIENT.stop();
	}
}