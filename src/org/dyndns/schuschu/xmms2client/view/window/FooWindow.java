package org.dyndns.schuschu.xmms2client.view.window;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.Action.FooSource;
import org.dyndns.schuschu.xmms2client.backend.FooBackendMedia;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylist;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylistSwitch;
import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import org.dyndns.schuschu.xmms2client.view.element.FooButtonsPlayback;
import org.dyndns.schuschu.xmms2client.view.element.FooButtonsPlaylist;
import org.dyndns.schuschu.xmms2client.view.element.FooCombo;
import org.dyndns.schuschu.xmms2client.view.element.FooList;
import org.dyndns.schuschu.xmms2client.view.element.FooTable;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenuItem;
import org.dyndns.schuschu.xmms2client.watch.FooWatchCurrentTrack;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaylist;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaylistLoad;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;

public class FooWindow implements FooInterfaceWindow {

	final static int WIDTH = 1000;
	final static int HEIGHT = 600;

	private Display display;

	private Shell sShell = null;
	private SashForm sashFormMain = null;
	private Composite compositePlaylist = null;
	private FooList listArtist = null;
	private FooList listAlbum = null;
	private FooList listTrack = null;
	private FooBackendMedia artistBackend;
	private FooBackendMedia albumBackend;
	private FooBackendMedia trackBackend;
	private FooBackendPlaylist playlistBackend;
	private FooBackendPlaylistSwitch switchBackend;
	private FooTable listPlaylist = null;
	private FooCombo comboPlaylist = null;
	private FooButtonsPlaylist buttonsPlaylist = null;
	private FooButtonsPlayback buttonsPlayback = null;
	private FooWatchCurrentTrack watchCurrentPos = null;
	private FooWatchPlaylist watchPlaylistList = null;
	private FooWatchPlaylist watchPlaylistCombo = null;
	private FooWatchPlaylistLoad watchPlaylistComboLoad = null;
	private FooWatchPlaylistLoad watchPlaylistListLoad = null;

	/**
	 * This method initializes sShell
	 */

	public FooWindow(boolean maximized) {
		initalize();
		if (maximized) {
			getsShell().setMaximized(true);
		}
	}

	public void setVisible(boolean visible) {
		getsShell().setVisible(visible);
	}

	public void toggleVisible() {
		setVisible(!getsShell().getVisible());
	}

	public void initalize() {

		createSShell();

		// here starts the magic (content chaining)

		// init playlist
		listPlaylist.getBackend().refresh();

		// create Watches
		getWatchCurrentPos().start();
		getWatchPlaylistCombo().start();
		getWatchPlaylistList().start();
		getWatchPlaylistComboLoad().start();
		getWatchPlaylistListLoad().start();
	}

	private void createSShell() {
		setsShell(new Shell(getDisplay()));
		getsShell().setText("fooXmms2");
		getsShell().setSize(new Point(WIDTH, HEIGHT));
		createSashFormMain();
		getsShell().setLayout(new FillLayout());

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

		getsShell().setImage(image);
	}

	private void createSashFormMain() {
		sashFormMain = new SashForm(getsShell(), SWT.NONE);

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

	public void setsShell(Shell sShell) {
		this.sShell = sShell;
	}

	public Shell getsShell() {
		return sShell;
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

	public void run() {
		while (!getsShell().isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
		getDisplay().dispose();
	}

	@Override
	public void loop() {
		while (!getsShell().isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
		getDisplay().dispose();
		FooLoader.client.stop();
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

	public void createListArtist() {
		listArtist = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		artistBackend = new FooBackendMedia("%artist%", "artist", listArtist);
		artistBackend.setName("Artistbackend");
		artistBackend.setDebugForeground(FooColor.DARK_MAGENTA);
		artistBackend.setToAll();
		listArtist.setBackend(artistBackend);

		listArtist.addAction(FooSource.MOUSE, artistBackend.ActionEnqueu(2));
		listArtist.addAction(FooSource.KEYBOARD, artistBackend
				.ActionEnqueu(SWT.CR));
		listArtist.addAction(FooSource.KEYBOARD, artistBackend
				.ActionDeselect(SWT.ESC));

		FooMenu menu = new FooMenu(sShell);

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

		albumBackend = new FooBackendMedia("%album% (%date%)", "album",
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

		FooMenu menu = new FooMenu(sShell);

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

		trackBackend = new FooBackendMedia("%title%", "title", listTrack);
		trackBackend.setName("Trackbackend");
		trackBackend.setDebugForeground(FooColor.DARK_RED);
		trackBackend.setContentProvider(albumBackend);
		listTrack.setBackend(trackBackend);

		listTrack.addAction(FooSource.MOUSE, trackBackend.ActionEnqueu(2));
		listTrack.addAction(FooSource.KEYBOARD, trackBackend
				.ActionEnqueu(SWT.CR));
		listTrack.addAction(FooSource.KEYBOARD, trackBackend
				.ActionDeselect(SWT.ESC));

		FooMenu menu = new FooMenu(sShell);

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

		switchBackend = new FooBackendPlaylistSwitch(
				comboPlaylist);
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
				"title", listPlaylist);
		playlistBackend.setName("Playlistbackend");
		playlistBackend.setDebugForeground(FooColor.BLUE);
		listPlaylist.setBackend(playlistBackend);

		listPlaylist.addAction(FooSource.MOUSE, playlistBackend.ActionPlay(2));
		listPlaylist.addAction(FooSource.KEYBOARD, playlistBackend.ActionPlay(SWT.CR));
		listPlaylist
				.addAction(FooSource.KEYBOARD, playlistBackend.ActionDeselect(SWT.ESC));
		listPlaylist.addAction(FooSource.KEYBOARD, playlistBackend.ActionRemove(SWT.DEL));

		FooMenu menu = new FooMenu(sShell);

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