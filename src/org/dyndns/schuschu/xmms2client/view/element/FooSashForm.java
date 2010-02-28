package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Element;

public class FooSashForm implements FooInterfaceControl, FooInterfaceComposite {

	private SashForm sash;

	public FooSashForm(Composite parent) {
		this(parent, SWT.NONE);
	}

	public FooSashForm(Composite parent, int style) {
		sash = new SashForm(parent, style);
	}

	@Override
	public Control getControl() {
		return sash;
	}

	@Override
	public Composite getComposite() {
		return sash;
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

				debug("creating SashForm " + name + " with parent " + parent);
				FooSashForm sash = new FooSashForm(getComposite(parent));
				FooFactory.putView(name, sash);
				return sash;
			}
			private Composite getComposite(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceComposite) {
					return ((FooInterfaceComposite) o).getComposite();
				}

				return null;
			}
		};
		
		FooFactory.factories.put("FooSashForm", factory);
	}

}
