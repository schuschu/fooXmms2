package org.dyndns.schuschu.xmms2client.action;

import org.dyndns.schuschu.xmms2client.backend.FooBackendMediaPlaylist;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class FooActionPlaylist implements FooInterfaceAction {

	private FooBackendMediaPlaylist backend;

	KeyAdapter key = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.keyCode) {
			case SWT.CR:
				play();
				break;
			case SWT.DEL:
				backend.removeSelection();
				break;
			case SWT.ESC:
				deselect();
				break;
			}
		}
	};

	MouseAdapter mouse = new MouseAdapter() {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			play();
		}
	};

	private void play() {
		backend.playSelection();
	}

	private void deselect() {
		backend.getView().setSelection(new int[0]);
		backend.selectionChanged();
	}

	public FooActionPlaylist(FooBackendMediaPlaylist backend) {
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

	public void setBackend(FooBackendMediaPlaylist backend) {
		this.backend = backend;
	}

	public FooBackendMediaPlaylist getBackend() {
		return backend;
	}
}
