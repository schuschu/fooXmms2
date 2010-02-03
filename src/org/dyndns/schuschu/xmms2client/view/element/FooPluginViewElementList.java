package org.dyndns.schuschu.xmms2client.view.element;

import se.fnord.xmms2.client.Client;

import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.dyndns.schuschu.xmms2client.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.backend.FooPluginBackendMedia;

/**
 * @author schuschu
 * 
 */
public class FooPluginViewElementList extends JList implements
		FooInterfaceViewElement {

	/**
	 * I have no idea what that stupid thing is for...
	 */
	private static final long serialVersionUID = 167219492048930052L;

	/**
	 * All the information processing xmms2connection etc is done there
	 */
	private FooInterfaceBackend backend;

	/**
	 * Default constructor
	 */
	public FooPluginViewElementList() {
	}

	/**
	 * Constructor using a already defined backend
	 * 
	 * @param backend
	 */
	public FooPluginViewElementList(FooInterfaceBackend backend) {
		setBackend(backend);
	}

	/**
	 * Constructor creating its own FooPluginBackendMedia
	 * 
	 * @param format
	 * @param filter
	 * @param client
	 */
	public FooPluginViewElementList(String format, String filter, Client client) {
		super();
		setBackend(new FooPluginBackendMedia(format, filter, client, this));
	}

	private void initialize() {
		addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				// this prevents multiple events to fire when a
				// change occurs
				if (!e.getValueIsAdjusting()) {
					backend.selectionChanged();
				}
			}
		});
	}

	@Override
	public int[] getIndices() {
		return super.getSelectedIndices();
	}

	@Override
	public void setContent(Vector<String> content) {
		super.setListData(content);

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
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@Override
	public void setSelection(int[] indices) {
		super.setSelectedIndices(indices);
	}

}