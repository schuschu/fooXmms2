package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackNext;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackPause;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackPlay;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackPrev;
import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlaybackStop;
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

		FooActionPlaybackPrev prev = new FooActionPlaybackPrev(sortButton,
				client);
		prev.addListeners();

		FooActionPlaybackStop stop = new FooActionPlaybackStop(shuffleButton,
				client);
		stop.addListeners();

		FooActionPlaybackPause pause = new FooActionPlaybackPause(newButton,
				client);
		pause.addListeners();

		FooActionPlaybackPlay play = new FooActionPlaybackPlay(deleteButton,
				client);
		play.addListeners();

		FooActionPlaybackNext next = new FooActionPlaybackNext(saveButton,
				client);
		next.addListeners();

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
