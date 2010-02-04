package org.dyndns.schuschu.xmms2client.view.menu;

import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.MenuShortcut;

import org.dyndns.schuschu.xmms2client.action.FooInterfaceViewClickable;

public class FooPluginViewMenuItem extends MenuItem implements
		FooInterfaceViewClickable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7563261116808199781L;

	public FooPluginViewMenuItem() throws HeadlessException {
		super();
	}

	public FooPluginViewMenuItem(String label, MenuShortcut s)
			throws HeadlessException {
		super(label, s);
	}

	public FooPluginViewMenuItem(String label) throws HeadlessException {
		super(label);
	}

}
