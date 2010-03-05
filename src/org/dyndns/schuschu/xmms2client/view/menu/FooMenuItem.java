package org.dyndns.schuschu.xmms2client.view.menu;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.action.base.FooSource;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceMenu;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.view.FooStyle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.w3c.dom.Element;

public class FooMenuItem implements FooInterfaceAction, FooInterfaceMenu{

	private MenuItem item;
	private Vector<FooAction> actions;

	public FooMenuItem(FooMenu parent) {
		this(parent, SWT.NONE);
	}

	public FooMenuItem(FooMenu parent, int style) {

		actions = new Vector<FooAction>();

		item = new MenuItem(parent.getMenu(), style);

		item.addListener(SWT.Selection, createListener());
	}

	private Listener createListener() {
		return new Listener() {
			// TODO: sudo make it good
			@Override
			public void handleEvent(Event arg0) {
				for (FooAction a : actions) {
					a.execute();
				}
			}
		};
	}

	public void setText(String string) {
		item.setText(string);
	}

	@Override
	public void addAction(FooSource source, FooAction action) {
		actions.add(action);
		
	}

	@Override
	public void removeAction(FooSource source, FooAction action) {
		actions.remove(action);
		
	}
	
	public static void registerFactory(){
		FooFactorySub factory = new FooFactorySub() {
			
			@Override
			public Object create(Element element) {
				//VIEW
			
				// get the parent nodes name for view (since menu are always direct
				// below (hirachical) their view element)
				Element father =(Element) element.getParentNode();
				String menu = father.getAttribute("name");

				//text of the menuitem, no default
				String text = element.getAttribute("text");
				
				// name equals variable name, no default
				String name = element.getAttribute("name");
				
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
 
				FooMenuItem item = new FooMenuItem(getMenu(menu),style);
				item.setText(text);
				
				FooFactory.putView(name, item);

				return menu;
			}
		
			private FooMenu getMenu(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooMenu) {
					return (FooMenu) o;
				}
				return null;
			}
		};
		FooFactory.factories.put("FooMenuItem", factory);
	}

	@Override
	public void setMenu(FooMenu menu) {
		item.setMenu(menu.getMenu());		
	}

}
