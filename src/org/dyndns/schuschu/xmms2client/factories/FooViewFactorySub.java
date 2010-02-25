package org.dyndns.schuschu.xmms2client.factories;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.eclipse.swt.widgets.Composite;
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
	
	// TODO: ??? really here
	protected Composite getComposite(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceComposite) {
			return ((FooInterfaceComposite) o).getComposite();
		}
		if (o instanceof Composite) {
			return (Composite) o;
		}

		return null;
	}

	protected abstract Object create(Element element);

}
