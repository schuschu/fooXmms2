/* Copyright (c) 2009 Henrik Gustafsson <henrik.gustafsson@fnord.se>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package se.fnord.xmms2.client.commands;

import se.fnord.xmms2.client.commands.internal.BasicCommand;
import se.fnord.xmms2.client.commands.internal.PlaybackStatusBroadcastCommand;
import se.fnord.xmms2.client.commands.internal.PlaybackStatusCommand;
import se.fnord.xmms2.client.commands.internal.RelJumpCommand;
import se.fnord.xmms2.client.commands.internal.TogglePlayCommand;
import se.fnord.xmms2.client.internal.IpcCommand;
import se.fnord.xmms2.client.internal.IpcObject;
import se.fnord.xmms2.client.internal.IpcSignal;

public class Playback {

	public static Command currentId() {
		return new BasicCommand(IpcObject.OUTPUT, IpcCommand.CURRENTID);
	}

	public static SignalCommand currentIdBroadcast() {
		return Signal.broadcast(IpcSignal.OUTPUT_CURRENTID);
	}

	public static Command getVolume() {
		return new BasicCommand(IpcObject.OUTPUT, IpcCommand.VOLUME_GET);
	}

	public static Command next() {
		return new RelJumpCommand(1);
	}

	public static Command pause() {
		return new BasicCommand(IpcObject.OUTPUT, IpcCommand.PAUSE);
	}

	public static Command play() {
		return new BasicCommand(IpcObject.OUTPUT, IpcCommand.START);
	}

	public static Command playtime() {
		return new BasicCommand(IpcObject.OUTPUT, IpcCommand.CPLAYTIME);
	}

	public static SignalCommand playtimeSignal() {
		return Signal.signal(IpcSignal.OUTPUT_PLAYTIME);
	}

	public static Command prev() {
		return new RelJumpCommand(-1);
	}

	public static Command seekMs(int ms) {
		return new BasicCommand(IpcObject.OUTPUT, IpcCommand.SEEKMS, ms);
	}

	public static Command setVolume(String channel, int value) {
		return new BasicCommand(IpcObject.OUTPUT, IpcCommand.VOLUME_SET, channel, value );
	}

	public static Command status() {
		return new PlaybackStatusCommand();
	}

	public static Command statusBroadcast() {
		return new PlaybackStatusBroadcastCommand();
	}

	public static Command stop() {
		return new BasicCommand(IpcObject.OUTPUT, IpcCommand.STOP);
	}

	public static Command tickle() {
		return new BasicCommand(IpcObject.OUTPUT, IpcCommand.DECODER_KILL);
	}

	public static Command togglePlay() {
		return new TogglePlayCommand();
	}

}
