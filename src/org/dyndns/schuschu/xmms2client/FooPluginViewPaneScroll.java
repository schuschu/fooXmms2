package org.dyndns.schuschu.xmms2client;

import java.awt.Component;

import javax.swing.JScrollPane;

/**
 * @author schuschu
 *
 */
public class FooPluginViewPaneScroll extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8524389343625042374L;

	public FooPluginViewPaneScroll() {
		super();
	}

	public FooPluginViewPaneScroll(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
	}

	public FooPluginViewPaneScroll(Component view) {
		super(view);
	}

	public FooPluginViewPaneScroll(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
	}

}
