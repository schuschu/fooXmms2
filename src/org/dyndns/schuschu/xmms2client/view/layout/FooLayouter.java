package org.dyndns.schuschu.xmms2client.view.layout;

import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooFactorySub;
import org.w3c.dom.Element;

public class FooLayouter {
	public static Object createLayout(Element element) {

		// type is the name of the backend which contains the action. if none is
		// specified the next (hirachical up) backend will be taken
		String type = element.getAttribute("type");

		// TODO: dependencies
		if (!element.getNodeName().equals("layoutdata")) {
			return null;
		}

		// TODO: move debug
		FooFactorySub sub = FooFactory.factories.get(type);
		if (sub == null) {
			return null;
		}

		return sub.create(element);
	}
}
