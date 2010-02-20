package org.dyndns.schuschu.xmms2client.view.element;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class FooLabel implements FooInterfaceText {

	private Label label;

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

}
