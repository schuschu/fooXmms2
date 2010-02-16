package org.dyndns.schuschu.xmms2client.newAction;

import org.dyndns.schuschu.xmms2client.loader.FooLoader;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Playback;

public class FooPlayback {

	public static FooAction ActionPlay(int code) {
		return new ActionPlayback(code, PlaybackType.PLAY, FooLoader.client);
	}

	public static FooAction ActionPause(int code) {
		return new ActionPlayback(code, PlaybackType.PAUSE, FooLoader.client);
	}

	public static FooAction ActionStop(int code) {
		return new ActionPlayback(code, PlaybackType.STOP, FooLoader.client);
	}

	public static FooAction ActionNext(int code) {
		return new ActionPlayback(code, PlaybackType.NEXT, FooLoader.client);
	}

	public static FooAction ActionPrev(int code) {
		return new ActionPlayback(code, PlaybackType.PREV, FooLoader.client);
	}

	private static class ActionPlayback extends FooAction {

		private final PlaybackType type;
		private final Client client;

		public ActionPlayback(int code, PlaybackType type, Client client) {
			super(code);
			this.type = type;
			this.client = client;
		}

		@Override
		public void execute() {
			switch (type) {
			case NEXT:
				Playback.next().execute(client);
				break;
			case PAUSE:
				Playback.togglePlay().execute(client);
				break;
			case PLAY:
				Playback.play().execute(client);
				break;
			case PREV:
				Playback.prev().execute(client);
				break;
			case STOP:
				Playback.stop().execute(client);
				break;
			}

		}

	}

}

enum PlaybackType {

	PAUSE, PLAY, STOP, NEXT, PREV;

}
