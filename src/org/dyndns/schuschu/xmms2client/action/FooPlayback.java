package org.dyndns.schuschu.xmms2client.action;

import org.dyndns.schuschu.xmms2client.action.base.FooAction;
import org.dyndns.schuschu.xmms2client.action.base.FooKey;
import org.dyndns.schuschu.xmms2client.action.base.FooSource;
import org.dyndns.schuschu.xmms2client.factories.FooFactory;
import org.dyndns.schuschu.xmms2client.factories.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceAction;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Playback;

public class FooPlayback{

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
				case play:
					action = ActionPlay(code);break;
				case pause:
					action =  ActionPause(code);break;
				case stop:
					action =  ActionStop(code);break;
				case next:
					action =  ActionNext(code);break;
				case prev:
					action =  ActionPrev(code);break;
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

		FooFactory.factories.put("Playback", factory);
	}

	private enum ActionType {
		play, pause, stop, next, prev;
	}

	public static FooAction ActionPlay(int code) {
		return new ActionPlayback(code, "play", PlaybackType.PLAY);
	}

	public static FooAction ActionPause(int code) {
		return new ActionPlayback(code, "pause", PlaybackType.PAUSE);
	}

	public static FooAction ActionStop(int code) {
		return new ActionPlayback(code, "stop", PlaybackType.STOP);
	}

	public static FooAction ActionNext(int code) {
		return new ActionPlayback(code, "next", PlaybackType.NEXT);
	}

	public static FooAction ActionPrev(int code) {
		return new ActionPlayback(code, "prev", PlaybackType.PREV);
	}

	private static class ActionPlayback extends FooAction {

		private final PlaybackType type;

		public ActionPlayback(int code, String name, PlaybackType type) {
			super(name, code);
			this.type = type;
		}

		@Override
		public void execute() {
			switch (type) {
			case NEXT:
				Playback.next().execute(FooLoader.CLIENT);
				break;
			case PAUSE:
				Playback.togglePlay().execute(FooLoader.CLIENT);
				break;
			case PLAY:
				Playback.play().execute(FooLoader.CLIENT);
				break;
			case PREV:
				Playback.prev().execute(FooLoader.CLIENT);
				break;
			case STOP:
				Playback.stop().execute(FooLoader.CLIENT);
				break;
			}

		}

	}

}

enum PlaybackType {

	PAUSE, PLAY, STOP, NEXT, PREV;

}
