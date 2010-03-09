package org.dyndns.schuschu.xmms2client.factory;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.w3c.dom.Element;

public abstract class FooFactorySub {
	
	protected static final boolean DEBUG = FooLoader.getBooleanArg("debug");
	protected FooColor debugForeground = FooColor.WHITE;
	protected FooColor debugBackground = FooColor.BLUE;

	protected void debug(String message) {
		if (DEBUG) {
			if (FooLoader.DOUTPUT!=null) {
				FooLoader.DOUTPUT.setForeground(debugForeground);
				FooLoader.DOUTPUT.setBackground(debugBackground);
			}
			System.out.println("debug: FooFactory " + message);
		}
	}
	
	public abstract Object create(Element element);

}
