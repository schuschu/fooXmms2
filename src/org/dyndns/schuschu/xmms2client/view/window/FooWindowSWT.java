package org.dyndns.schuschu.xmms2client.view.window;

import org.dyndns.schuschu.xmms2client.action.FooPluginActionFilter;
import org.dyndns.schuschu.xmms2client.backend.FooPluginBackendMedia;
import org.dyndns.schuschu.xmms2client.view.element.FooList;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

import se.fnord.xmms2.client.Client;

public class FooWindowSWT {

	final static int WIDTH = 600;
	final static int HEIGHT = 400;

	Client client;

	private Display display = new Display(); // @jve:decl-index=0:

	boolean visible = false;

	private Shell sShell = null;
	private SashForm sashFormMain = null;
	private SashForm sashFormSubLeft = null;
	private SashForm sashFormSubRight = null;
	private FooList listArtist = null;
	private FooList listAlbum = null;
	private FooList listTrack = null;
	private FooList listPlaylist = null;

	/**
	 * This method initializes sShell
	 */

	public FooWindowSWT(Client client) {
		this.client = client;
		initalize();
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		if (visible) {
			getsShell().open();
		} else {
			getsShell().close();
		}
	}

	public void toggleVisible() {
		visible = !visible;
		setVisible(visible);
	}

	public void initalize() {
		createSShell();

		// here starts the magic (content chaining)
		listArtist.getBackend().setToAll();
		listAlbum.getBackend().setContentProvider(listArtist.getBackend());
		listTrack.getBackend().setContentProvider(listAlbum.getBackend());

		/*
		 * 
		 * 
		 * fpVeLiPlaylist.getBackend().setContentProvider(
		 * fpVeCbPlaylist.getBackend());
		 * 
		 * // TODO: remove workaround to load content on startup;
		 * fpVeCbPlaylist.getBackend().refresh();
		 */
	}

	private void createSShell() {
		setsShell(new Shell(getDisplay()));
		getsShell().setText("fooXmms2");
		getsShell().setSize(new Point(WIDTH, HEIGHT));
		createSashFormMain();
		getsShell().setLayout(new FillLayout());
	}

	private void createSashFormMain() {
		sashFormMain = new SashForm(getsShell(), SWT.NONE);
		createSashFormSubLeft();
		createSashFormSubRight();
	}

	private void createSashFormSubLeft() {
		sashFormSubLeft = new SashForm(sashFormMain, SWT.NONE);

		listArtist = new FooList(sashFormSubLeft, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		FooPluginBackendMedia artist_backend = new FooPluginBackendMedia(
				"%artist%", "artist", client, listArtist);
		listArtist.setBackend(artist_backend);
		FooPluginActionFilter artist_action = new FooPluginActionFilter(
				artist_backend);
		artist_action.addListeners();

		listAlbum = new FooList(sashFormSubLeft, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		// I still prefer album (date) but i don't realy care that much
		FooPluginBackendMedia album_backend = new FooPluginBackendMedia(
				"%date% - %album%", "album", client, listAlbum);
		listAlbum.setBackend(album_backend);
		FooPluginActionFilter album_action = new FooPluginActionFilter(
				album_backend);
		album_action.addListeners();
	}

	private void createSashFormSubRight() {
		sashFormSubRight = new SashForm(sashFormMain, SWT.NONE);
		
		listTrack = new FooList(sashFormSubRight, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);

		FooPluginBackendMedia track_backend = new FooPluginBackendMedia(
				"%title%", "title", client, listTrack);
		listTrack.setBackend(track_backend);

		FooPluginActionFilter action = new FooPluginActionFilter(track_backend);
		action.addListeners();

		listPlaylist = new FooList(sashFormSubRight, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
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
}
