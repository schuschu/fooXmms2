package org.dyndns.schuschu.xmms2client.view.window;

import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.view.element.FooShell;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FooWindow implements FooInterfaceWindow {

	public static FooShell SHELL = null;

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

	private void initalize() {
		createWindowElements(FooXML.getElement("window"));
		createLayout(FooXML.getElement("window"));
	}

	private void createWindowElements(Element root) {
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
				createWindowElements(child);
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