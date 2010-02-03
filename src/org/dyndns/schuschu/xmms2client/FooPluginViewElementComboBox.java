package org.dyndns.schuschu.xmms2client;

import se.fnord.xmms2.client.Client;

import java.awt.event.ItemEvent;
import java.util.Vector;

import javax.swing.JComboBox;

/*
 * TODO:
 * - better way to set POSSIBLE Values, either a global variable (not in list) some static vodoo or by parsing the format string 
 * - migrate to java plugin framework
 * - add Extensionpoints for doubleclick==enter action
 * - add Extensionpoints to integrate into Panels Extensionpoint
 * - add Extensionpoint for next Element
 * - sleep
 */

/**
 * @author schuschu
 * 
 */
public class FooPluginViewElementComboBox extends JComboBox implements
		FooInterfaceViewElement {

	/**
	 * I have no idea what that stupid thing is for...
	 */
	private static final long serialVersionUID = 3504234292360728173L;

	/**
	 * s All the information processing xmms2connection etc is done there
	 */
	private FooInterfaceBackend backend;

	private FooInterfaceAction action_manager;

	/**
	 * Default constructor
	 */
	public FooPluginViewElementComboBox() {
	}

	/**
	 * Constructor using a already defined backend
	 * 
	 * @param backend
	 */
	public FooPluginViewElementComboBox(FooInterfaceBackend backend) {
		setBackend(backend);
	}

	/**
	 * Constructor creating its own FooPluginBackendMedia
	 * 
	 * @param format
	 * @param filter
	 * @param client
	 */
	public FooPluginViewElementComboBox(String format, String filter,
			Client client) {
		super();
		setBackend(new FooPluginBackendMedia(format, filter, client, this));
	}

	private void initialize() {
		addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					backend.selectionChanged();
				}
			}
		});
	}

	@Override
	public int[] getIndices() {
		int[] indecies = new int[1];
		indecies[0] = super.getSelectedIndex();
		return indecies;
	}

	@Override
	public void setContent(Vector<String> content) {

		super.removeAllItems();

		for (String text : content) {
			super.addItem(text);
		}

	}

	@Override
	public void setBackend(FooInterfaceBackend backend) {
		this.backend = backend;
		initialize();
	}

	@Override
	public FooInterfaceBackend getBackend() {
		return backend;
	}

	@Override
	public void setSingleSelectionMode() {
		// nothing to do
	}

	@Override
	public void setActionManager(FooInterfaceAction action) {
		this.action_manager = action;

	}

	@Override
	public FooInterfaceAction getActionManager() {
		return action_manager;
	}

	@Override
	public void setSelection(int[] indices) {
		super.setSelectedIndex(indices[0]);
	}

}