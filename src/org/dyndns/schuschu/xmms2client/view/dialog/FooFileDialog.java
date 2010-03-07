package org.dyndns.schuschu.xmms2client.view.dialog;

import org.dyndns.schuschu.xmms2client.view.element.FooShell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class FooFileDialog {
	public static String show(FooShell shell, String filename){
		return show(shell.getShell(),filename);
	}
	
	public static String show(Shell shell, String filename){
		FileDialog fd = new FileDialog(shell, SWT.SAVE);
		fd.setText(filename);
		fd.setFilterPath(System.getProperty("user.home"));
		fd.setFileName("fooXmms2.log");
		return fd.open();
	}
}
