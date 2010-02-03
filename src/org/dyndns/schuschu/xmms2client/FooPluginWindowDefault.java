package org.dyndns.schuschu.xmms2client;

import se.fnord.xmms2.client.Client;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.awt.AWTKeyStroke;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * @author schuschu
 * 
 */
public class FooPluginWindowDefault extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8692328843233503738L;

	final static int WIDTH = 600;
	final static int HEIGHT = 400;
	final static int BOARDER = 5;

	private static String window_title = "fooXmms2";

	private Client client;

	// main panel
	private JPanel jPanel = null;

	// Lists
	private FooPluginViewElementList fpVeLiArtist = null;
	private FooPluginViewElementList fpVeLiAlbum = null;
	private FooPluginViewElementList fpVeLiTrack = null;
	private FooPluginViewElementList fpVeLiPlaylist = null;

	// Combobox
	private FooPluginViewElementComboBox fpVeCbPlaylist = null;

	// Scrollpanes
	private FooPluginViewPaneScroll fpVpScrArtist = null;
	private FooPluginViewPaneScroll fpVpScrAlbum = null;
	private FooPluginViewPaneScroll fpVpScrTrack = null;
	private FooPluginViewPaneScroll fpVpScrPlaylist = null;

	// Splitpanes
	private FooPluginViewPaneSplit fpVpSpMain = null;
	private FooPluginViewPaneSplit fpVpSpRighSub = null;
	private FooPluginViewPaneSplit fpVpSpLeftSub = null;
	private FooPluginViewPaneSplit fpVpSpPlaylist = null;

	/**
	 * This is the default constructor
	 */
	public FooPluginWindowDefault(Client client) {
		super();
		this.client = client;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJPanel());
		this.setTitle(getWindow_title());
		this.setBounds(new Rectangle(0, 0, WIDTH + (3 * BOARDER), HEIGHT));
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
		// here starts the magic (content chaining)
		fpVeLiArtist.getBackend().setToAll();

		fpVeLiAlbum.getBackend().setContentProvider(fpVeLiArtist.getBackend());
		fpVeLiTrack.getBackend().setContentProvider(fpVeLiAlbum.getBackend());

		fpVeLiPlaylist.getBackend().setContentProvider(
				fpVeCbPlaylist.getBackend());

		// TODO: workaround to load content on startup;
		fpVeCbPlaylist.getBackend().refresh();

		// ensure artist list gets focus
		// atm unneeded because ArtistList is first element in layout
		/*
		 * this.addWindowFocusListener(new WindowAdapter() { public void
		 * windowGainedFocus(WindowEvent e) {
		 * fpVeLiArtist.requestFocusInWindow(); } });
		 */

		// TODO: create own FocusTraversalPolicy
		// extend set of focus traversal keys

		// TODO: move this stuff away from here
		Set<AWTKeyStroke> forwardKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(
				forwardKeys);
		newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));

		Set<AWTKeyStroke> backwardKeys = getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
		Set<AWTKeyStroke> newBackwardKeys = new HashSet<AWTKeyStroke>(
				backwardKeys);
		newBackwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));

		fpVeLiArtist.setFocusTraversalKeys(
				KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
		fpVeLiArtist.setFocusTraversalKeys(
				KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newBackwardKeys);
		fpVeLiAlbum.setFocusTraversalKeys(
				KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
		fpVeLiAlbum.setFocusTraversalKeys(
				KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newBackwardKeys);
		fpVeLiTrack.setFocusTraversalKeys(
				KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
		fpVeLiTrack.setFocusTraversalKeys(
				KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newBackwardKeys);
		fpVeCbPlaylist.setFocusTraversalKeys(
				KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
		fpVeCbPlaylist.setFocusTraversalKeys(
				KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newBackwardKeys);
		fpVeLiPlaylist.setFocusTraversalKeys(
				KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);
		fpVeLiPlaylist.setFocusTraversalKeys(
				KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newBackwardKeys);
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();

			// set to better layout
			jPanel.setLayout(new GridLayout(1, 1));
			jPanel.add(getFpvPslMain());
		}
		return jPanel;
	}

	/*
	 * *********************************************************************
	 * ViewElements
	 * *********************************************************************
	 */

	/**
	 * This method initializes getFpVeLiArtist
	 * 
	 * @return FooPluginViewElementList
	 */
	private FooPluginViewElementList getFpVeLiArtist() {
		if (fpVeLiArtist == null) {
			fpVeLiArtist = new FooPluginViewElementList();

			FooPluginBackendMedia backend = new FooPluginBackendMedia(
					"%artist%", "artist", client, fpVeLiArtist);
			fpVeLiArtist.setBackend(backend);

			FooPluginActionFilter action = new FooPluginActionFilter(backend);
			fpVeLiArtist.setActionManager(action);

			// TODO: fix inital data display
			fpVeLiArtist.getBackend().refresh();

		}
		return fpVeLiArtist;
	}

	/**
	 * This method initializes getFpVeLiAlbum
	 * 
	 * @return FooPluginViewElementList
	 */
	private FooPluginViewElementList getFpVeLiAlbum() {
		if (fpVeLiAlbum == null) {
			fpVeLiAlbum = new FooPluginViewElementList();

			FooPluginBackendMedia backend = new FooPluginBackendMedia(
					"%album% (%date%)", "album", client, fpVeLiAlbum);
			fpVeLiAlbum.setBackend(backend);

			FooPluginActionFilter action = new FooPluginActionFilter(backend);
			fpVeLiAlbum.setActionManager(action);

		}
		return fpVeLiAlbum;
	}

	/**
	 * This method initializes getFpVeLiTrack
	 * 
	 * @return FooPluginViewElementList
	 */
	private FooPluginViewElementList getFpVeLiTrack() {
		if (fpVeLiTrack == null) {
			fpVeLiTrack = new FooPluginViewElementList();

			FooPluginBackendMedia backend = new FooPluginBackendMedia(
					"%title%", "title", client, fpVeLiTrack);
			fpVeLiTrack.setBackend(backend);

			FooPluginActionFilter action = new FooPluginActionFilter(backend);
			fpVeLiTrack.setActionManager(action);
		}
		return fpVeLiTrack;
	}

	/**
	 * This method initializes getFpVeLiPlaylist
	 * 
	 * @return FooPluginViewElementList
	 */
	private FooPluginViewElementList getFpVeLiPlaylist() {
		if (fpVeLiPlaylist == null) {
			fpVeLiPlaylist = new FooPluginViewElementList();

			FooPluginBackendMediaPlaylist backend = new FooPluginBackendMediaPlaylist(
					"%id%: %artist% - %title%", "title", client, fpVeLiPlaylist);

			fpVeLiPlaylist.setBackend(backend);

			FooPluginActionPlaylist action = new FooPluginActionPlaylist(
					backend);
			fpVeLiTrack.setActionManager(action);
		}
		return fpVeLiPlaylist;
	}

	/**
	 * This method initializes fpCbPlaylist
	 * 
	 * @return FooPluginViewElementComboBox
	 */
	private FooPluginViewElementComboBox getFpVeCbPlaylist() {
		if (fpVeCbPlaylist == null) {
			fpVeCbPlaylist = new FooPluginViewElementComboBox();

			FooPluginBackendPlaylist backend = new FooPluginBackendPlaylist(
					client, fpVeCbPlaylist);
			fpVeCbPlaylist.setBackend(backend);

		}
		return fpVeCbPlaylist;
	}

	/*
	 * *********************************************************************
	 * ViewPaneScroll
	 * *********************************************************************
	 */

	/**
	 * This method initializes fpVpScrArtist
	 * 
	 * @return FooPluginViewPaneScroll
	 */
	private FooPluginViewPaneScroll getFpScrArtist() {
		if (fpVpScrArtist == null) {
			fpVpScrArtist = new FooPluginViewPaneScroll();
			fpVpScrArtist.setViewportView(getFpVeLiArtist());
		}
		return fpVpScrArtist;
	}

	/**
	 * This method initializes fpVpScrAlbum
	 * 
	 * @return javax.swing.FooPluginViewPaneScroll
	 */
	private FooPluginViewPaneScroll getFpVpScrAlbum() {
		if (fpVpScrAlbum == null) {
			fpVpScrAlbum = new FooPluginViewPaneScroll();
			fpVpScrAlbum.setViewportView(getFpVeLiAlbum());
		}
		return fpVpScrAlbum;
	}

	/**
	 * This method initializes fpVpScrTrack
	 * 
	 * @return FooPluginViewPaneScroll
	 */
	private FooPluginViewPaneScroll getFpVpScrTrack() {
		if (fpVpScrTrack == null) {
			fpVpScrTrack = new FooPluginViewPaneScroll();
			fpVpScrTrack.setViewportView(getFpVeLiTrack());
		}
		return fpVpScrTrack;
	}

	/**
	 * This method initializes getFpVeSpPlaylist
	 * 
	 * @return FooPluginViewPaneScroll
	 */
	private FooPluginViewPaneScroll getFpVpScrPlaylist() {
		if (fpVpScrPlaylist == null) {
			fpVpScrPlaylist = new FooPluginViewPaneScroll();
			fpVpScrPlaylist.setViewportView(getFpVeLiPlaylist());
		}
		return fpVpScrPlaylist;
	}

	/*
	 * *********************************************************************
	 * ViewPaneSplit
	 * *********************************************************************
	 */

	/**
	 * This method initializes fpVpSpMain
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private FooPluginViewPaneSplit getFpvPslMain() {
		if (fpVpSpMain == null) {
			fpVpSpMain = new FooPluginViewPaneSplit();
			fpVpSpMain.setDividerSize(BOARDER);
			fpVpSpMain.setDividerLocation((int) (WIDTH / 2));
			fpVpSpMain.setResizeWeight(0.5D);
			fpVpSpMain.setContinuousLayout(false);
			fpVpSpMain.setLeftComponent(getFpVpSpLeftSup());
			fpVpSpMain.setRightComponent(getFpVpSpRightSub());
		}
		return fpVpSpMain;
	}

	/**
	 * This method initializes fpVpSpRighSub
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private FooPluginViewPaneSplit getFpVpSpRightSub() {
		if (fpVpSpRighSub == null) {
			fpVpSpRighSub = new FooPluginViewPaneSplit();
			fpVpSpRighSub.setDividerSize(BOARDER);
			fpVpSpRighSub.setDividerLocation((int) (WIDTH / 4));
			fpVpSpRighSub.setResizeWeight(0.5D);
			fpVpSpRighSub.setLeftComponent(getFpVpScrTrack());
			fpVpSpRighSub.setRightComponent(getFpVPSpPlaylist());
		}
		return fpVpSpRighSub;
	}

	/**
	 * This method initializes fpVpSpLeftSub
	 * 
	 * @return FooPluginViewSplitPanel
	 */
	private FooPluginViewPaneSplit getFpVpSpLeftSup() {
		if (fpVpSpLeftSub == null) {
			fpVpSpLeftSub = new FooPluginViewPaneSplit();
			fpVpSpLeftSub.setDividerSize(BOARDER);
			fpVpSpLeftSub.setDividerLocation((int) (WIDTH / 4));
			fpVpSpLeftSub.setResizeWeight(0.5D);
			fpVpSpLeftSub.setLeftComponent(getFpScrArtist());
			fpVpSpLeftSub.setRightComponent(getFpVpScrAlbum());
		}
		return fpVpSpLeftSub;
	}

	/**
	 * This method initializes fpVpSpLeftSub
	 * 
	 * @return FooPluginViewSplitPanel
	 */
	private FooPluginViewPaneSplit getFpVPSpPlaylist() {
		if (fpVpSpPlaylist == null) {
			fpVpSpPlaylist = new FooPluginViewPaneSplit();
			fpVpSpPlaylist
					.setOrientation(FooPluginViewPaneSplit.VERTICAL_SPLIT);
			fpVpSpPlaylist.setBottomComponent(getFpVpScrPlaylist());
			fpVpSpPlaylist.setTopComponent(getFpVeCbPlaylist());
		}
		return fpVpSpPlaylist;
	}

	/*
	 * *********************************************************************
	 * Getter/Setter
	 * *********************************************************************
	 */

	public static void setWindow_title(String window_title) {
		FooPluginWindowDefault.window_title = window_title;
	}

	public static String getWindow_title() {
		return window_title;
	}
}