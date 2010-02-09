package org.dyndns.schuschu.xmms2client.view.window;

import org.dyndns.schuschu.xmms2client.action.FooActionFilter;
import org.dyndns.schuschu.xmms2client.action.FooActionPlaylist;
import org.dyndns.schuschu.xmms2client.backend.FooBackendMedia;
import org.dyndns.schuschu.xmms2client.backend.FooBackendMediaPlaylist;
import org.dyndns.schuschu.xmms2client.backend.FooBackendPlaylist;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.view.element.FooButtonsPlayback;
import org.dyndns.schuschu.xmms2client.view.element.FooButtonsPlaylist;
import org.dyndns.schuschu.xmms2client.view.element.FooCombo;
import org.dyndns.schuschu.xmms2client.view.element.FooList;
import org.dyndns.schuschu.xmms2client.view.menu.FooContextMedia;
import org.dyndns.schuschu.xmms2client.watch.FooWatchPlaylist;
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

	// TODO: Code cleanup

	final static int WIDTH = 600;
	final static int HEIGHT = 400;

	Client client;

	private Display display = new Display();

	private Shell sShell = null;
	private SashForm sashFormMain = null;
	private SashForm sashFormSubLeft = null;
	private SashForm sashFormSubRight = null;
	private Composite compositePlaylist = null;
	private FooList listArtist = null;
	private FooList listAlbum = null;
	private FooList listTrack = null;
	private FooList listPlaylist = null;
	private FooCombo comboPlaylist = null;
	private FooButtonsPlaylist buttonsPlaylist = null;
	private FooButtonsPlayback buttonsPlayback = null;

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

	public void display(boolean visible) {
		while (!getsShell().isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
		getDisplay().dispose();
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

		listPlaylist.getBackend()
				.setContentProvider(comboPlaylist.getBackend());

		// generate initial listPlaylist data
		comboPlaylist.getBackend().generateFilteredContent();
	}

	private void createSShell() {
		setsShell(new Shell(getDisplay()));
		getsShell().setText("fooXmms2");
		getsShell().setSize(new Point(WIDTH, HEIGHT));
		createSashFormMain();
		getsShell().setLayout(new FillLayout());

		Image image = new Image(getDisplay(), "pixmaps/xmms2-128.png");
		getsShell().setImage(image);
	}

	private void createSashFormMain() {
		sashFormMain = new SashForm(getsShell(), SWT.NONE);
		createSashFormSubLeft();
		createSashFormSubRight();
	}

	private void createSashFormSubLeft() {
		sashFormSubLeft = new SashForm(sashFormMain, SWT.NONE);

		createListArtist();
		createListAlbum();
	}

	private void createSashFormSubRight() {
		sashFormSubRight = new SashForm(sashFormMain, SWT.NONE);

		createListTrack();
		createCompositePlaylist();
	}

	private void createCompositePlaylist() {
		compositePlaylist = new Composite(sashFormSubRight, SWT.NONE);
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
	}

	public SashForm getSashFormMain() {
		if (sashFormMain == null) {
			createSashFormMain();
		}
		return sashFormMain;
	}

	public SashForm getSashFormSubLeft() {
		if (sashFormSubLeft == null) {
			createSashFormSubLeft();
		}
		return sashFormSubLeft;
	}

	public SashForm getSashFormSubRight() {
		if (sashFormSubRight == null) {
			createSashFormSubRight();
		}
		return sashFormSubRight;
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

	public FooList getListPlaylist() {
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

	public void createListArtist() {
		listArtist = new FooList(sashFormSubLeft, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);
		FooBackendMedia artist_backend = new FooBackendMedia("%artist%",
				"artist", client, listArtist);
		listArtist.setBackend(artist_backend);
		FooActionFilter artist_action = new FooActionFilter(artist_backend);
		artist_action.addListeners();

		FooContextMedia artistMenu = new FooContextMedia(listArtist,
				artist_backend, client);
		artistMenu.setMenu();
		;
	}

	public void createListAlbum() {
		listAlbum = new FooList(sashFormSubLeft, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);
		FooBackendMedia album_backend = new FooBackendMedia("%date% - %album%",
				"album", client, listAlbum);
		listAlbum.setBackend(album_backend);
		FooActionFilter album_action = new FooActionFilter(album_backend);
		album_action.addListeners();

		FooContextMedia albumMenu = new FooContextMedia(listAlbum,
				album_backend, client);
		albumMenu.setMenu();
	}

	public void createListTrack() {
		listTrack = new FooList(sashFormSubRight, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		FooBackendMedia track_backend = new FooBackendMedia("%title%", "title",
				client, listTrack);
		listTrack.setBackend(track_backend);

		FooContextMedia trackMenu = new FooContextMedia(listTrack,
				track_backend, client);
		trackMenu.setMenu();

		FooActionFilter action = new FooActionFilter(track_backend);
		action.addListeners();

	}

	public void createComboPlaylist() {
		// TODO: add functionality to select by typing the name, if it does not
		// exist prompt if the list should be created. Autocompletition?
		comboPlaylist = new FooCombo(compositePlaylist, SWT.READ_ONLY);
		FormData comboData = new FormData();
		comboData.top = new FormAttachment(0, 0);
		comboData.left = new FormAttachment(0, 0);
		comboData.right = new FormAttachment(100, 0);
		comboPlaylist.setLayoutData(comboData);

		FooBackendPlaylist backend = new FooBackendPlaylist(client,
				comboPlaylist);
		comboPlaylist.setBackend(backend);

	}

	public void createListPlaylist() {
		listPlaylist = new FooList(compositePlaylist, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL);

		FormData listData = new FormData();
		listData.top = new FormAttachment(comboPlaylist.getCombo(), 0);
		listData.left = new FormAttachment(0, 0);
		listData.right = new FormAttachment(100, 0);
		listData.bottom = new FormAttachment(getButtonsPlaylist()
				.getComposite(), 0);
		listPlaylist.setLayoutData(listData);

		FooBackendMediaPlaylist list_backend = new FooBackendMediaPlaylist(
				"%artist% - %title%", "title", client, listPlaylist);

		listPlaylist.setBackend(list_backend);

		FooActionPlaylist list_action = new FooActionPlaylist(list_backend);
		list_action.addListeners();
		
		FooWatchPlaylist watch = new FooWatchPlaylist(client, comboPlaylist);
		watch.start();

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