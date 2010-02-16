package org.dyndns.schuschu.xmms2client.Action;

import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.commands.Playback;

public class FooPlayback {

	public static FooAction ActionPlay(int code) {
		return new ActionPlayback(code, PlaybackType.PLAY);
	}

	public static FooAction ActionPause(int code) {
		return new ActionPlayback(code, PlaybackType.PAUSE);
	}

	public static FooAction ActionStop(int code) {
		return new ActionPlayback(code, PlaybackType.STOP);
	}

	public static FooAction ActionNext(int code) {
		return new ActionPlayback(code, PlaybackType.NEXT);
	}

	public static FooAction ActionPrev(int code) {
		return new ActionPlayback(code, PlaybackType.PREV);
	}

	private static class ActionPlayback extends FooAction {

		private final PlaybackType type;

		public ActionPlayback(int code, PlaybackType type) {
			super(code);
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
