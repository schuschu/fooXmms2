package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.action.playlist.FooActionPlaylistDelete;
import org.dyndns.schuschu.xmms2client.action.playlist.FooActionPlaylistNew;
import org.dyndns.schuschu.xmms2client.action.playlist.FooActionPlaylistSave;
import org.dyndns.schuschu.xmms2client.action.playlist.FooActionPlaylistShuffle;
import org.dyndns.schuschu.xmms2client.action.playlist.FooActionPlaylistSort;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import se.fnord.xmms2.client.Client;

public class FooButtonsPlaylist {

	private Composite composite;
	private Client client;

	public FooButtonsPlaylist(Composite parent, int style, Client client) {
		this.setClient(client);
		this.setComposite(new Composite(parent, style));

		composite.setLayout(new FillLayout());

		FooButton sortButton = new FooButton(getComposite(), SWT.NONE);
		FooButton shuffleButton = new FooButton(getComposite(), SWT.NONE);
		FooButton newButton = new FooButton(getComposite(), SWT.NONE);
		FooButton deleteButton = new FooButton(getComposite(), SWT.NONE);
		FooButton saveButton = new FooButton(getComposite(), SWT.NONE);

		sortButton.setText("⤑");
		shuffleButton.setText("⥂");
		newButton.setText("+");
		deleteButton.setText("-");
		saveButton.setText("⎇");

		FooActionPlaylistSort sort = new FooActionPlaylistSort(sortButton,
				client);
		sort.addListeners();

		FooActionPlaylistShuffle stop = new FooActionPlaylistShuffle(
				shuffleButton, client);
		stop.addListeners();

		FooActionPlaylistNew newlist = new FooActionPlaylistNew(newButton,
				client);
		newlist.addListeners();

		// TODO: implemnt this
		FooActionPlaylistDelete delete = new FooActionPlaylistDelete(
				deleteButton, client);
		delete.addListeners();

		FooActionPlaylistSave save = new FooActionPlaylistSave(saveButton,
				client);
		save.addListeners();

	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public void setComposite(Composite composite) {
		this.composite = composite;
	}

	public Composite getComposite() {
		return composite;
	}

	public void setLayoutData(Object layoutData) {
		composite.setLayoutData(layoutData);
	}

}
