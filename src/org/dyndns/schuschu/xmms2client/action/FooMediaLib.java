package org.dyndns.schuschu.xmms2client.action;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.action.base.FooKey;
import org.dyndns.schuschu.xmms2client.action.base.FooSource;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.view.dialog.FooDirectoryDialog;
import org.dyndns.schuschu.xmms2client.view.element.FooShell;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Medialib;

public class FooMediaLib {

	//TODO: Add File
	
	public static void registerFactory() {
		// ACTION
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public FooAction create(Element element) {

				// the name of the action within the backend , no default
				// possible
				String name = element.getAttribute("name");

				// Source of the event that triggers the event, default is
				// KEYBOARD
				String sourcestring = element.hasAttribute("source") ? element
						.getAttribute("source") : "KEYBOARD";
				FooSource source = FooSource.valueOf(sourcestring);

				// TODO: mousecode
				// Code (keycode, mousecode) that triggers the event, default is
				// NONE
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

				// get the parent nodes name for view (since actions are always
				// direct
				// below (hirachical) their view element)
				Element father = (Element) element.getParentNode();
				String viewstring = father.getAttribute("name");
				FooInterfaceAction view = getView(viewstring);

				FooAction action = null;

				switch (ActionType.valueOf(name)) {
				case add_dir:
					action = ActionAddDir(code,getShell(element));
					break;
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

		FooFactory.factories.put("MediaLib", factory);
	}

	private enum ActionType {
		add_dir, add_file;
	}

	public static FooAction ActionAddDir(int code, FooShell shell) {
		return new ActionMediaLib(code, "add_dir", MedialibType.ADD_DIR, shell);
	}

	private static class ActionMediaLib extends FooAction {

		private final MedialibType type;
		private FooShell shell;

		public ActionMediaLib(int code, String name, MedialibType type,
				FooShell shell) {
			super(name, code);
			this.shell = shell;
			this.type = type;
		}

		@Override
		public void execute() {
			switch (type) {
			case ADD_DIR:
				String path = FooDirectoryDialog.show(shell);
				if(path!=null){
					Command c = Medialib.pathImport("file://" + path);
					c.execute(FooLoader.CLIENT);
				}
			}
		}

	}

}

enum MedialibType {

	ADD_DIR, ADD_FILE;

}
