package org.dyndns.schuschu.xmms2client.view.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class FooMessageDialog {
	public static void show(Shell shell, String message, String text) {
		MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		mb.setText(text);
		mb.setMessage(message);
		mb.open();
	}
}
