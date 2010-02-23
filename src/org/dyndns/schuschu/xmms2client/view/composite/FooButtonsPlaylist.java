package org.dyndns.schuschu.xmms2client.view.composite;

import org.dyndns.schuschu.xmms2client.action.FooPlaylist;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.view.element.FooButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FooButtonsPlaylist implements FooInterfaceControl{

	private Composite composite;

	public FooButtonsPlaylist(Composite parent, int style) {
		this.setComposite(new Composite(parent, style));

		composite.setLayout(new FillLayout());

		FooButton sortButton = new FooButton(getComposite(), SWT.NONE);
		FooButton shuffleButton = new FooButton(getComposite(), SWT.NONE);
		FooButton newButton = new FooButton(getComposite(), SWT.NONE);
		FooButton deleteButton = new FooButton(getComposite(), SWT.NONE);
		FooButton saveButton = new FooButton(getComposite(), SWT.NONE);

		sortButton.setText("⤑");
		shuffleButton.setText("⥂");
		newButton.setText("+");
		deleteButton.setText("-");
		saveButton.setText("⎇");

		sortButton.addAction(FooPlaylist.ActionSort(0));
		shuffleButton.addAction(FooPlaylist.ActionShuffle(0));
		newButton.addAction(FooPlaylist.ActionNew(0));
		deleteButton.addAction(FooPlaylist.ActionDelete(0));
		saveButton.addAction(FooPlaylist.ActionSava(0));
	}

	public void setComposite(Composite composite) {
		this.composite = composite;
	}

	public Composite getComposite() {
		return composite;
	}

	public void setLayoutData(Object layoutData) {
		composite.setLayoutData(layoutData);
	}

	@Override
	public Control getControl() {
		return composite;
	}

}
