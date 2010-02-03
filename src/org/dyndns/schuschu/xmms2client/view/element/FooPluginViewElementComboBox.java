package org.dyndns.schuschu.xmms2client.view.element;

import se.fnord.xmms2.client.Client;

import java.awt.event.ItemEvent;
import java.util.Vector;

import javax.swing.JComboBox;

import org.dyndns.schuschu.xmms2client.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.backend.FooPluginBackendMedia;

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
	public void setSelection(int[] indices) {
		super.setSelectedIndex(indices[0]);
	}

}