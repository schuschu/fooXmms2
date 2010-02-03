package org.dyndns.schuschu.xmms2client.view.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.dyndns.schuschu.xmms2client.action.playback.FooPluginActionPlaybackExit;
import org.dyndns.schuschu.xmms2client.action.playback.FooPluginActionPlaybackPlay;
import org.dyndns.schuschu.xmms2client.action.playback.FooPluginActionPlaybackStop;
import org.dyndns.schuschu.xmms2client.view.menu.FooPluginViewMenuItem;
import org.dyndns.schuschu.xmms2client.view.window.FooPluginWindowDefault;

import se.fnord.xmms2.client.Client;

/**
 * @author thomas
 * @author schuschu
 * 
 */
public class FooPluginViewTrayicon implements FooInterfaceViewTray {

	private Client client;
	// TODO: WindowInterface, Observer anything but this
	private FooPluginWindowDefault window;

	private SystemTray tray = null;
	private Image image = null;
	private TrayIcon icon = null;

	private PopupMenu popup = null;
	private FooPluginViewMenuItem fpVMiPlay = null;
	private FooPluginViewMenuItem fpVMiStop = null;
	private FooPluginViewMenuItem fpVMiExit = null;

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public FooPluginWindowDefault getWindow() {
		return window;
	}

	public void setWindow(FooPluginWindowDefault window) {
		this.window = window;
	}

	public SystemTray getTray() {
		if (tray == null) {
			if (isSupported()) {
				tray = SystemTray.getSystemTray();
			} else {
				window.setVisible(true);
			}
		}
		return tray;
	}

	public Image getImage() {
		if (image == null) {
			// sadly only supports gif, jpg or png
			image = Toolkit.getDefaultToolkit()
					.getImage("pixmaps/xmms2-48.png");
		}
		return image;
	}

	public TrayIcon getIcon() {
		if (icon == null) {
			icon = new TrayIcon(getImage());

			MouseAdapter mouseAdapter = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// on mouse click toggle visibility
					getWindow().toggleVisible();
				}
			};

			icon.setImageAutoSize(true);
			icon.addMouseListener(mouseAdapter);
			icon.setPopupMenu(getPopup());
		}
		return icon;
	}

	public PopupMenu getPopup() {
		if (popup == null) {
			popup = new PopupMenu();
			popup.add(getFpVMiPlay());
			popup.add(getFpVMiStop());
			popup.add(getFpVMiExit());
		}
		return popup;
	}

	public FooPluginViewMenuItem getFpVMiPlay() {
		if (fpVMiPlay == null) {
			fpVMiPlay = new FooPluginViewMenuItem("Play");
			FooPluginActionPlaybackPlay fpAPlay = new FooPluginActionPlaybackPlay(
					fpVMiPlay, getClient());
			fpAPlay.addListeners();
		}
		return fpVMiPlay;
	}

	public FooPluginViewMenuItem getFpVMiStop() {
		if (fpVMiStop == null) {
			fpVMiStop = new FooPluginViewMenuItem("Stop");
			FooPluginActionPlaybackStop fpAStop = new FooPluginActionPlaybackStop(
					fpVMiStop, getClient());
			fpAStop.addListeners();
		}
		return fpVMiStop;
	}

	public FooPluginViewMenuItem getFpVMiExit() {
		if (fpVMiExit == null) {
			fpVMiExit = new FooPluginViewMenuItem("Exit");
			FooPluginActionPlaybackExit fpAExit = new FooPluginActionPlaybackExit(
					fpVMiExit);
			fpAExit.addListeners();
		}
		return fpVMiExit;
	}

	public FooPluginViewTrayicon(final FooPluginWindowDefault window,
			final Client client, boolean hidden) {
		setClient(client);
		setWindow(window);
		if (!hidden || !isSupported()) {
			getWindow().setVisible(true);
		}
	}

	@Override
	public boolean isSupported() {
		return SystemTray.isSupported();
	}

	@Override
	public void hide() {
		if (isSupported()) {
			getTray().remove(getIcon());
		}
	}

	@Override
	public void show() {
		if (isSupported()) {
			try {
				getTray().add(getIcon());
			} catch (AWTException e) {
				// if no tray is present
				// start main window maximized

				// set visible
				getWindow().setVisible(true);
				// maximize window
				// window.setExtendedState(JFrame.MAXIMIZED_BOTH);
			}
		}
	}
}
