package org.dyndns.schuschu.xmms2client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FooPluginActionPlaylist implements FooInterfaceAction {

	private FooPluginBackendMediaPlaylist backend;

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
		this.setBackend(backend);
	}

	public void addListeners() {
		if (backend != null) {
			backend.getView().addKeyListener(key);
			backend.getView().addMouseListener(mouse);
		}
	}

	public void removeListeners() {
		if (backend != null) {
			backend.getView().removeKeyListener(key);
			backend.getView().removeMouseListener(mouse);
		}
	}

	public void setBackend(FooPluginBackendMediaPlaylist backend) {
		this.backend = backend;
	}

	public FooPluginBackendMediaPlaylist getBackend() {
		return backend;
	}
}
