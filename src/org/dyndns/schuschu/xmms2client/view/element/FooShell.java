package org.dyndns.schuschu.xmms2client.view.element;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackendText;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceMenu;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceDecorations;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceText;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.view.layout.FooLayoutType;
import org.dyndns.schuschu.xmms2client.view.menu.FooMenu;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;

public class FooShell implements FooInterfaceComposite, FooInterfaceMenu,
		FooInterfaceDecorations, FooInterfaceText {

	private Shell shell;
	private Point location;
	private boolean maximized;
	private FooInterfaceBackendText backend;

	public FooShell() {
		shell = new Shell(Display.getDefault());
	}

	public void open() {
		shell.open();
	}

	public void close() {
		shell.close();
	}

	@Override
	public Composite getComposite() {
		return shell;
	}

	public void setLayout(Layout layout) {
		shell.setLayout(layout);
	}

	public boolean getMaximized() {
		return shell.getMaximized();
	}

	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
		shell.setMaximized(maximized);
	}

	public void setVisible(boolean visible) {
		shell.setVisible(visible);
	}

	public boolean getVisible() {
		return shell.getVisible();
	}

	public void toggleVisible() {
		if (getVisible()) {
			location = getLocation();
			location = location.x < 0 || location.y < 0 ? null : location;
			maximized = getMaximized();
		} else {
			if (location != null) {
				setLocation(location);
			}
			setMaximized(maximized);
		}
		setVisible(!getVisible());
	}

	public Point getLocation() {
		return shell.getLocation();
	}

	public void setLocation(Point location) {
		shell.setLocation(location);
	}

	public void setText(String string) {
		if (!shell.isDisposed()) {
			shell.setText(string);
		}
	}

	public void setSize(Point size) {
		shell.setSize(size);
	}

	public void setImage(Image image) {
		shell.setImage(image);
	}

	public boolean isDisposed() {
		return shell.isDisposed();
	}

	public Shell getShell() {
		return shell;
	}

	public Rectangle getBounds() {
		return shell.getBounds();
	}

	public static void registerFactory() {
		// VIEW
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public Object create(Element element) {

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// title of the window, default fooXmms2
				String text = element.hasAttribute("text") ? element
						.getAttribute("text") : "fooXmms2";

				String widthstring = element.hasAttribute("width") ? element
						.getAttribute("width") : "1000";
				String heigthstring = element.hasAttribute("heigth") ? element
						.getAttribute("heigth") : "600";
				int width = Integer.parseInt(widthstring);
				int heigth = Integer.parseInt(heigthstring);

				// gets the layout of the composite/shell , default is
				// FillLayout
				String layoutstring = element.hasAttribute("layout") ? element
						.getAttribute("layout") : "FillLayout";

				boolean maximized = element.hasAttribute("maximized") ? element
						.getAttribute("maximized").equals("true")
						|| (element.getAttribute("maximized").equals("global") && FooLoader
								.getBooleanArg("maximized")) : false;
				boolean visible = element.hasAttribute("visible") ? element
						.getAttribute("visible").equals("true")
						|| (element.getAttribute("visible").equals("global") && FooLoader
								.getBooleanArg("visible")) : true;

				debug("creating FooShell " + name);

				FooShell shell = new FooShell();

				shell.setSize(new Point(width, heigth));

				// position of the window, has defualts
				Monitor primary = Display.getDefault().getPrimaryMonitor();
				Rectangle bounds = primary.getBounds();
				Rectangle rect = shell.getBounds();

				// absolute values, one to default and windowmanager does the
				// magic
				String xstring = element.hasAttribute("x") ? element.getAttribute("x")
						: "default";
				String ystring = element.hasAttribute("y") ? element.getAttribute("y")
						: "default";

				if (!ystring.equals("default") || !xstring.equals("default")) {

					int x = xstring.equals("center") ? bounds.x
							+ (bounds.width - rect.width) / 2 : Integer.parseInt(xstring);
					int y = ystring.equals("center") ? bounds.y
							+ (bounds.height - rect.height) / 2 : Integer.parseInt(ystring);

					// offset values
					String xoffstring = element.hasAttribute("xoff") ? element
							.getAttribute("xoff") : "0";
					String yoffstring = element.hasAttribute("yoff") ? element
							.getAttribute("yoff") : "0";
					int xoff = Integer.parseInt(xoffstring);
					int yoff = Integer.parseInt(yoffstring);

					shell.setLocation(new Point(x + xoff, y + yoff));

				}
				shell.setText(text);

				shell.setMaximized(maximized);
				shell.setVisible(visible);

				shell.setLayout(createLayout(layoutstring));

				Image image = null;

				// TODO: image from xml
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

				shell.setImage(image);

				FooFactory.putView(name, shell);

				return shell;
			}

			private Layout createLayout(String layoutstring) {
				try {
					switch (FooLayoutType.valueOf(layoutstring)) {
					case FillLayout:
						return new FillLayout();
					case FormLayout:
						return new FormLayout();
					}
				} catch (IllegalArgumentException e) {
					// Thats not an enum!
				}
				return null;
			}
		};

		FooFactory.factories.put("FooShell", factory);
	}

	@Override
	public void setMenu(FooMenu menu) {
		shell.setMenu(menu.getMenu());
	}

	@Override
	public Decorations getDecorations() {
		return shell;
	}

	@Override
	public void setMenubar(FooMenu menu) {
		shell.setMenuBar(menu.getMenu());
	}

	@Override
	public FooInterfaceBackendText getBackend() {
		return backend;
	}

	@Override
	public void setBackend(FooInterfaceBackendText backend) {
		this.backend = backend;

	}

}
