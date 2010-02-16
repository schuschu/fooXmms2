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
		listArtist.getBackend().setToAll();
		listAlbum.getBackend().setContentProvider(listArtist.getBackend());
		listTrack.getBackend().setContentProvider(listAlbum.getBackend());

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
			watchPlaylistCombo = new FooWatchPlaylist(comboPlaylist);
		}
		return watchPlaylistCombo;
	}

	public FooWatchPlaylist getWatchPlaylistList() {
		if (watchPlaylistList == null) {
			watchPlaylistList = new FooWatchPlaylist(listPlaylist);
		}
		return watchPlaylistList;
	}

	public FooWatchCurrentTrack getWatchCurrentPos() {
		if (watchCurrentPos == null) {
			watchCurrentPos = new FooWatchCurrentTrack(listPlaylist);
		}
		return watchCurrentPos;
	}

	public FooWatchPlaylistLoad getWatchPlaylistComboLoad() {
		if (watchPlaylistComboLoad == null) {
			watchPlaylistComboLoad = new FooWatchPlaylistLoad(comboPlaylist);
		}
		return watchPlaylistComboLoad;
	}

	public FooWatchPlaylistLoad getWatchPlaylistListLoad() {
		if (watchPlaylistListLoad == null) {
			watchPlaylistListLoad = new FooWatchPlaylistLoad(listPlaylist);
		}
		return watchPlaylistListLoad;
	}

	public void createListArtist() {
		listArtist = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		FooBackendMedia back = new FooBackendMedia("%artist%", "artist",
				listArtist);
		back.setName("Artistbackend");

		back.setDebugForeground(FooColor.DARK_MAGENTA);
		listArtist.setBackend(back);

		listArtist.addAction(FooSource.MOUSE, back.ActionEnqueu(2));
		listArtist.addAction(FooSource.KEYBOARD, back.ActionEnqueu(SWT.CR));
		listArtist.addAction(FooSource.KEYBOARD, back.ActionDeselect(SWT.ESC));

		FooMenu menu = new FooMenu(sShell);

		FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		orderItem.setText("change order");
		orderItem.addAction(back.ActionOrder(0));

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		formatItem.addAction(back.ActionFormat(0));

		listArtist.setMenu(menu);
	}

	public void createListAlbum() {
		listAlbum = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		FooBackendMedia back = new FooBackendMedia("%album% (%date%)", "album",
				listAlbum);
		back.setName("Albumbackend");
		back.setDebugForeground(FooColor.MAGENTA);
		listAlbum.setBackend(back);

		listAlbum.addAction(FooSource.MOUSE, back.ActionEnqueu(2));
		listAlbum.addAction(FooSource.KEYBOARD, back.ActionEnqueu(SWT.CR));
		listAlbum.addAction(FooSource.KEYBOARD, back.ActionDeselect(SWT.ESC));

		FooMenu menu = new FooMenu(sShell);

		FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		orderItem.setText("change order");
		orderItem.addAction(back.ActionOrder(0));

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		formatItem.addAction(back.ActionFormat(0));

		listAlbum.setMenu(menu);
	}

	public void createListTrack() {
		listTrack = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		FooBackendMedia back = new FooBackendMedia("%title%", "title",
				listTrack);
		back.setName("Trackbackend");
		back.setDebugForeground(FooColor.DARK_RED);
		listTrack.setBackend(back);

		listTrack.addAction(FooSource.MOUSE, back.ActionEnqueu(2));
		listTrack.addAction(FooSource.KEYBOARD, back.ActionEnqueu(SWT.CR));
		listTrack.addAction(FooSource.KEYBOARD, back.ActionDeselect(SWT.ESC));

		FooMenu menu = new FooMenu(sShell);

		FooMenuItem orderItem = new FooMenuItem(menu, SWT.NONE);
		orderItem.setText("change order");
		orderItem.addAction(back.ActionOrder(0));

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		formatItem.addAction(back.ActionFormat(0));

		listTrack.setMenu(menu);
	}

	public void createComboPlaylist() {

		comboPlaylist = new FooCombo(compositePlaylist, SWT.READ_ONLY);
		FormData comboData = new FormData();
		comboData.top = new FormAttachment(0, 0);
		comboData.left = new FormAttachment(0, 0);
		comboData.right = new FormAttachment(100, 0);
		comboPlaylist.setLayoutData(comboData);

		FooBackendPlaylistSwitch backend = new FooBackendPlaylistSwitch(
				comboPlaylist);
		backend.setName("PlaylistComboBackend");
		backend.setDebugForeground(FooColor.GRAY);

		comboPlaylist.setBackend(backend);

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

		FooBackendPlaylist back = new FooBackendPlaylist("%artist% - %title%",
				"title", listPlaylist);
		back.setName("Playlistbackend");
		back.setDebugForeground(FooColor.BLUE);
		listPlaylist.setBackend(back);

		listPlaylist.addAction(FooSource.MOUSE, back.ActionPlay(2));
		listPlaylist.addAction(FooSource.KEYBOARD, back.ActionPlay(SWT.CR));
		listPlaylist
				.addAction(FooSource.KEYBOARD, back.ActionDeselect(SWT.ESC));
		listPlaylist.addAction(FooSource.KEYBOARD, back.ActionRemove(SWT.DEL));

		FooMenu menu = new FooMenu(sShell);

		FooMenuItem formatItem = new FooMenuItem(menu, SWT.NONE);
		formatItem.setText("change format");
		formatItem.addAction(back.ActionFormat(0));

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