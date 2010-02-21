package org.dyndns.schuschu.xmms2client.view.window;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.Action.FooSource;
import org.dyndns.schuschu.xmms2client.backend.FooBackendFilter;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylist;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylistSwitch;
import org.dyndns.schuschu.xmms2client.backend.FooBackendText;
import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import org.dyndns.schuschu.xmms2client.view.composite.FooButtonsPlayback;
import org.dyndns.schuschu.xmms2client.view.composite.FooButtonsPlaylist;
import org.dyndns.schuschu.xmms2client.view.element.FooCombo;
import org.dyndns.schuschu.xmms2client.view.element.FooLabel;
import org.dyndns.schuschu.xmms2client.view.element.FooList;
import org.dyndns.schuschu.xmms2client.view.element.FooTable;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenuItem;
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
	private FooTable listPlaylist = null;
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

		// init playlist
		listPlaylist.getBackend().refresh();

		// create Watches
		getWatchCurrentPos().start();
		getWatchPlaylistCombo().start();
		getWatchPlaylistList().start();
		getWatchPlaylistComboLoad().start();
		getWatchPlaylistListLoad().start();
		getWatchPlaybackPos().start();
		getWatchPlaybackTrack().start();
		getWatchPlaybackStatus().start();

	}

	private void createSShell() {
		SHELL = new Shell(getDisplay());
		SHELL.setText("fooXmms2");
		SHELL.setSize(new Point(WIDTH, HEIGHT));

		FormLayout layout = new FormLayout();
		SHELL.setLayout(layout);

		createSashFormMain();

		createStatusbar();

		FormData sashData = new FormData();
		sashData.top = new FormAttachment(0, 0);
		sashData.left = new FormAttachment(0, 0);
		sashData.right = new FormAttachment(100, 0);
		sashData.bottom = new FormAttachment(statusbar.getLabel(), 0);
		sashFormMain.setLayoutData(sashData);

		FormData labelData = new FormData();
		labelData.left = new FormAttachment(0, 0);
		labelData.right = new FormAttachment(100, 0);
		labelData.bottom = new FormAttachment(100, 0);

		statusbar.setLayoutData(labelData);

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

	private void createStatusbar() {
		statusbar = new FooLabel(SHELL, SWT.BORDER);

		statusbarBackend = new FooBackendText(
				"%status%: %artist% - %title%: %currentTime%/%duration%");

		statusbarBackend.setName("statusbar");
		statusbarBackend.setDebugForeground(FooColor.DARK_YELLOW);
		statusbarBackend.setView(statusbar);
	}

	private void createSashFormMain() {
		sashFormMain = new SashForm(SHELL, SWT.NONE);

		createListArtist();
		createListAlbum();
		createListTrack();
		createCompositePlaylist();
	}

	private void createCompositePlaylist() {
		compositePlaylist = new Composite(sashFormMain, SWT.NONE);
		FormLayout layout = new FormLayout();
		compositePlaylist.setLayout(layout);

		createComboPlaylist();
		createListPlaylist();
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

	public FooTable getListPlaylist() {
		if (listPlaylist == null) {
			createListPlaylist();
		}
		return listPlaylist;
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
		listArtist = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		artistBackend = new FooBackendFilter("%artist%", "artist", listArtist);
		artistBackend.setName("Artistbackend");
		artistBackend.setDebugForeground(FooColor.DARK_MAGENTA);
		artistBackend.setToAll();
		listArtist.setBackend(artistBackend);

		listArtist.addAction(FooSource.MOUSE, artistBackend.ActionEnqueu(2));
		listArtist.addAction(FooSource.KEYBOARD, artistBackend
				.ActionEnqueu(SWT.CR));
		listArtist.addAction(FooSource.KEYBOARD, artistBackend
				.ActionDeselect(SWT.ESC));

		FooMenu menu = new FooMenu(SHELL);

		FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		orderItem.setText("change order");
		orderItem.addAction(artistBackend.ActionOrder(0));

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		formatItem.addAction(artistBackend.ActionFormat(0));

		listArtist.setMenu(menu);

	}

	public void createListAlbum() {
		listAlbum = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		albumBackend = new FooBackendFilter("%album% (%date%)", "album",
				listAlbum);
		albumBackend.setName("Albumbackend");
		albumBackend.setDebugForeground(FooColor.MAGENTA);
		albumBackend.setContentProvider(artistBackend);
		listAlbum.setBackend(albumBackend);

		listAlbum.addAction(FooSource.MOUSE, albumBackend.ActionEnqueu(2));
		listAlbum.addAction(FooSource.KEYBOARD, albumBackend
				.ActionEnqueu(SWT.CR));
		listAlbum.addAction(FooSource.KEYBOARD, albumBackend
				.ActionDeselect(SWT.ESC));

		FooMenu menu = new FooMenu(SHELL);

		FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		orderItem.setText("change order");
		orderItem.addAction(albumBackend.ActionOrder(0));

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		formatItem.addAction(albumBackend.ActionFormat(0));

		listAlbum.setMenu(menu);

	}

	public void createListTrack() {
		listTrack = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		trackBackend = new FooBackendFilter("%title%", "title", listTrack);
		trackBackend.setName("Trackbackend");
		trackBackend.setDebugForeground(FooColor.DARK_RED);
		trackBackend.setContentProvider(albumBackend);
		listTrack.setBackend(trackBackend);

		listTrack.addAction(FooSource.MOUSE, trackBackend.ActionEnqueu(2));
		listTrack.addAction(FooSource.KEYBOARD, trackBackend
				.ActionEnqueu(SWT.CR));
		listTrack.addAction(FooSource.KEYBOARD, trackBackend
				.ActionDeselect(SWT.ESC));

		FooMenu menu = new FooMenu(SHELL);

		FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		orderItem.setText("change order");
		orderItem.addAction(trackBackend.ActionOrder(0));

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		formatItem.addAction(trackBackend.ActionFormat(0));

		listTrack.setMenu(menu);

	}

	public void createComboPlaylist() {

		comboPlaylist = new FooCombo(compositePlaylist, SWT.READ_ONLY);
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

	public void createListPlaylist() {
		listPlaylist = new FooTable(compositePlaylist, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		FormData listData = new FormData();
		listData.top = new FormAttachment(comboPlaylist.getCombo(), 0);
		listData.left = new FormAttachment(0, 0);
		listData.right = new FormAttachment(100, 0);
		listData.bottom = new FormAttachment(getButtonsPlaylist()
				.getComposite(), 0);
		listPlaylist.setLayoutData(listData);

		playlistBackend = new FooBackendPlaylist("%artist% - %title%",
				listPlaylist);
		playlistBackend.setName("Playlistbackend");
		playlistBackend.setDebugForeground(FooColor.BLUE);
		listPlaylist.setBackend(playlistBackend);

		listPlaylist.addAction(FooSource.MOUSE, playlistBackend.ActionPlay(2));
		listPlaylist.addAction(FooSource.KEYBOARD, playlistBackend
				.ActionPlay(SWT.CR));
		listPlaylist.addAction(FooSource.KEYBOARD, playlistBackend
				.ActionDeselect(SWT.ESC));
		listPlaylist.addAction(FooSource.KEYBOARD, playlistBackend
				.ActionRemove(SWT.DEL));

		FooMenu menu = new FooMenu(SHELL);

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		formatItem.addAction(playlistBackend.ActionFormat(0));

		listPlaylist.setMenu(menu);

	}

	public void createButtonsPlaylist() {
		buttonsPlaylist = new FooButtonsPlaylist(compositePlaylist, SWT.NONE);

		FormData buttonsplaylistData = new FormData();
		buttonsplaylistData.left = new FormAttachment(0, 0);
		buttonsplaylistData.right = new FormAttachment(100, 0);
		buttonsplaylistData.bottom = new FormAttachment(getButtonsPlayback()
				.getComposite(), 0);
		buttonsPlaylist.setLayoutData(buttonsplaylistData);
	}

	public void createButtonsPlayback() {
		buttonsPlayback = new FooButtonsPlayback(compositePlaylist, SWT.NONE);
		FormData buttonsPlaybackData = new FormData();
		buttonsPlaybackData.left = new FormAttachment(0, 0);
		buttonsPlaybackData.right = new FormAttachment(100, 0);
		buttonsPlaybackData.bottom = new FormAttachment(100, 0);
		buttonsPlayback.setLayoutData(buttonsPlaybackData);

	}
}