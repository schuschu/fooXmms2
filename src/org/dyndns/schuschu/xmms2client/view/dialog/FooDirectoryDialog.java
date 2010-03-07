package org.dyndns.schuschu.xmms2client.view.dialog;

import org.dyndns.schuschu.xmms2client.view.element.FooShell;
import org.eclipse.swt.widgets.DirectoryDialog;

public class FooDirectoryDialog {

	public static String show(FooShell shell){
		DirectoryDialog dd = new DirectoryDialog(shell.getShell());
        
        dd.setFilterPath(System.getProperty("user.home"));
        dd.setMessage("Please select a directory and click OK");
        
        return dd.open();
	}
	
	
	
}
