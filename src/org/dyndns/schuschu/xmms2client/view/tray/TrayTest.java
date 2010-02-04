package org.dyndns.schuschu.xmms2client.view.tray;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

//import net.sourceforge.atunes.gui.images.ImageLoader;

public class TrayTest {

	private JPopupMenu popupMenu;
	private JDialog trayParent;
	private TrayIcon trayIcon;
	private ActionListener leftClickListener, leftDoubleClickListener,
			rightClickListener;

	private void setTray() {
		popupMenu = new JPopupMenu();
		JMenuItem menuItem1 = new JMenuItem("Test 1");
		menuItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Test 1 angeklickt");
			}
		});
		popupMenu.add(menuItem1);
		JMenuItem menuItem2 = new JMenuItem("Test 2");
		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Test 2 angeklickt");
			}
		});
		popupMenu.add(menuItem2);
		popupMenu.addSeparator();
		JMenuItem menuItem3 = new JMenuItem("Beenden");
		menuItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		popupMenu.add(menuItem3);

		trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(
				"pixmaps/xmms2-48.png"));

		trayParent = new JDialog();
		trayParent.setTitle("Tray-Menu");

		trayParent.setSize(0, 0);
		trayParent.setUndecorated(true);
		trayParent.setAlwaysOnTop(true);
		trayParent.setVisible(false);

		setTrayPopUp(popupMenu);

		SystemTray systemTray = SystemTray.getSystemTray();
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void setTrayPopUp(JPopupMenu trayMenu) {
		popupMenu = trayMenu;

		popupMenu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				trayParent.setVisible(false);
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
		popupMenu.setVisible(true);
		popupMenu.setVisible(false);

		trayIcon.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (e.getClickCount() == 1 && leftClickListener != null)
						leftClickListener.actionPerformed(null);
					else if (e.getClickCount() == 2
							&& leftDoubleClickListener != null)
						leftDoubleClickListener.actionPerformed(null);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					if (SwingUtilities.isRightMouseButton(e)
							&& rightClickListener != null)
						rightClickListener.actionPerformed(null);
					showPopup(e.getPoint());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					if (SwingUtilities.isRightMouseButton(e)
							&& rightClickListener != null)
						rightClickListener.actionPerformed(null);
					showPopup(e.getPoint());
				}
			}
		});
	}

	private void showPopup(final Point p) {
		trayParent.setVisible(true);
		trayParent.toFront();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Point p2 = computeDisplayPoint(p.x, p.y, popupMenu
						.getPreferredSize());
				popupMenu.show(trayParent, p2.x - trayParent.getLocation().x,
						p2.y - trayParent.getLocation().y);
			};
		});
	}

	/**
	 * Compute the proper position for a popup
	 */
	private Point computeDisplayPoint(int x, int y, Dimension dim) {
		if (x - dim.width > 0)
			x -= dim.width;
		if (y - dim.height > 0)
			y -= dim.height;
		return new Point(x, y);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				new TrayTest().setTray();
			}
		});
	}
}