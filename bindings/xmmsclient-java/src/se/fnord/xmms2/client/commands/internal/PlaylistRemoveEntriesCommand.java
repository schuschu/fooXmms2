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
package se.fnord.xmms2.client.commands.internal;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.MessageListener;
import se.fnord.xmms2.client.internal.IpcCommand;
import se.fnord.xmms2.client.internal.IpcObject;
import se.fnord.xmms2.client.internal.SendMessage;

public class PlaylistRemoveEntriesCommand  extends AbstractCommand  {

	private final String playlist;
	private final int[] indices;
	private int reply_count = 0;
	private final AtomicBoolean activated = new AtomicBoolean(false);
	private Error error = null;

	protected final MessageListener message_handler = new MessageListener() {
		public void handleReply(Object[] reply) {
			if (reply.length != 1)
				throw new IllegalArgumentException();

			/* TODO: Handle errors more intelligently */
			if (reply[0] instanceof Error)
				error = (Error) reply[0];

			reply_count++;
			if (reply_count == indices.length) {
				if (error == null) {
					addReply(reply[0]);
				}
				else {
					addReply(error);
				}
			}
		}
	};

	public PlaylistRemoveEntriesCommand(String playlist, int[] indices) {
		this.playlist = playlist;
		this.indices = indices.clone();
	}

	public void execute(Client client) {
		if (!activated.compareAndSet(false, true)) throw new IllegalStateException();

		Arrays.sort(indices);
		for (int i = indices.length - 1; i >= 0; i--) {
			client.enqueue(message_handler, new SendMessage(IpcObject.PLAYLIST, IpcCommand.REMOVE_ENTRY, client.newCookie(), playlist, indices[i]));
		}
	}


}
