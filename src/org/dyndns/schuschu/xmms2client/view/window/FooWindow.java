package org.dyndns.schuschu.xmms2client.view.window;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;

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

	private Point location;

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

		createSShell();

		createViews();
		
		createMenus();

		createBackends();
		
		createWatches();

		createActions();
		
	}

	private void createSShell() {
		SHELL = new Shell(getDisplay());

		FooFactory.putView("SHELL", SHELL);

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
					FooFactory.getViewFactory().create(child);
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
					FooFactory.getViewFactory().createLayoutData(child);
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
					FooFactory.getBackendFactory().create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
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
					FooFactory.getActionFactory().create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void createMenus() {
		createMenuElements(FooXML.getElement("menus"));
	}

	private void createMenuElements(Element root) {
		NodeList nodes = root.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {

			Node node = nodes.item(i);

			if (node instanceof Element) {

				Element child = (Element) node;

				try {
					FooFactory.getMenuFactory().create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
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
					FooFactory.getWatchFactory().create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
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