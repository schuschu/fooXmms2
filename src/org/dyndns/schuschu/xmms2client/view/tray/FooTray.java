package org.dyndns.schuschu.xmms2client.view.tray;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.Action.FooPlayback;
import org.dyndns.schuschu.xmms2client.Action.FooSource;
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

	private Tray tray;
	private Display display;
	private final FooInterfaceWindow window;
	private final FooMenu menu;
	private Image image;

	public FooTray(FooInterfaceWindow window) {
		this.window = window;
		menu = new FooMenu(new Shell(getDisplay(), SWT.NONE), SWT.POP_UP);
	}

	public void initialize() {
		if (getTray() != null) {
			createItem();
			createMenuPlay();
			createMenuPause();
			createMenuStop();
			createMenuPrev();
			createMenuNext();
			createSeperator();
			createMenuExit();
		}
	}

	public void createItem() {
		TrayItem item = new TrayItem(tray, SWT.NONE);

		loadImage();
		item.setImage(image);
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

	public void loadImage() {
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
	}

	public void createSeperator() {
		new FooMenuItem(menu, SWT.SEPARATOR);
	}

	public void createMenuPlay() {
		FooMenuItem menuPlay = new FooMenuItem(menu, SWT.PUSH);
		menuPlay.setText("Play");
		menuPlay.addAction(FooSource.MOUSE, FooPlayback.ActionPlay(0));
	}

	public void createMenuPause() {
		FooMenuItem menuPause = new FooMenuItem(menu, SWT.PUSH);
		menuPause.setText("Pause");
		menuPause.addAction(FooSource.MOUSE, FooPlayback.ActionPause(0));
	}

	public void createMenuStop() {
		FooMenuItem menuStop = new FooMenuItem(menu, SWT.PUSH);
		menuStop.setText("Stop");
		menuStop.addAction(FooSource.MOUSE, FooPlayback.ActionStop(0));
	}

	public void createMenuNext() {
		FooMenuItem menuNext = new FooMenuItem(menu, SWT.PUSH);
		menuNext.setText("Next");
		menuNext.addAction(FooSource.MOUSE, FooPlayback.ActionNext(0));
	}

	public void createMenuPrev() {
		FooMenuItem menuPrev = new FooMenuItem(menu, SWT.PUSH);
		menuPrev.setText("Prev");
		menuPrev.addAction(FooSource.MOUSE, FooPlayback.ActionPrev(0));
	}

	public void createMenuExit() {
		FooMenuItem menuExit = new FooMenuItem(menu, SWT.PUSH);
		menuExit.setText("Exit");
		menuExit.addAction(FooSource.MOUSE, FooSystem.ActionExit(0));
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
