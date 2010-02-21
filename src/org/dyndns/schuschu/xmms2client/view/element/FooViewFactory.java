package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

public class FooViewFactory {

	private FooWindow window;

	public FooViewFactory(FooWindow window) {
		this.window = window;
	}

	public Object create(Element element) {
		String type = FooXML.getTagValue("type", element);
		String name = FooXML.getTagValue("name", element);

		Element father = (Element) element.getParentNode();
		String parent = FooXML.getTagValue("name", father);

		switch (FooViewType.fromString(type)) {
		case SashForm:
			SashForm sash = new SashForm(getParent(parent), SWT.NONE);
			window.items.put(name, sash);
			return sash;
		case Composite:
			Composite comp = new Composite(getParent(parent), SWT.NONE);
			window.items.put(name, comp);
			return comp;
		case FooList:
			FooList list = new FooList(getParent(parent));
			window.items.put(name, list);
			return list;
		case FooCombo:
			FooCombo combo = new FooCombo(getParent(parent));
			window.items.put(name, combo);
			return combo;
		case FooTable:
			FooTable table = new FooTable(getParent(parent));
			window.items.put(name, table);
			return table;
		}

		return null;

	}

	public Composite getParent(String s) {
		Object o = window.items.get(s);
		if (o instanceof Composite) {
			return (Composite) o;
		}
		return null;
	}
}
