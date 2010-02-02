package org.dyndns.schuschu.xmms2client;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * @author thomas
 * 
 */
public class FooPluginViewTrayicon {

	/**
	 * constructor
	 * 
	 * @param window
	 *        instance of main window
	 * 
	 */
	public FooPluginViewTrayicon(final FooPluginWindowDefault window) {

		//
		SystemTray tray = SystemTray.getSystemTray();

		// sadly only supports gif, jpg or png
		Image image = Toolkit.getDefaultToolkit().getImage("pixmaps/xmms2-48.png");

		MouseListener mouseListener = new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				// on mouse click toggle visibility
				if (window.isVisible()) {
					// set unvisible
					window.setVisible(false);
				} else {
					// set visible
					window.setVisible(true);
					// maximize window
					//window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
			}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {}

			public void mouseReleased(MouseEvent e) {}
		};

		TrayIcon icon = new TrayIcon(image);

		icon.setImageAutoSize(true);
		icon.addMouseListener(mouseListener);

		try {
			tray.add(icon);
		} catch (AWTException e) {
			// if no tray is present
			// start main window maximized

			// set visible
			window.setVisible(true);
			// maximize window
			//window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}
}
