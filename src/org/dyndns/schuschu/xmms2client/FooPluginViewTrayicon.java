package org.dyndns.schuschu.xmms2client;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Playback;

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
	private MenuItem miPlay = null;
	private MenuItem miStop = null;
	private MenuItem miExit = null;

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

			// TODO: Replace with action
			MouseAdapter mouseAdapter = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// on mouse click toggle visibility
					if (getWindow().isVisible()) {
						// set unvisible
						getWindow().setVisible(false);
					} else {
						// set visible
						getWindow().setVisible(true);
						// maximize window
						// window.setExtendedState(JFrame.MAXIMIZED_BOTH);
					}
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
			popup.add(getMiPlay());
			popup.add(getMiStop());
			popup.add(getMiExit());
		}
		return popup;
	}

	// TODO: MenuItem->Plugin, separate action
	public MenuItem getMiPlay() {
		miPlay = new MenuItem("Play");
		miPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Playback.play().executeSync(getClient());
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
					e1.printStackTrace();
				}
			}
		});
		return miPlay;
	}

	// TODO: MenuItem->Plugin, separate action
	public MenuItem getMiStop() {
		miStop = new MenuItem("Stop");
		miStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Playback.stop().executeSync(getClient());
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
					e1.printStackTrace();
				}
			}
		});
		return miStop;
	}

	// TODO: MenuItem->Plugin, separate action
	public MenuItem getMiExit() {
		if (miExit == null) {
			miExit = new MenuItem("Exit");
			miExit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return miExit;
	}

	public FooPluginViewTrayicon(final FooPluginWindowDefault window,
			final Client client, boolean hidden) {
		setClient(client);
		setWindow(window);
		if(!hidden || !isSupported()){
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
