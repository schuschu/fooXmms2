package org.dyndns.schuschu.xmms2client.Action.global;

import org.dyndns.schuschu.xmms2client.Action.FooAction;
import org.dyndns.schuschu.xmms2client.Action.factory.FooActionFactory;
import org.dyndns.schuschu.xmms2client.Action.factory.FooActionFactorySub;

public class FooSystem {

	public static void registerActionFactory() {
		FooActionFactorySub factory = new FooActionFactorySub() {

			@Override
			public FooAction create(String name, int code) {
				try {
					switch (ActionType.valueOf(name)) {
					case exit:
						return ActionExit(code);

					}

				} catch (NullPointerException e) {
					// TODO: this is bad you know...
				} catch (IllegalArgumentException e) {
					// Thats not an enum!
				}
				return null;
			}

		};

		FooActionFactory.factories.put("Playback", factory);
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
