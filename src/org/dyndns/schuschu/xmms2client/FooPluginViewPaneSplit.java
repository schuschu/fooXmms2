package org.dyndns.schuschu.xmms2client;

import java.awt.Component;

import javax.swing.JSplitPane;

/**
 * @author schuschu
 *
 */
public class FooPluginViewPaneSplit extends JSplitPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5529515738227924299L;

	public FooPluginViewPaneSplit() {
	}

	public FooPluginViewPaneSplit(int newOrientation) {
		super(newOrientation);
	}

	public FooPluginViewPaneSplit(int newOrientation, boolean newContinuousLayout) {
		super(newOrientation, newContinuousLayout);
	}

	public FooPluginViewPaneSplit(int newOrientation, Component newLeftComponent,
			Component newRightComponent) {
		super(newOrientation, newLeftComponent, newRightComponent);
	}

	public FooPluginViewPaneSplit(int newOrientation, boolean newContinuousLayout,
			Component newLeftComponent, Component newRightComponent) {
		super(newOrientation, newContinuousLayout, newLeftComponent,
				newRightComponent);
	}

}
