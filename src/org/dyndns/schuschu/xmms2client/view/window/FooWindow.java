package org.dyndns.schuschu.xmms2client.view.window;

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;

import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylist;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylistSwitch;
import org.dyndns.schuschu.xmms2client.backend.FooBackendText;
import org.dyndns.schuschu.xmms2client.backend.factory.FooBackendFactory;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;

import org.dyndns.schuschu.xmms2client.view.element.factory.FooViewFactory;
import org.dyndns.schuschu.xmms2client.watch.FooWatchCurrentTrack;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaybackPos;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaybackStatus;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaylist;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaylistLoad;
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

	private FooWatchCurrentTrack watchCurrentPos = null;
	private FooWatchPlaylist watchPlaylistList = null;
	private FooWatchPlaylist watchPlaylistCombo = null;
	private FooWatchPlaylistLoad watchPlaylistComboLoad = null;
	private FooWatchPlaylistLoad watchPlaylistListLoad = null;
	private FooWatchPlaybackPos watchPlaybackPos = null;
	private FooWatchCurrentTrack watchPlaybackTrack = null;
	private FooWatchPlaybackStatus watchPlaybackStatus = null;

	private FooViewFactory viewFactory = null;
	private FooBackendFactory backendFactory = null;
	private FooWatchFactory watchFactory = null;

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
		
		/*
		 * // create Watches getWatchCurrentPos().start();
		 * getWatchPlaylistCombo().start(); getWatchPlaylistList().start();
		 * getWatchPlaylistComboLoad().start();
		 * getWatchPlaylistListLoad().start(); getWatchPlaybackPos().start();
		 * getWatchPlaybackTrack().start(); getWatchPlaybackStatus().start();
		 */

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

	public FooWatchPlaylist getWatchPlaylistCombo() {
		if (watchPlaylistCombo == null) {
			watchPlaylistCombo = new FooWatchPlaylist(
					(FooBackendPlaylistSwitch) backends
							.get("playlistComboBackend"));
		}
		return watchPlaylistCombo;
	}

	public FooWatchPlaylistLoad getWatchPlaylistComboLoad() {
		if (watchPlaylistComboLoad == null) {
			watchPlaylistComboLoad = new FooWatchPlaylistLoad(
					(FooBackendPlaylistSwitch) backends
							.get("playlistComboBackend"));
		}
		return watchPlaylistComboLoad;
	}

	public FooWatchPlaylist getWatchPlaylistList() {
		if (watchPlaylistList == null) {
			watchPlaylistList = new FooWatchPlaylist(
					(FooBackendPlaylist) backends.get("playlistBackend"));
		}
		return watchPlaylistList;
	}

	public FooWatchCurrentTrack getWatchCurrentPos() {
		if (watchCurrentPos == null) {
			watchCurrentPos = new FooWatchCurrentTrack(
					(FooBackendPlaylist) backends.get("playlistBackend"));
		}
		return watchCurrentPos;
	}

	public FooWatchPlaylistLoad getWatchPlaylistListLoad() {
		if (watchPlaylistListLoad == null) {
			watchPlaylistListLoad = new FooWatchPlaylistLoad(
					(FooBackendPlaylist) backends.get("playlistBackend"));
		}
		return watchPlaylistListLoad;
	}

	public FooWatchPlaybackPos getWatchPlaybackPos() {
		if (watchPlaybackPos == null) {
			watchPlaybackPos = new FooWatchPlaybackPos(
					(FooBackendText) backends.get("statusbarBackend"));
		}
		return watchPlaybackPos;
	}

	public FooWatchCurrentTrack getWatchPlaybackTrack() {
		if (watchPlaybackTrack == null) {
			watchPlaybackTrack = new FooWatchCurrentTrack(
					(FooBackendText) backends.get("statusbarBackend"));
		}
		return watchPlaybackTrack;
	}

	public FooWatchPlaybackStatus getWatchPlaybackStatus() {
		if (watchPlaybackStatus == null) {
			watchPlaybackStatus = new FooWatchPlaybackStatus(
					(FooBackendText) backends.get("statusbarBackend"));
		}
		return watchPlaybackStatus;
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
}