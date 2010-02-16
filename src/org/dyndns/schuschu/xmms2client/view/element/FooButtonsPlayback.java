package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.newAction.FooPlayback;
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

		// TODO: icons
		prevButton.setText("◂◂");
		stopButton.setText("◾");
		pauseButton.setText("▮▮");
		playButton.setText("►");
		nextButton.setText("▸▸");

		prevButton.addAction(FooPlayback.ActionPrev(0));
		stopButton.addAction(FooPlayback.ActionStop(0));
		pauseButton.addAction(FooPlayback.ActionPause(0));
		playButton.addAction(FooPlayback.ActionPlay(0));
		nextButton.addAction(FooPlayback.ActionNext(0));

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
