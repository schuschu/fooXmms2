package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.Action.FooPlaylist;
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

		sortButton.addAction(FooPlaylist.ActionSort(0));
		shuffleButton.addAction(FooPlaylist.ActionShuffle(0));
		newButton.addAction(FooPlaylist.ActionNew(0));
		deleteButton.addAction(FooPlaylist.ActionDelete(0));
		saveButton.addAction(FooPlaylist.ActionSava(0));
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
