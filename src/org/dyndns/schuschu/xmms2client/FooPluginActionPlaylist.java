package org.dyndns.schuschu.xmms2client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import se.fnord.xmms2.client.Client;

public class FooPluginActionPlaylist implements FooInterfaceAction {

	private Client client;
	private FooPluginBackendMediaPlaylist backend;
	private FooInterfaceViewElement view;

	KeyAdapter key = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				play();
				break;
			case KeyEvent.VK_DELETE:
				backend.removeSelection();
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
				play();
				break;
			default:
				super.mouseClicked(e);
				break;
			}
		}
	};

	private void play() {
		backend.playSelection();
	}

	public FooPluginActionPlaylist(FooPluginBackendMediaPlaylist backend) {
		initialize(backend, backend.getView());
	}

	public FooPluginActionPlaylist(FooPluginBackendMediaPlaylist backend,
			FooInterfaceViewElement view) {
		initialize(backend, view);
	}

	private void initialize(FooPluginBackendMediaPlaylist backend,
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

	public void setBackend(FooPluginBackendMediaPlaylist backend) {
		this.backend = backend;
	}

	public FooPluginBackendMediaPlaylist getBackend() {
		return backend;
	}

	public void setView(FooInterfaceViewElement view) {
		this.view = view;
	}

	public FooInterfaceViewElement getView() {
		return view;
	}

}
