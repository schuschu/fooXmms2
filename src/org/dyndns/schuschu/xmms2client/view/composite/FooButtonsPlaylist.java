package org.dyndns.schuschu.xmms2client.view.composite;

import org.dyndns.schuschu.xmms2client.action.FooPlaylist;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.view.element.FooButton;
import org.dyndns.schuschu.xmms2client.view.element.FooShell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Element;

public class FooButtonsPlaylist implements FooInterfaceControl{

	private Composite composite;

	public FooButtonsPlaylist(Composite parent, int style, FooShell shell) {
		this.setComposite(new Composite(parent, style));

		composite.setLayout(new FillLayout());

		FooButton sortButton = new FooButton(getComposite(), SWT.NONE);
		FooButton shuffleButton = new FooButton(getComposite(), SWT.NONE);
		FooButton newButton = new FooButton(getComposite(), SWT.NONE);
		FooButton deleteButton = new FooButton(getComposite(), SWT.NONE);
		FooButton saveButton = new FooButton(getComposite(), SWT.NONE);

		sortButton.setText("⤑");
		sortButton.setToolTipText("sort playlist");
		shuffleButton.setText("⥂");
		shuffleButton.setToolTipText("shuffle playlist");
		newButton.setText("+");
		newButton.setToolTipText("create new playlist");
		deleteButton.setText("-");
		deleteButton.setToolTipText("delete playlist");
		saveButton.setText("⎇");
		saveButton.setToolTipText("save current playlist");

		sortButton.addAction(FooPlaylist.ActionSort(0,shell));
		shuffleButton.addAction(FooPlaylist.ActionShuffle(0,shell));
		newButton.addAction(FooPlaylist.ActionNew(0,shell));
		deleteButton.addAction(FooPlaylist.ActionDelete(0,shell));
		saveButton.addAction(FooPlaylist.ActionSava(0,shell));
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
						getComposite(parent), SWT.NONE,getShell(element));
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
			
			private FooShell getShell(Element element) {
				Element root = element;
				do {
					root = (Element) root.getParentNode();
				} while (!root.getNodeName().equals("shell"));

				Object o = FooFactory.getView(root.getAttribute("name"));

				if (o instanceof FooShell) {
					return (FooShell) o;
				}
				return null;

			}
		}; 
		
		FooFactory.factories.put("FooButtonsPlaylist", factory);
	}

}
