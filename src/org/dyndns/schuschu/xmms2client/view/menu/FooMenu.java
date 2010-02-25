package org.dyndns.schuschu.xmms2client.view.menu;

import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;

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
}
