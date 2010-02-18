package org.dyndns.schuschu.xmms2client.view.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class FooConfirmationDialog {
	public static int show(Shell shell, String message, String text) {
		MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
		mb.setText(text);
		mb.setMessage(message);
		return mb.open();
	}
}
