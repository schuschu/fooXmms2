package org.dyndns.schuschu.xmms2client.view.composite;

import org.dyndns.schuschu.xmms2client.action.FooPlayback;
import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooViewFactory;
import org.dyndns.schuschu.xmms2client.factories.FooViewFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceControl;
import org.dyndns.schuschu.xmms2client.view.element.FooButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Element;

public class FooButtonsPlayback implements FooInterfaceControl{

	private Composite composite;

	public FooButtonsPlayback(Composite parent, int style) {
		this.setComposite(new Composite(parent, style));

		composite.setLayout(new FillLayout());

		FooButton prevButton = new FooButton(getComposite(), SWT.NONE);
		FooButton stopButton = new FooButton(getComposite(), SWT.NONE);
		FooButton pauseButton = new FooButton(getComposite(), SWT.NONE);
		FooButton playButton = new FooButton(getComposite(), SWT.NONE);
		FooButton nextButton = new FooButton(getComposite(), SWT.NONE);

		// TODO: icons
		prevButton.setText("◂◂");
		stopButton.setText("◾");
		pauseButton.setText("▮▮");
		playButton.setText("►");
		nextButton.setText("▸▸");

		prevButton.addAction(FooPlayback.ActionPrev(0));
		stopButton.addAction(FooPlayback.ActionStop(0));
		pauseButton.addAction(FooPlayback.ActionPause(0));
		playButton.addAction(FooPlayback.ActionPlay(0));
		nextButton.addAction(FooPlayback.ActionNext(0));

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
		FooViewFactorySub factory = new FooViewFactorySub() {
			
			@Override
			protected Object create(Element element) {

				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for parent (hirachical xml)
				Element father = (Element) element.getParentNode();
				String parent = father.getAttribute("name");
				
				debug("creating FooButtonsPlayback " + name + " with parent "
						+ parent);
				FooButtonsPlayback playButtons = new FooButtonsPlayback(
						getComposite(parent), SWT.NONE);
				FooFactory.putView(name, playButtons);
				return playButtons;
			}
		};
		
		FooViewFactory.factories.put("FooButtonsPlayback", factory);
	}

}
