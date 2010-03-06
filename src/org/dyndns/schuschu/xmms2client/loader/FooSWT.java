package org.dyndns.schuschu.xmms2client.loader;

import java.io.PrintStream;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;

public class FooSWT {
	// Initialization for SWT

	public static void init() {
		init(true, false);
	}

	public static FooInterfaceWindow main;
	
	public static void init(boolean show_on_start, boolean max_on_start) {
		
		main = new FooWindow(max_on_start);

		if (show_on_start) {
			main.setVisible(true);
		}

		main.loop();
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
