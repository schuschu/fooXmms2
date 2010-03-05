package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.view.FooStyle;
import org.dyndns.schuschu.xmms2client.view.layout.FooLayoutType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.w3c.dom.Element;

public class FooComposite implements FooInterfaceComposite {
	private Composite comp;

	public FooComposite(Composite parent) {
		this(parent, SWT.NONE);
	}

	public FooComposite(Composite parent, int style) {
		comp = new Composite(parent, style);
	}

	@Override
	public Composite getComposite() {
		return comp;
	}

	public void setLayout(Layout layout) {
		comp.setLayout(layout);
	}

	public static void registerFactory() {
		// VIEW

		FooFactorySub factory = new FooFactorySub() {

			@Override
			public Object create(Element element) {

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for parent (hirachical xml)
				Element father = (Element) element.getParentNode();
				String parent = father.getAttribute("name");

				// gets the layout of the composite/shell , default is
				// FillLayout
				String layoutstring = element.hasAttribute("layout") ? element
						.getAttribute("layout") : "FillLayout";

				// style attribute defines the look of the widget, default is
				// none
				int style = SWT.NONE;

				if (element.hasAttribute("style")) {
					String s = element.getAttribute("style");
					String[] p = s.split(" ");
					for (String x : p) {
						int i = FooStyle.valueOf(x).getCode();
						style = style | i;
					}
				}

				debug("creating Composite " + name + " with parent " + parent);
				FooComposite comp = new FooComposite(getComposite(parent),style);
				if (layoutstring != null) {
					comp.setLayout(createLayout(layoutstring));
				}
				FooFactory.putView(name, comp);
				return comp;

			}

			private Layout createLayout(String layoutstring) {
				try {
					switch (FooLayoutType.valueOf(layoutstring)) {
					case FillLayout:
						return new FillLayout();
					case FormLayout:
						return new FormLayout();
					}
				} catch (IllegalArgumentException e) {
					// Thats not an enum!
				}
				return null;
			}

			private Composite getComposite(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceComposite) {
					return ((FooInterfaceComposite) o).getComposite();
				}

				return null;
			}
		};

		FooFactory.factories.put("FooComposite", factory);
	}
}
