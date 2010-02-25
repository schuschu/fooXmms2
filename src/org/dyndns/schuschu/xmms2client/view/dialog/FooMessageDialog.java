package org.dyndns.schuschu.xmms2client.view.dialog;

import org.dyndns.schuschu.xmms2client.view.element.FooShell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class FooMessageDialog {
	public static void show(FooShell shell, String message, String text) {
		show(shell.getShell(), message, text);
	}
	
	public static void show(Shell shell, String message, String text) {
		MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		mb.setText(text);
		mb.setMessage(message);
		mb.open();
	}
}
