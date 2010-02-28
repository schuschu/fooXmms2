package org.dyndns.schuschu.xmms2client.view.element;

import java.io.IOException;
import java.io.InputStream;

import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceComposite;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;

public class FooShell implements FooInterfaceComposite{
	private Shell shell;
	
	public FooShell(){
		shell = new Shell();
	}

	@Override
	public Composite getComposite() {
		return shell;
	}
	
	public void setLayout(Layout layout){
		shell.setLayout(layout);
	}
	
	public void setMaximized(boolean maximized){
		shell.setMaximized(maximized);
	}
	
	public void setVisible(boolean visible){
		shell.setVisible(visible);
	}
	
	public boolean getVisible(){
		return shell.getVisible();
	}
	
	public Point getLocation(){
		return shell.getLocation();
	}
	
	public void setLocation(Point location){
		shell.setLocation(location);
	}
	
	public void setText(String string){
		shell.setText(string);
	}
	
	public void setSize(Point size){
		shell.setSize(size);
	}
	
	public void setImage(Image image){
		shell.setImage(image);
	}
	
	public boolean isDisposed(){
		return shell.isDisposed();
	}
	
	public Shell getShell(){
		return shell;
	}
	
	public static void registerFactory(){
		//VIEW
		FooFactorySub factory = new FooFactorySub() {
			
			@Override
			public Object create(Element element) {
				
				// name equals variable name, no default
				String name = element.getAttribute("name");

				// title of the window, default fooXmms2
				String text = element.hasAttribute("text") ? element.getAttribute("text") : "fooXmms2";

				// TODO: defaults
				// dimensions of the window, has defualts
				String widthstring = element.hasAttribute("width") ? element.getAttribute("width") : "1000";
				String heigthstring = element.hasAttribute("heigth")  ? element.getAttribute("heigth") : "600";
				int width = Integer.parseInt(widthstring);
				int heigth = Integer.parseInt(heigthstring);
				
				// gets the layout of the composite/shell , default is FillLayout
				String layoutstring = element.hasAttribute("layout") ? element
						.getAttribute("layout") : "FillLayout";
				
				//TODO: shell from xml 
				FooShell shell = new FooShell();

				FooFactory.putView(name, shell);

				shell.setText(text);
				shell.setSize(new Point(width, heigth));

				shell.setLayout(createLayout(layoutstring));

				Image image = null;

				//TODO: image from xml
				InputStream stream = this.getClass().getResourceAsStream(
						"/pixmaps/xmms2-128.png");
				if (stream != null) {
					try {
						image = new Image(Display.getDefault(), stream);
					} catch (IllegalArgumentException e) {
					} finally {
						try {
							stream.close();
						} catch (IOException e) {
						}
					}
				} else {
					// TODO: find better way to do this
					image = new Image(Display.getDefault(), "pixmaps/xmms2-128.png");
				}

				shell.setImage(image);
				
				//TODO: replace static with search in map! (everywhere)
				FooWindow.SHELL = shell;
				return shell;
			}
			
			private Layout createLayout(String layoutstring) {
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
		};
		
		FooFactory.factories.put("FooShell", factory);
	}

}
