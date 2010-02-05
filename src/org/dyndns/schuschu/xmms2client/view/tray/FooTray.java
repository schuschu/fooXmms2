package org.dyndns.schuschu.xmms2client.view.tray;

import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackExit;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackPlay;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackStop;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenuItem;
import org.dyndns.schuschu.xmms2client.view.window.FooWindowSWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import se.fnord.xmms2.client.Client;

public class FooTray {

	private Tray tray;
	private Display display;
	// TODO: create window interface
	private FooWindowSWT window;
	private Client client;

	public FooTray(FooWindowSWT window, Client client) {
		this.window = window;
		this.client = client;
		// initialize();
	}

	public void initialize() {
		if (getTray() != null) {
			TrayItem item = new TrayItem(tray, SWT.NONE);
			// SVGImageConverter converter = new SVGImageConverter();

			Image image = new Image(getDisplay(), "pixmaps/xmms2-48.png");
			item.setImage(image);

			final Menu menu = new Menu(new Shell(getDisplay(), SWT.NONE),
					SWT.POP_UP);

			FooMenuItem menuPlay = new FooMenuItem(menu, SWT.PUSH);
			menuPlay.setText("Play");
			FooActionPlaybackPlay play = new FooActionPlaybackPlay(menuPlay,
					client);
			play.addListeners();

			FooMenuItem menuStop = new FooMenuItem(menu, SWT.PUSH);
			menuStop.setText("Stop");
			FooActionPlaybackStop stop = new FooActionPlaybackStop(menuStop,
					client);
			stop.addListeners();

			FooMenuItem menuExit = new FooMenuItem(menu, SWT.PUSH);
			menuExit.setText("Exit");
			FooActionPlaybackExit exit = new FooActionPlaybackExit(menuExit);
			exit.addListeners();

			item.addListener(SWT.MenuDetect, new Listener() {
				@Override
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});

			item.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					window.toggleVisible();
				}
			});

		}
	}

	public Tray getTray() {
		if (tray == null) {
			tray = getDisplay().getSystemTray();
		}
		return tray;
	}

	public Display getDisplay() {
		if (display == null) {
			display = Display.getCurrent();
		}
		return display;
	}

}
