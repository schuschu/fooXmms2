package org.dyndns.schuschu.xmms2client.view.menu;

import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceMenu;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.w3c.dom.Element;

public class FooMenu {

	private Menu menu;

	public FooMenu() {
		setMenu(new Menu(FooWindow.SHELL.getShell()));
	}

	public FooMenu(FooInterfaceControl parent) {
		setMenu(new Menu(parent.getControl()));
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
	
	public static void registerFactory(){
		FooFactorySub factory = new FooFactorySub() {
			
			@Override
			public Object create(Element element) {
				//VIEW

				// name equals variable name, no default
				String name = element.getAttribute("name");
				
				// get the parent nodes name for view (since menu are always direct
				// below (hirachical) their view element)
				Element father =(Element) element.getParentNode();
				String view = father.getAttribute("name");

				debug("creating menu for " + view);

				FooMenu menu = new FooMenu();

				getView(view).setMenu(menu);
				
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
		};
		FooFactory.factories.put("FooMenu", factory);
	}
}
