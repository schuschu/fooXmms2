package org.dyndns.schuschu.xmms2client.view.menu;

import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceMenu;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceDecorations;
import org.dyndns.schuschu.xmms2client.view.FooStyle;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.w3c.dom.Element;

public class FooMenu implements FooInterfaceMenu {

	private Menu menu;

	public FooMenu() {
		setMenu(new Menu(FooWindow.SHELL.getShell()));
	}

	public FooMenu(FooInterfaceControl parent) {
		setMenu(new Menu(parent.getControl()));
	}

	public FooMenu(FooInterfaceDecorations parent, int style) {
		setMenu(new Menu(parent.getDecorations(), style));
	}
	
	public FooMenu(Decorations parent, int style) {
		setMenu(new Menu(parent, style));
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setVisible(boolean visible) {
		menu.setVisible(visible);
	}

	public static void registerFactory() {
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public Object create(Element element) {
				// VIEW

				FooMenu menu = null;

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for view (since menu are always
				// direct
				// below (hirachical) their view element)
				Element father = (Element) element.getParentNode();
				String view = father.getAttribute("name");

				// style attribute defines the look of the widget, default is
				// none
				if (element.hasAttribute("style")) {
					String string = element.getAttribute("style");
					int style = FooStyle.valueOf(string).getCode();
					
					switch (style) {
					case SWT.BAR:
						debug("creating menubar for " + view);
						menu = new FooMenu(
								getDecoration(view), style);
						getDecoration(view).setMenubar(menu);
						break;
					case SWT.DROP_DOWN:
						debug("creating dropdown menu for " + view);
						menu= new FooMenu(FooWindow.SHELL, style);
						getView(view).setMenu(menu);
					}

				} else {

					debug("creating menu for " + view);

					menu = new FooMenu();
					getView(view).setMenu(menu);
				}
				
				FooFactory.putView(name, menu);
				
				return menu;
			}

			private FooInterfaceMenu getView(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceMenu) {
					return (FooInterfaceMenu) o;
				}
				return null;
			}

			private FooInterfaceDecorations getDecoration(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceDecorations) {
					return (FooInterfaceDecorations) o;
				}
				return null;
			}

		};
		FooFactory.factories.put("FooMenu", factory);
	}

	@Override
	public void setMenu(FooMenu menu) {
		menu.setMenu(menu);
	}
}
