package org.dyndns.schuschu.xmms2client.view.tray;

import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackExit;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackNext;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackPlay;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackPrev;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackStop;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackPause;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenuItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import se.fnord.xmms2.client.Client;

public class FooTray {

	// TODO: Code cleanup

	private Tray tray;
	private Display display;
	private FooInterfaceWindow window;
	private Client client;

	public FooTray(FooInterfaceWindow window, Client client) {
		this.window = window;
		this.client = client;
	}

	public void initialize() {
		if (getTray() != null) {
			TrayItem item = new TrayItem(tray, SWT.NONE);

			Image image = new Image(getDisplay(), "pixmaps/xmms2-128.png");
			item.setImage(image);

			final FooMenu menu = new FooMenu(new Shell(getDisplay(), SWT.NONE),
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

			FooMenuItem menuPause = new FooMenuItem(menu, SWT.PUSH);
			menuPause.setText("Pause");
			FooActionPlaybackPause pause = new FooActionPlaybackPause(
					menuPause, client);
			pause.addListeners();

			FooMenuItem menuNext = new FooMenuItem(menu, SWT.PUSH);
			menuNext.setText("Next");
			FooActionPlaybackNext next = new FooActionPlaybackNext(menuNext,
					client);
			next.addListeners();

			FooMenuItem menuPrev = new FooMenuItem(menu, SWT.PUSH);
			menuPrev.setText("Prev");
			FooActionPlaybackPrev prev = new FooActionPlaybackPrev(menuPrev,
					client);
			prev.addListeners();

			new FooMenuItem(menu, SWT.SEPARATOR);

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

	public boolean isSupported() {
		if (getTray() != null) {
			return true;
		}
		return false;
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
