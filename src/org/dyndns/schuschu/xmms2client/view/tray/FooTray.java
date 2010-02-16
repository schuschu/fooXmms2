package org.dyndns.schuschu.xmms2client.view.tray;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.Action.FooPlayback;
import org.dyndns.schuschu.xmms2client.Action.FooSystem;
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

public class FooTray {

	// TODO: Code cleanup

	private Tray tray;
	private Display display;
	private FooInterfaceWindow window;

	public FooTray(FooInterfaceWindow window) {
		this.window = window;
	}

	public void initialize() {
		if (getTray() != null) {
			TrayItem item = new TrayItem(tray, SWT.NONE);
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

			item.setImage(image);

			final FooMenu menu = new FooMenu(new Shell(getDisplay(), SWT.NONE),
					SWT.POP_UP);

			FooMenuItem menuPlay = new FooMenuItem(menu, SWT.PUSH);
			menuPlay.setText("Play");
			menuPlay.addAction(FooPlayback.ActionPlay(0));


			FooMenuItem menuPause = new FooMenuItem(menu, SWT.PUSH);
			menuPause.setText("Pause");
			menuPlay.addAction(FooPlayback.ActionPause(0));

			FooMenuItem menuStop = new FooMenuItem(menu, SWT.PUSH);
			menuStop.setText("Stop");
			menuPlay.addAction(FooPlayback.ActionStop(0));

			new FooMenuItem(menu, SWT.SEPARATOR);

			FooMenuItem menuNext = new FooMenuItem(menu, SWT.PUSH);
			menuNext.setText("Next");
			menuPlay.addAction(FooPlayback.ActionNext(0));

			FooMenuItem menuPrev = new FooMenuItem(menu, SWT.PUSH);
			menuPrev.setText("Prev");
			menuPlay.addAction(FooPlayback.ActionPrev(0));

			new FooMenuItem(menu, SWT.SEPARATOR);

			FooMenuItem menuExit = new FooMenuItem(menu, SWT.PUSH);
			menuExit.setText("Exit");
			menuPlay.addAction(FooSystem.ActionExit(0));

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
