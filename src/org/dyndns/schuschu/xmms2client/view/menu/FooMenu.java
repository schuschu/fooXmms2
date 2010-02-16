package org.dyndns.schuschu.xmms2client.view.menu;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;

public class FooMenu {

	private Menu menu;

	// TODO: Interface for Control
	public FooMenu(Control parent) {
		setMenu(new Menu(parent));
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
}
