package org.dyndns.schuschu.xmms2client.action;

import org.dyndns.schuschu.xmms2client.backend.FooPluginBackendMedia;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class FooPluginActionFilter implements FooInterfaceAction {

	private FooPluginBackendMedia backend;

	KeyAdapter key = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.keyCode) {
			case SWT.CR:
				enqueu();
				break;
			}
		}
	};

	MouseAdapter mouse = new MouseAdapter() {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			enqueu();
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
