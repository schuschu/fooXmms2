package org.dyndns.schuschu.xmms2client.factories;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.loader.FooXML;
import org.dyndns.schuschu.xmms2client.view.composite.FooButtonsPlayback;
import org.dyndns.schuschu.xmms2client.view.composite.FooButtonsPlaylist;
import org.dyndns.schuschu.xmms2client.view.element.FooCombo;
import org.dyndns.schuschu.xmms2client.view.element.FooLabel;
import org.dyndns.schuschu.xmms2client.view.element.FooList;
import org.dyndns.schuschu.xmms2client.view.element.FooSashForm;
import org.dyndns.schuschu.xmms2client.view.element.FooTable;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.w3c.dom.Element;

public class FooViewFactory {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private FooColor debugForeground = FooColor.WHITE;
	private FooColor debugBackground = FooColor.BLUE;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(debugForeground);
				FooDebug.setBackground(debugBackground);
			}
			System.out.println("debug: ViewFactory " + message);
		}
	}

	public FooViewFactory() {
	}

	public Object create(Element element) {
		
		String type = element.getAttribute("type");
		String name = element.getAttribute("name");

		Element father = (Element) element.getParentNode();
		String parent = father.getAttribute("name");
		
		String layoutstring = FooXML.getTagValue("layout", element);

		switch (FooViewType.valueOf(type)) {
		case Shell:
			debug("creating Shell " + name + " with parent " + parent + " ... not");
			return FooWindow.SHELL;
		case Composite:
			debug("creating Composite " + name + " with parent " + parent);
			Composite comp = new Composite(getComposite(parent), SWT.NONE);
			if (layoutstring != null) {
				comp.setLayout(createLayout(layoutstring));
			}
			FooFactory.putView(name, comp);
			return comp;

		case FooList:
			debug("creating FooList " + name + " with parent " + parent);
			FooList list = new FooList(getComposite(parent));
			FooFactory.putView(name, list);
			return list;
		case FooCombo:
			debug("creating FooCombo " + name + " with parent " + parent);
			FooCombo combo = new FooCombo(getComposite(parent));
			FooFactory.putView(name, combo);
			return combo;
		case FooTable:
			debug("creating FooTable " + name + " with parent " + parent);
			FooTable table = new FooTable(getComposite(parent));
			FooFactory.putView(name, table);
			return table;
		case FooLabel:
			debug("creating FooLabel " + name + " with parent " + parent);
			FooLabel label = new FooLabel(getComposite(parent));
			FooFactory.putView(name, label);
			return label;

		case FooButtonsPlaylist:
			debug("creating FooButtonsPlaylist " + name + " with parent "
					+ parent);
			FooButtonsPlaylist listButtons = new FooButtonsPlaylist(
					getComposite(parent), SWT.NONE);
			FooFactory.putView(name, listButtons);
			return listButtons;
		case FooButtonsPlayback:
			debug("creating FooButtonsPlayback " + name + " with parent "
					+ parent);
			FooButtonsPlayback playButtons = new FooButtonsPlayback(
					getComposite(parent), SWT.NONE);
			FooFactory.putView(name, playButtons);
			return playButtons;
		case FooSashForm:
			debug("creating SashForm " + name + " with parent " + parent);
			FooSashForm sash = new FooSashForm(getComposite(parent));
			FooFactory.putView(name, sash);
			return sash;
		}
		return null;

	}

	private Composite getComposite(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof Composite) {
			return (Composite) o;
		}
		if (o instanceof FooInterfaceComposite) {
			return ((FooInterfaceComposite) o).getComposite();
		}

		return null;
	}

	private Control getControl(String s) {
		Object o = FooFactory.getView(s);
		if (o instanceof FooInterfaceControl) {
			return ((FooInterfaceControl) o).getControl();
		}
		return null;
	}

	enum LayoutType {
		FillLayout, FormLayout;
	}

	public Layout createLayout(String layoutstring) {
		try {
			switch (LayoutType.valueOf(layoutstring)) {
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

	public Object createLayoutData(Element item) {
		Element layout = FooXML.getElement(item, "layoutdata");
		
		if (layout != null) {
			// TODO: other layouts
			FormData data = new FormData();
			String top = layout.getAttribute("top");
			String bottom = layout.getAttribute("bottom");
			String left = layout.getAttribute("left");
			String right = layout.getAttribute("right");

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

			debug("layout for " + item.getAttribute("name")
					+ " created!");
			
			Control c = getControl(item.getAttribute("name"));
			
			if (c == null) {
				debug("THIS SHOULD NOT HAPPEN!");
				return null;
			}
			c.setLayoutData(data);
			return data;
		}
		return null;
	}
}
