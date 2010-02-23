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

		createWindow();
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

	private void createWindow() {
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