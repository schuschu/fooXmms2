package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class FooShell implements FooInterfaceComposite{
	private Shell shell;
	
	public FooShell(){
		shell = new Shell();
	}

	@Override
	public Composite getComposite() {
		return shell;
	}
	

}
