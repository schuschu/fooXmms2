package org.dyndns.schuschu.xmms2client.view.composite;

import org.dyndns.schuschu.xmms2client.action.FooPlaylist;
import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.view.element.FooButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Element;

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
	
	public static void registerFactory(){
		//VIEW
		FooFactorySub factory = new FooFactorySub() {
			
			@Override
			public Object create(Element element) {	

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for parent (hirachical xml)
				Element father = (Element) element.getParentNode();
				String parent = father.getAttribute("name");
				
				debug("creating FooButtonsPlaylist " + name + " with parent "
						+ parent);
				FooButtonsPlaylist listButtons = new FooButtonsPlaylist(
						getComposite(parent), SWT.NONE);
				FooFactory.putView(name, listButtons);
				return listButtons;
			}
			private Composite getComposite(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceComposite) {
					return ((FooInterfaceComposite) o).getComposite();
				}
				if (o instanceof Composite) {
					return (Composite) o;
				}

				return null;
			}
		}; 
		
		FooFactory.factories.put("FooButtonsPlaylist", factory);
	}

}
