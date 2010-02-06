package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.action.playback.FooActionPlayback;
import org.dyndns.schuschu.xmms2client.action.playback.FooPlaybackType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import se.fnord.xmms2.client.Client;

public class FooButtonsPlayback {

	private Composite composite;
	private Client client;

	public FooButtonsPlayback(Composite parent, int style, Client client) {
		this.setClient(client);
		this.setComposite(new Composite(parent, style));

		composite.setLayout(new FillLayout());

		FooButton prevButton = new FooButton(getComposite(), SWT.NONE);
		FooButton stopButton = new FooButton(getComposite(), SWT.NONE);
		FooButton pauseButton = new FooButton(getComposite(), SWT.NONE);
		FooButton playButton = new FooButton(getComposite(), SWT.NONE);
		FooButton nextButton = new FooButton(getComposite(), SWT.NONE);

		prevButton.setText("◂◂");
		stopButton.setText("◾");
		pauseButton.setText("▮▮");
		playButton.setText("►");
		nextButton.setText("▸▸");

		FooActionPlayback prev = new FooActionPlayback(FooPlaybackType.PREV,
				prevButton, client);
		prev.addListeners();

		FooActionPlayback stop = new FooActionPlayback(FooPlaybackType.STOP,
				stopButton, client);
		stop.addListeners();

		FooActionPlayback pause = new FooActionPlayback(FooPlaybackType.PAUSE,
				pauseButton, client);
		pause.addListeners();

		FooActionPlayback play = new FooActionPlayback(FooPlaybackType.PLAY,
				playButton, client);
		play.addListeners();

		FooActionPlayback next = new FooActionPlayback(FooPlaybackType.NEXT,
				nextButton, client);
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
