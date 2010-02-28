package org.dyndns.schuschu.xmms2client.action;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.action.base.FooKey;
import org.dyndns.schuschu.xmms2client.action.base.FooSource;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.w3c.dom.Element;

public class FooSystem {

	public static void registerFactory() {
		// ACTION
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public FooAction create(Element element) {
				
				// the name of the action within the backend , no default possible
				String name = element.getAttribute("name");

				// Source of the event that triggers the event, default is KEYBOARD
				String sourcestring = element.hasAttribute("source") ? element
						.getAttribute("source") : "KEYBOARD";
				FooSource source = FooSource.valueOf(sourcestring);

				// TODO: mousecode
				// Code (keycode, mousecode) that triggers the event, default is NONE
				String codestring = element.hasAttribute("code") ? element
						.getAttribute("code") : "NONE";

				int code = 0;
				switch (source) {
				case MOUSE:
					code = Integer.parseInt(codestring);
					break;
				case KEYBOARD:
					code = FooKey.valueOf(codestring).getCode();
					break;
				}

				// get the parent nodes name for view (since actions are always direct
				// below (hirachical) their view element)
				Element father = (Element) element.getParentNode();
				String viewstring = father.getAttribute("name");
				FooInterfaceAction view = getView(viewstring);
				
				FooAction action=null;

				switch (ActionType.valueOf(name)) {
				case exit:
					action = ActionExit(code); break;

				}
				
				view.addAction(source, action);
				return action;
			}
			
			private FooInterfaceAction getView(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceAction) {
					return (FooInterfaceAction) o;
				}
				return null;
			}
			

		};
		FooFactory.factories.put("System", factory);
	}

	private enum ActionType {
		exit;
	}

	public static FooAction ActionExit(int code) {
		return new ActionExit(code);
	}

	private static class ActionExit extends FooAction {

		public ActionExit(int code) {
			super("exit", code);
		}

		@Override
		public void execute() {
			System.exit(0);
		}

	}

}
