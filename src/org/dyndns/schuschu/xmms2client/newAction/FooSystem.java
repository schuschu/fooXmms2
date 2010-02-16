package org.dyndns.schuschu.xmms2client.newAction;

public class FooSystem {

	public static FooAction ActionExit(int code){
		return new ActionExit(code);
	}

	private static class ActionExit extends FooAction {

		public ActionExit(int code) {
			super(code);
		}

		@Override
		public void execute() {
			System.exit(0);
		}

	}

}
