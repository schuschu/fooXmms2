package org.dyndns.schuschu.xmms2client.view.window;

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;

import org.dyndns.schuschu.xmms2client.backend.FooBackendFilter;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylist;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylistSwitch;
import org.dyndns.schuschu.xmms2client.backend.FooBackendText;
import org.dyndns.schuschu.xmms2client.backend.factory.FooBackendFactory;
import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;

import org.dyndns.schuschu.xmms2client.view.composite.FooButtonsPlayback;
import org.dyndns.schuschu.xmms2client.view.composite.FooButtonsPlaylist;
import org.dyndns.schuschu.xmms2client.view.element.FooCombo;
import org.dyndns.schuschu.xmms2client.view.element.FooLabel;
import org.dyndns.schuschu.xmms2client.view.element.FooList;
import org.dyndns.schuschu.xmms2client.view.element.FooTable;
import org.dyndns.schuschu.xmms2client.view.element.factory.FooViewFactory;
import org.dyndns.schuschu.xmms2client.watch.FooWatchCurrentTrack;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaybackPos;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaybackStatus;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaylist;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaylistLoad;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FooWindow implements FooInterfaceWindow {

	final static int WIDTH = 1000;
	final static int HEIGHT = 600;

	public static Shell SHELL = null;

	private Display display;

	private SashForm sashFormMain = null;
	private Composite compositePlaylist = null;
	private FooList listArtist = null;
	private FooList listAlbum = null;
	private FooList listTrack = null;
	private FooBackendFilter artistBackend;
	private FooBackendFilter albumBackend;
	private FooBackendFilter trackBackend;
	private FooBackendPlaylist playlistBackend;
	private FooBackendPlaylistSwitch switchBackend;
	private FooBackendText statusbarBackend;
	private FooTable tablePlaylist = null;
	private FooCombo comboPlaylist = null;
	private FooLabel statusbar = null;
	private FooButtonsPlaylist buttonsPlaylist = null;
	private FooButtonsPlayback buttonsPlayback = null;
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

	private Point location;

	public HashMap<String, Object> views=null;
	public HashMap<String, Object> backends=null;

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
		
		createSShell();

		createViews();
		
		createBackends();

		/*
		 * // init playlist tablePlaylist.getBackend().refresh();
		 * 
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

		// FormLayout layout = new FormLayout();
		FormLayout layout = new FormLayout();
		SHELL.setLayout(layout);

		/*
		 * createSashFormMain();
		 * 
		 * createStatusbar();
		 * 
		 * FormData sashData = new FormData(); sashData.top = new
		 * FormAttachment(0, 0); sashData.left = new FormAttachment(0, 0);
		 * sashData.right = new FormAttachment(100, 0); sashData.bottom = new
		 * FormAttachment(statusbar.getLabel(), 0);
		 * sashFormMain.setLayoutData(sashData);
		 * 
		 * FormData labelData = new FormData(); 
		 * labelData.left = new FormAttachment(0, 0); 
		 * labelData.right = new FormAttachment(100, 0);
		 * labelData.bottom = new FormAttachment(100, 0);
		 * 
		 * statusbar.setLayoutData(labelData);
		 */

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
	
	private void createViews(){
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
		Element views = root;

		NodeList nodes = views.getChildNodes();

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
	
	private void createBackends(){
		createBackendElements(FooXML.getElement("backends"));
	}
	
	private void createBackendElements(Element root){
		Element views = root;

		NodeList nodes = views.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {

			Node node = nodes.item(i);

			if (node instanceof Element) {

				Element child = (Element) node;

				try {
					getBackendFactory().create(child);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				createViewElements(child);
			}

		}
	}

	private void createStatusbar() {
		statusbar = new FooLabel(SHELL, SWT.BORDER);

		statusbarBackend = new FooBackendText(
				"%status%: %artist% - %title%: %currentTime%/%duration%",statusbar);

		statusbarBackend.setName("statusbar");
		statusbarBackend.setDebugForeground(FooColor.DARK_YELLOW);
		statusbarBackend.setView(statusbar);
	}

	private void createSashFormMain() {
		Element element = FooXML.getElementWithName("views/shell",
				"sashFormMain");
		sashFormMain = (SashForm) getViewFactory().create(element);
		// sashFormMain = new SashForm(SHELL, SWT.NONE);

		createListArtist();
		createListAlbum();
		createListTrack();
		createCompositePlaylist();
	}

	private void createCompositePlaylist() {
		compositePlaylist = new Composite(sashFormMain, SWT.NONE);
		views.put("compositePlaylist", compositePlaylist);

		FormLayout layout = new FormLayout();
		compositePlaylist.setLayout(layout);

		createComboPlaylist();
		createTablePlaylist();
		createButtonsPlaylist();
		createButtonsPlayback();

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

	public SashForm getSashFormMain() {
		if (sashFormMain == null) {
			createSashFormMain();
		}
		return sashFormMain;
	}

	public Composite getCompositePlaylist() {
		if (compositePlaylist == null) {
			createCompositePlaylist();
		}
		return compositePlaylist;
	}

	public FooList getListArtist() {
		if (listArtist == null) {
			createListArtist();
		}
		return listArtist;
	}

	public FooList getListAlbum() {
		if (listAlbum == null) {
			createListArtist();
		}
		return listAlbum;
	}

	public FooList getListTrack() {
		if (listTrack == null) {
			createListTrack();
		}
		return listTrack;
	}

	public FooTable getTablePlaylist() {
		if (tablePlaylist == null) {
			createTablePlaylist();
		}
		return tablePlaylist;
	}

	public FooCombo getComboPlaylist() {
		if (comboPlaylist == null) {
			createComboPlaylist();
		}
		return comboPlaylist;
	}

	public FooButtonsPlaylist getButtonsPlaylist() {
		if (buttonsPlaylist == null) {
			createButtonsPlaylist();
		}
		return buttonsPlaylist;
	}

	public FooButtonsPlayback getButtonsPlayback() {
		if (buttonsPlayback == null) {
			createButtonsPlayback();
		}
		return buttonsPlayback;
	}

	public FooWatchPlaylist getWatchPlaylistCombo() {
		if (watchPlaylistCombo == null) {
			watchPlaylistCombo = new FooWatchPlaylist(switchBackend);
		}
		return watchPlaylistCombo;
	}

	public FooWatchPlaylist getWatchPlaylistList() {
		if (watchPlaylistList == null) {
			watchPlaylistList = new FooWatchPlaylist(playlistBackend);
		}
		return watchPlaylistList;
	}

	public FooWatchCurrentTrack getWatchCurrentPos() {
		if (watchCurrentPos == null) {
			watchCurrentPos = new FooWatchCurrentTrack(playlistBackend);
		}
		return watchCurrentPos;
	}

	public FooWatchPlaylistLoad getWatchPlaylistComboLoad() {
		if (watchPlaylistComboLoad == null) {
			watchPlaylistComboLoad = new FooWatchPlaylistLoad(switchBackend);
		}
		return watchPlaylistComboLoad;
	}

	public FooWatchPlaylistLoad getWatchPlaylistListLoad() {
		if (watchPlaylistListLoad == null) {
			watchPlaylistListLoad = new FooWatchPlaylistLoad(playlistBackend);
		}
		return watchPlaylistListLoad;
	}

	public FooWatchPlaybackPos getWatchPlaybackPos() {
		if (watchPlaybackPos == null) {
			watchPlaybackPos = new FooWatchPlaybackPos(statusbarBackend);
		}
		return watchPlaybackPos;
	}

	public FooWatchCurrentTrack getWatchPlaybackTrack() {
		if (watchPlaybackTrack == null) {
			watchPlaybackTrack = new FooWatchCurrentTrack(statusbarBackend);
		}
		return watchPlaybackTrack;
	}

	public FooWatchPlaybackStatus getWatchPlaybackStatus() {
		if (watchPlaybackStatus == null) {
			watchPlaybackStatus = new FooWatchPlaybackStatus(statusbarBackend);
		}
		return watchPlaybackStatus;
	}

	public void createListArtist() {
		Element view = FooXML.getElementWithName("views/shell/sash",
				"listArtist");
		listArtist = (FooList) viewFactory.create(view);

		Element root = FooXML.getElementWithName("backends", "artistBackend");

		String format = FooXML.getTagValue("format", root);
		String filter = FooXML.getTagValue("filter", root);
		String name = FooXML.getTagValue("name", root);
		String debugForeground = FooXML.getTagValue("debugForeground", root);
		String debugBackground = FooXML.getTagValue("debugBackground", root);

		artistBackend = new FooBackendFilter(format, filter, listArtist);
		artistBackend.setName(name);
		artistBackend.setDebugForeground(FooColor.fromString(debugForeground));
		artistBackend.setDebugBackground(FooColor.fromString(debugBackground));

		artistBackend.setToAll();

		listArtist.setBackend(artistBackend);

		/*
		 * listArtist.addAction(FooSource.MOUSE, artistBackend.ActionEnqueu(2));
		 * listArtist.addAction(FooSource.KEYBOARD, artistBackend
		 * .ActionEnqueu(SWT.CR)); listArtist.addAction(FooSource.KEYBOARD,
		 * artistBackend .ActionDeselect(SWT.ESC));
		 * 
		 * FooMenu menu = new FooMenu(SHELL);
		 * 
		 * FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		 * orderItem.setText("change order");
		 * orderItem.addAction(artistBackend.ActionOrder(0));
		 * 
		 * FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		 * formatItem.setText("change format");
		 * formatItem.addAction(artistBackend.ActionFormat(0));
		 * 
		 * listArtist.setMenu(menu);
		 */

	}

	public void createListAlbum() {
		Element view = FooXML.getElementWithName("views/shell/sash",
				"listAlbum");
		listAlbum = (FooList) viewFactory.create(view);

		Element root = FooXML.getElementWithName("backends", "albumBackend");

		String format = FooXML.getTagValue("format", root);
		String filter = FooXML.getTagValue("filter", root);
		String name = FooXML.getTagValue("name", root);
		String debugForeground = FooXML.getTagValue("debugForeground", root);
		String debugBackground = FooXML.getTagValue("debugBackground", root);

		albumBackend = new FooBackendFilter(format, filter, listAlbum);
		albumBackend.setName(name);
		albumBackend.setDebugForeground(FooColor.fromString(debugForeground));
		albumBackend.setDebugBackground(FooColor.fromString(debugBackground));

		albumBackend.setContentProvider(artistBackend);

		listAlbum.setBackend(albumBackend);

		/*
		 * listAlbum.addAction(FooSource.MOUSE, albumBackend.ActionEnqueu(2));
		 * listAlbum.addAction(FooSource.KEYBOARD, albumBackend
		 * .ActionEnqueu(SWT.CR)); listAlbum.addAction(FooSource.KEYBOARD,
		 * albumBackend .ActionDeselect(SWT.ESC));
		 * 
		 * FooMenu menu = new FooMenu(SHELL);
		 * 
		 * FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		 * orderItem.setText("change order");
		 * orderItem.addAction(albumBackend.ActionOrder(0));
		 * 
		 * FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		 * formatItem.setText("change format");
		 * formatItem.addAction(albumBackend.ActionFormat(0));
		 * 
		 * listAlbum.setMenu(menu);
		 */
	}

	public void createListTrack() {
		Element view = FooXML.getElementWithName("views/shell/sash",
				"listTrack");
		listTrack = (FooList) viewFactory.create(view);

		Element root = FooXML.getElementWithName("backends", "trackBackend");

		String format = FooXML.getTagValue("format", root);
		String filter = FooXML.getTagValue("filter", root);
		String name = FooXML.getTagValue("name", root);
		String debugForeground = FooXML.getTagValue("debugForeground", root);
		String debugBackground = FooXML.getTagValue("debugBackground", root);

		trackBackend = new FooBackendFilter(format, filter, listTrack);
		trackBackend.setName(name);
		trackBackend.setDebugForeground(FooColor.fromString(debugForeground));
		trackBackend.setDebugBackground(FooColor.fromString(debugBackground));

		trackBackend.setContentProvider(albumBackend);

		listTrack.setBackend(trackBackend);

		/*
		 * listTrack.addAction(FooSource.MOUSE, trackBackend.ActionEnqueu(2));
		 * listTrack.addAction(FooSource.KEYBOARD, trackBackend.ActionEnqueu(SWT.CR)); 
		 * listTrack.addAction(FooSource.KEYBOARD, trackBackend.ActionDeselect(SWT.ESC));
		 * 
		 * FooMenu menu = new FooMenu(SHELL);
		 * 
		 * FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		 * orderItem.setText("change order");
		 * orderItem.addAction(trackBackend.ActionOrder(0));
		 * 
		 * FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		 * formatItem.setText("change format");
		 * formatItem.addAction(trackBackend.ActionFormat(0));
		 * 
		 * listTrack.setMenu(menu);
		 */
	}

	public void createComboPlaylist() {
		Element view = FooXML.getElementWithName("views/shell/sash/composite",
				"comboPlaylist");
		comboPlaylist = (FooCombo) viewFactory.create(view);

		FormData comboData = new FormData();
		comboData.top = new FormAttachment(0, 0);
		comboData.left = new FormAttachment(0, 0);
		comboData.right = new FormAttachment(100, 0);
		comboPlaylist.setLayoutData(comboData);

		switchBackend = new FooBackendPlaylistSwitch(comboPlaylist);
		switchBackend.setName("PlaylistComboBackend");
		switchBackend.setDebugForeground(FooColor.GRAY);

		comboPlaylist.setBackend(switchBackend);

	}

	public void createTablePlaylist() {
		Element view = FooXML.getElementWithName("views/shell/sash/composite",
				"tablePlaylist");
		tablePlaylist = (FooTable) viewFactory.create(view);

		// TODO: find better way of layouting, this will kill me when parsing
		FormData listData = new FormData();
		listData.top = new FormAttachment(comboPlaylist.getCombo(), 0);
		listData.left = new FormAttachment(0, 0);
		listData.right = new FormAttachment(100, 0);
		listData.bottom = new FormAttachment(getButtonsPlaylist()
				.getComposite(), 0);
		tablePlaylist.setLayoutData(listData);

		Element root = FooXML.getElementWithName("backends", "playlistBackend");

		String format = FooXML.getTagValue("format", root);
		String name = FooXML.getTagValue("name", root);
		String debugForeground = FooXML.getTagValue("debugForeground", root);
		String debugBackground = FooXML.getTagValue("debugBackground", root);

		playlistBackend = new FooBackendPlaylist(format, tablePlaylist);
		playlistBackend.setName(name);
		playlistBackend
				.setDebugForeground(FooColor.fromString(debugForeground));
		playlistBackend
				.setDebugBackground(FooColor.fromString(debugBackground));

		tablePlaylist.setBackend(playlistBackend);

		/*
		 * tablePlaylist.addAction(FooSource.MOUSE,
		 * playlistBackend.ActionPlay(2));
		 * tablePlaylist.addAction(FooSource.KEYBOARD, playlistBackend
		 * .ActionPlay(SWT.CR)); tablePlaylist.addAction(FooSource.KEYBOARD,
		 * playlistBackend .ActionDeselect(SWT.ESC));
		 * tablePlaylist.addAction(FooSource.KEYBOARD, playlistBackend
		 * .ActionRemove(SWT.DEL));
		 * 
		 * FooMenu menu = new FooMenu(SHELL);
		 * 
		 * FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		 * formatItem.setText("change format");
		 * formatItem.addAction(playlistBackend.ActionFormat(0));
		 * 
		 * tablePlaylist.setMenu(menu);
		 */
	}

	public void createButtonsPlaylist() {
		Element view = FooXML.getElementWithName("views/shell/sash/composite",
				"buttonsPlaylist");
		buttonsPlaylist = (FooButtonsPlaylist) viewFactory.create(view);

		FormData buttonsplaylistData = new FormData();
		buttonsplaylistData.left = new FormAttachment(0, 0);
		buttonsplaylistData.right = new FormAttachment(100, 0);
		buttonsplaylistData.bottom = new FormAttachment(getButtonsPlayback()
				.getComposite(), 0);
		buttonsPlaylist.setLayoutData(buttonsplaylistData);
	}

	public void createButtonsPlayback() {
		Element view = FooXML.getElementWithName("views/shell/sash/composite",
				"buttonsPlayback");
		buttonsPlayback = (FooButtonsPlayback) viewFactory.create(view);

		FormData buttonsPlaybackData = new FormData();
		buttonsPlaybackData.left = new FormAttachment(0, 0);
		buttonsPlaybackData.right = new FormAttachment(100, 0);
		buttonsPlaybackData.bottom = new FormAttachment(100, 0);
		buttonsPlayback.setLayoutData(buttonsPlaybackData);
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
}