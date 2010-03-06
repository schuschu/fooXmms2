package org.dyndns.schuschu.xmms2client.loader;

import java.io.PrintStream;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.view.element.FooShell;
import org.eclipse.swt.widgets.Display;

public class FooSWT {
	// Initialization for SWT

	public static void init() {
		
		FooFactory.parse();

		Display.getDefault().update();
		
		FooShell main = (FooShell) FooFactory.getView("main");
				
		if(main==null){
			System.out.println("Please name the main shell main");
			System.exit(1);
		}
		
		while (!main.isDisposed()) {
			if (!Display.getDefault().readAndDispatch()) {
				Display.getDefault().sleep();
			}
		}
		Display.getDefault().dispose();
		FooLoader.CLIENT.stop();

		System.exit(0);

	}

	public static void createDebug() {
		if (FooLoader.DEBUG) {
			if (FooLoader.VISUAL) {
				PrintStream out = new PrintStream(new FooDebug());
				System.setOut(out);
				FooDebug.setForeground(FooColor.RED);
			}
			System.out.println("Welcome to fooXmms2");
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(FooColor.RED);
			}
			System.out.println("===================");
		}
	}

}
