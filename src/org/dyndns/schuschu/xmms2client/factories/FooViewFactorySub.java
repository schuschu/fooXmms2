package org.dyndns.schuschu.xmms2client.factories;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.w3c.dom.Element;

public abstract class FooViewFactorySub {
	
	protected static final boolean DEBUG = FooLoader.DEBUG;
	protected FooColor debugForeground = FooColor.WHITE;
	protected FooColor debugBackground = FooColor.BLUE;

	protected void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(debugForeground);
				FooDebug.setBackground(debugBackground);
			}
			System.out.println("debug: BackendFactory " + message);
		}
	}

	protected abstract Object create(Element element);

}
