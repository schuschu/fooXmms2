package org.dyndns.schuschu.xmms2client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FooPluginActionFilter implements FooInterfaceAction {

	private FooPluginBackendMedia backend;

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

	public FooPluginActionFilter() {
	}

	public FooPluginActionFilter(FooPluginBackendMedia backend) {
		setBackend(backend);
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

	public void setBackend(FooPluginBackendMedia backend) {
		this.backend = backend;
	}

	public FooPluginBackendMedia getBackend() {
		return backend;
	}
}
