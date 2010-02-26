package org.dyndns.schuschu.xmms2client.view.layout;

import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Element;

public class FooFormData {

	public static void registerFactory(){
		FooFactorySub factory = new FooFactorySub() {
			
			@Override
			protected Object create(Element element) {

				if (element != null) {
					// TODO: other layouts
					FormData data = new FormData();
					String top = element.getAttribute("top");
					String bottom = element.getAttribute("bottom");
					String left = element.getAttribute("left");
					String right = element.getAttribute("right");

					if (getControl(top) != null) {
						data.top = new FormAttachment(getControl(top));
					} else {
						try {
							data.top = new FormAttachment(Integer.parseInt(top));
						} catch (NumberFormatException e) {

						}
					}

					if (getControl(bottom) != null) {
						data.bottom = new FormAttachment(getControl(bottom));
					} else {
						try {
							data.bottom = new FormAttachment(Integer.parseInt(bottom));
						} catch (NumberFormatException e) {

						}
					}

					if (getControl(left) != null) {
						data.left = new FormAttachment(getControl(left));
					} else {
						try {
							data.left = new FormAttachment(Integer.parseInt(left));
						} catch (NumberFormatException e) {

						}
					}

					if (getControl(right) != null) {
						data.right = new FormAttachment(getControl(right));
					} else {
						try {
							data.right = new FormAttachment(Integer.parseInt(right));
						} catch (NumberFormatException e) {

						}
					}

					Element father = (Element) element.getParentNode();
					Control c = getControl(father.getAttribute("name"));

					c.setLayoutData(data);
					return data;
				}
				return null;
			}
		};
		FooFactory.factories.put("FormData", factory);
	}

	private static Control getControl(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceControl) {
			return ((FooInterfaceControl) o).getControl();
		}
		return null;
	}
	
}
