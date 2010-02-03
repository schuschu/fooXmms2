package org.dyndns.schuschu.xmms2client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import se.fnord.xmms2.client.Client;

public class FooPluginActionFilter implements FooInterfaceAction {

	private Client client;
	private FooPluginBackendMedia backend;
	private FooInterfaceViewElement view;

	KeyAdapter key = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				enqueu();
				break;
			default:
				super.keyTyped(e);
				break;
			}
		}
	};

	MouseAdapter mouse = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			switch (e.getClickCount()) {
			case 2:
				enqueu();
				break;
			default:
				super.mouseClicked(e);
				break;
			}
		}
	};

	private void enqueu() {
		backend.enqueuSelection();
	}

	public FooPluginActionFilter(FooPluginBackendMedia backend) {
		initialize(backend, backend.getView());
	}

	public FooPluginActionFilter(FooPluginBackendMedia backend,
			FooInterfaceViewElement view) {
		initialize(backend, view);
	}

	private void initialize(FooPluginBackendMedia backend,
			FooInterfaceViewElement view) {

		setClient(client);
		setBackend(backend);
		setView(view);
	}

	public void addListeners() {
		view.addKeyListener(key);
		view.addMouseListener(mouse);
	}

	public void removeListeners() {
		view.removeKeyListener(key);
		view.removeMouseListener(mouse);
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public void setBackend(FooPluginBackendMedia backend) {
		this.backend = backend;
	}

	public FooPluginBackendMedia getBackend() {
		return backend;
	}

	public void setView(FooInterfaceViewElement view) {
		this.view = view;
	}

	public FooInterfaceViewElement getView() {
		return view;
	}

}
