package org.dyndns.schuschu.xmms2client.view.window;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.backend.FooBackendMedia;
import org.dyndns.schuschu.xmms2client.backend.FooBackendMediaPlaylist;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylist;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.newAction.FooActionDeselect;
import org.dyndns.schuschu.xmms2client.newAction.FooSource;
import org.dyndns.schuschu.xmms2client.newAction.Media.FooActionEnqueu;
import org.dyndns.schuschu.xmms2client.newAction.MediaPlaylist.FooActionPlay;
import org.dyndns.schuschu.xmms2client.view.element.FooButtonsPlayback;
import org.dyndns.schuschu.xmms2client.view.element.FooButtonsPlaylist;
import org.dyndns.schuschu.xmms2client.view.element.FooCombo;
import org.dyndns.schuschu.xmms2client.view.element.FooList;
import org.dyndns.schuschu.xmms2client.view.element.FooTable;
import org.dyndns.schuschu.xmms2client.view.menu.FooContextMedia;
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

import se.fnord.xmms2.client.Client;

public class FooWindow implements FooInterfaceWindow {

	final static int WIDTH = 1000;
	final static int HEIGHT = 600;

	Client client;

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

	public FooWindow(Client client, boolean maximized) {
		this.client = client;
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
		// TODO: end threads or just system exit in loader?
		client.stop();
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
			watchPlaylistCombo = new FooWatchPlaylist(client, comboPlaylist);
		}
		return watchPlaylistCombo;
	}

	public FooWatchPlaylist getWatchPlaylistList() {
		if (watchPlaylistList == null) {
			watchPlaylistList = new FooWatchPlaylist(client, listPlaylist);
		}
		return watchPlaylistList;
	}

	public FooWatchCurrentTrack getWatchCurrentPos() {
		if (watchCurrentPos == null) {
			watchCurrentPos = new FooWatchCurrentTrack(client, listPlaylist);
		}
		return watchCurrentPos;
	}

	public FooWatchPlaylistLoad getWatchPlaylistComboLoad() {
		if (watchPlaylistComboLoad == null) {
			watchPlaylistComboLoad = new FooWatchPlaylistLoad(client,
					comboPlaylist);
		}
		return watchPlaylistComboLoad;
	}

	public FooWatchPlaylistLoad getWatchPlaylistListLoad() {
		if (watchPlaylistListLoad == null) {
			watchPlaylistListLoad = new FooWatchPlaylistLoad(client,
					listPlaylist);
		}
		return watchPlaylistListLoad;
	}

	public void createListArtist() {
		listArtist = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		FooBackendMedia artist_backend = new FooBackendMedia("%artist%",
				"artist", client, listArtist);
		artist_backend.setName("Artistbackend");

		artist_backend.setDebugForeground(SWT.COLOR_DARK_MAGENTA);
		listArtist.setBackend(artist_backend);

		listArtist.addAction(FooSource.MOUSE, new FooActionEnqueu(2,
				artist_backend));
		listArtist.addAction(FooSource.KEYBOARD, new FooActionEnqueu(SWT.CR,
				artist_backend));
		listArtist.addAction(FooSource.KEYBOARD, new FooActionDeselect(SWT.ESC,
				artist_backend));

		FooContextMedia artistMenu = new FooContextMedia(listArtist,
				artist_backend, client);
		artistMenu.setMenu();
		;
	}

	public void createListAlbum() {
		listAlbum = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		FooBackendMedia album_backend = new FooBackendMedia("%album% (%date%)",
				"album", client, listAlbum);
		album_backend.setName("Albumbackend");
		album_backend.setDebugForeground(SWT.COLOR_MAGENTA);
		listAlbum.setBackend(album_backend);

		listAlbum.addAction(FooSource.MOUSE, new FooActionEnqueu(2,
				album_backend));
		listAlbum.addAction(FooSource.KEYBOARD, new FooActionEnqueu(SWT.CR,
				album_backend));
		listAlbum.addAction(FooSource.KEYBOARD, new FooActionDeselect(SWT.ESC,
				album_backend));

		FooContextMedia albumMenu = new FooContextMedia(listAlbum,
				album_backend, client);
		albumMenu.setMenu();
	}

	public void createListTrack() {
		listTrack = new FooList(sashFormMain, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		FooBackendMedia track_backend = new FooBackendMedia("%title%", "title",
				client, listTrack);
		track_backend.setName("Trackbackend");
		track_backend.setDebugForeground(SWT.COLOR_DARK_RED);
		listTrack.setBackend(track_backend);

		listTrack.addAction(FooSource.MOUSE, new FooActionEnqueu(2,
				track_backend));
		listTrack.addAction(FooSource.KEYBOARD, new FooActionEnqueu(SWT.CR,
				track_backend));
		listTrack.addAction(FooSource.KEYBOARD, new FooActionDeselect(SWT.ESC,
				track_backend));

		FooContextMedia trackMenu = new FooContextMedia(listTrack,
				track_backend, client);
		trackMenu.setMenu();
	}

	public void createComboPlaylist() {
		/*
		 * TODO: add functionality to select by typing the name, if it does not
		 * exist prompt if the list should be created. Autocompletition?
		 */
		comboPlaylist = new FooCombo(compositePlaylist, SWT.READ_ONLY);
		FormData comboData = new FormData();
		comboData.top = new FormAttachment(0, 0);
		comboData.left = new FormAttachment(0, 0);
		comboData.right = new FormAttachment(100, 0);
		comboPlaylist.setLayoutData(comboData);

		FooBackendPlaylist backend = new FooBackendPlaylist(client,
				comboPlaylist);
		backend.setName("PlaylistComboBackend");
		backend.setDebugForeground(SWT.COLOR_GRAY);

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

		FooBackendMediaPlaylist list_backend = new FooBackendMediaPlaylist(
				"%artist% - %title%", "title", client, listPlaylist);
		list_backend.setName("Playlistbackend");
		list_backend.setDebugForeground(SWT.COLOR_BLUE);
		listPlaylist.setBackend(list_backend);

		
//  TODO: fix me
//		FooContextMediaPlaylist menu = new FooContextMediaPlaylist(
//				listPlaylist, list_backend, client);
//		menu.setMenu();

		listPlaylist.addAction(FooSource.MOUSE, new FooActionPlay(2,
				list_backend));
		listPlaylist.addAction(FooSource.KEYBOARD, new FooActionPlay(SWT.CR,
				list_backend));
		listPlaylist.addAction(FooSource.KEYBOARD, new FooActionDeselect(SWT.ESC,
				list_backend));
	}

	public void createButtonsPlaylist() {
		buttonsPlaylist = new FooButtonsPlaylist(compositePlaylist, SWT.NONE,
				client);

		FormData buttonsplaylistData = new FormData();
		buttonsplaylistData.left = new FormAttachment(0, 0);
		buttonsplaylistData.right = new FormAttachment(100, 0);
		buttonsplaylistData.bottom = new FormAttachment(getButtonsPlayback()
				.getComposite(), 0);
		buttonsPlaylist.setLayoutData(buttonsplaylistData);
	}

	public void createButtonsPlayback() {
		buttonsPlayback = new FooButtonsPlayback(compositePlaylist, SWT.NONE,
				client);
		FormData buttonsPlaybackData = new FormData();
		buttonsPlaybackData.left = new FormAttachment(0, 0);
		buttonsPlaybackData.right = new FormAttachment(100, 0);
		buttonsPlaybackData.bottom = new FormAttachment(100, 0);
		buttonsPlayback.setLayoutData(buttonsPlaybackData);

	}
}