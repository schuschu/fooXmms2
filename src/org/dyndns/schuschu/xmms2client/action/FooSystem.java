package org.dyndns.schuschu.xmms2client.action;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.factories.FooActionFactory;
import org.dyndns.schuschu.xmms2client.factories.FooActionFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.factory.FooInterfaceFactory;

public class FooSystem implements FooInterfaceFactory {

	public static void registerActionFactory() {
		FooActionFactorySub factory = new FooActionFactorySub() {

			@Override
			public FooAction create(String name, int code) {
				switch (ActionType.valueOf(name)) {
				case exit:
					return ActionExit(code);

				}
				return null;
			}

		};

		FooActionFactory.factories.put("System", factory);
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
