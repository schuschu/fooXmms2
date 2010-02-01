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

import java.util.concurrent.LinkedBlockingQueue;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.Error;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.CommandResponseListener;

public abstract class AbstractCommand implements Command {
	private final LinkedBlockingQueue<Object> replies = new LinkedBlockingQueue<Object>();
	protected CommandResponseListener handler = null;

	protected void addReply(Object o) {
		replies.add(o);

		if (handler != null)
			handler.responseReceived(this);
	}

	public  <A> A executeSync(Client client) throws InterruptedException {
		execute(client);
		return waitReply();
	}

	@SuppressWarnings("unchecked")
	public synchronized <A> A getReply() {
		final Object o = replies.poll();
		if (o instanceof Error)
			throw new CommandErrorException((Error) o);

		return (A) o;
	}

	public void setHandler(CommandResponseListener handler) {
		this.handler = handler;
	}

	@SuppressWarnings("unchecked")
	public synchronized <A> A  waitReply() throws InterruptedException {
		final Object o = replies.take();
		if (o instanceof Error)
			throw new CommandErrorException((Error) o);

		return (A) o;
	}

}
