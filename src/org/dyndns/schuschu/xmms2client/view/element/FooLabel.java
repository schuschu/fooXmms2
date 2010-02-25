package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooViewFactory;
import org.dyndns.schuschu.xmms2client.factories.FooViewFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackendText;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.w3c.dom.Element;

public class FooLabel implements FooInterfaceText,FooInterfaceControl {

	private Label label;
	private FooInterfaceBackendText backend;
	
	public FooLabel(Composite parent) {
		this(parent,SWT.NONE);
	}

	public FooLabel(Composite parent, int style) {
		setLabel(new Label(parent, style));
	}

	@Override
	public void setText(String string) {
		if (!getLabel().isDisposed()) {
			getLabel().setText(string);
		}

	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public Label getLabel() {
		return label;
	}

	public void setLayoutData(Object layoutData) {
		label.setLayoutData(layoutData);
	}

	@Override
	public Control getControl() {
		return label;
	}

	@Override
	public void setBackend(FooInterfaceBackendText backend) {
		this.backend = backend;
		
	}

	@Override
	public FooInterfaceBackendText getBackend() {
		return backend;
	}

	public  static void registerFactory(){
		//VIEW
		FooViewFactorySub factory = new FooViewFactorySub() {
			
			@Override
			protected Object create(Element element) {

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for parent (hirachical xml)
				Element father = (Element) element.getParentNode();
				String parent = father.getAttribute("name");
				
				debug("creating FooLabel " + name + " with parent " + parent);
				FooLabel label = new FooLabel(getComposite(parent));
				FooFactory.putView(name, label);
				return label;
			}
			private Composite getComposite(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceComposite) {
					return ((FooInterfaceComposite) o).getComposite();
				}
				//TODO: remove once FooShell exists
				if (o instanceof Composite) {
					return (Composite) o;
				}

				return null;
			}
		};
		FooViewFactory.factories.put("FooLabel", factory);
	}
	
}
