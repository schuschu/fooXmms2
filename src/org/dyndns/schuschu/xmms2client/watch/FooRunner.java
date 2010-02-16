package org.dyndns.schuschu.xmms2client.watch;

import org.eclipse.swt.widgets.Display;

public class FooRunner {
	public static void run(Runnable r) {
		Display.getDefault().asyncExec(r);
	}
}
