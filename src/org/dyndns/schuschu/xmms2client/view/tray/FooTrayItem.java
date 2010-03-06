package org.dyndns.schuschu.xmms2client.view.tray;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceMenu;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceDecorations;
import org.dyndns.schuschu.xmms2client.loader.FooSWT;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.w3c.dom.Element;

public class FooTrayItem implements FooInterfaceMenu, FooInterfaceDecorations {

	private TrayItem item;
	private Image image;
	private FooMenu menu = null;
	private Shell shell;
	Tray tray;

	public FooTrayItem(int style) {
		tray = Display.getDefault().getSystemTray();
		shell = new Shell(Display.getCurrent(),SWT.NONE);
		// TODO: checks
		item = new TrayItem(tray, style);
		createItem();
	}

	public FooTrayItem() {
		this(SWT.NONE);
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
				if (FooSWT.main != null) {
					FooSWT.main.toggleVisible();
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
				FooTrayItem item = new FooTrayItem();

				FooFactory.putView(name, item);

				return item;

			}
		};

		FooFactory.factories.put("FooTrayItem", factory);
	}

	@Override
	public Decorations getDecorations() {
		return shell;
	}

	@Override
	public void setMenubar(FooMenu menu) {
		shell.setMenuBar(menu.getMenu());
		
	}
}
