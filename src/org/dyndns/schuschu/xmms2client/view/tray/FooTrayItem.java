package org.dyndns.schuschu.xmms2client.view.tray;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceMenu;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceDecorations;
import org.dyndns.schuschu.xmms2client.view.element.FooShell;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.w3c.dom.Element;

public class FooTrayItem implements FooInterfaceMenu, FooInterfaceDecorations {

	private TrayItem item;
	private Image image;
	private FooMenu menu = null;
	private FooShell shell;
	Tray tray;

	public FooTrayItem(FooShell shell, int style) {
		tray = Display.getDefault().getSystemTray();
		this.shell = shell;
		if (tray == null) {
			shell.setVisible(true);
		} else {
			item = new TrayItem(tray, style);
			createItem();
		}
	}

	public FooTrayItem(FooShell shell) {
		this(shell, SWT.NONE);
	}

	public TrayItem getItem() {
		return item;
	}

	@Override
	public void setMenu(FooMenu menu) {
		this.menu = menu;

	}

	private TrayItem createItem() {
		TrayItem item = new TrayItem(tray, SWT.NONE);

		loadImage();
		item.setImage(image);

		// TODO: use action
		item.addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (menu != null) {
					menu.setVisible(true);
				}
			}
		});

		// TODO: use action
		item.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				if (shell != null) {
					shell.toggleVisible();
				}
			}
		});

		return item;
	}

	private void loadImage() {
		InputStream stream = this.getClass().getResourceAsStream(
				"/pixmaps/xmms2-128.png");
		if (stream != null) {
			try {
				image = new Image(Display.getDefault(), stream);
			} catch (IllegalArgumentException e) {
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		} else {
			// TODO: find better way to do this
			image = new Image(Display.getDefault(), "pixmaps/xmms2-128.png");
		}
	}

	public static void registerFactory() {
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public Object create(Element element) {

				// name equals variable name, no default
				String name = element.getAttribute("name");

				debug("creating FooTray " + name);
				FooTrayItem item = new FooTrayItem(getShell(element));

				FooFactory.putView(name, item);

				return item;

			}

			private FooShell getShell(Element element) {
				Element root = element;
				do {
					root = (Element) root.getParentNode();
				} while (!root.getNodeName().equals("shell"));

				Object o = FooFactory.getView(root.getAttribute("name"));

				if (o instanceof FooShell) {
					return (FooShell) o;
				}
				return null;

			}
		};

		FooFactory.factories.put("FooTrayItem", factory);
	}

	@Override
	public Decorations getDecorations() {
		return shell.getShell();
	}

	@Override
	public void setMenubar(FooMenu menu) {
		shell.setMenubar(menu);

	}

	@Override
	public boolean isDisposed() {
		return item.isDisposed();
	}
}
