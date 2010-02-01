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

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.MessageListener;
import se.fnord.xmms2.client.internal.IpcCommand;
import se.fnord.xmms2.client.internal.IpcObject;
import se.fnord.xmms2.client.internal.SendMessage;

public final class TogglePlayCommand extends AbstractCommand {
	private Client client;
	
	private final MessageListener status_handler = new MessageListener() {
		public void handleReply(Object[] reply) {
			if (reply.length != 1)
				throw new IllegalArgumentException();

			if (reply[0] instanceof Integer) {
				int status = (Integer) reply[0];
				IpcCommand c = IpcCommand.START;
				/* TODO: sudo make me an enum */
				if (status == 1) {
					c = IpcCommand.PAUSE;
				}
				client.enqueue(command_handler, new SendMessage(IpcObject.OUTPUT, c, client.newCookie()));
				return;
			}

			addReply(reply[0]);
		}
	};

	private final MessageListener command_handler = new MessageListener() {
		public void handleReply(Object[] reply) {
			if (reply.length != 1)
				throw new IllegalArgumentException();

			addReply(reply[0]);
		}
	};

	public TogglePlayCommand() {
		super();
	}

	public void execute(Client client) {
		this.client = client;
		client.enqueue(status_handler, new SendMessage(IpcObject.OUTPUT, IpcCommand.OUTPUT_STATUS, client.newCookie()));
	}
}
