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

package se.fnord.xmms2.client.commands.utils;

import java.util.concurrent.LinkedBlockingQueue;

import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.CommandResponseListener;

public final class CommandQueue {
	private final LinkedBlockingQueue<Command> replies = new LinkedBlockingQueue<Command>();
	private final CommandResponseListener handler = new CommandResponseListener() {
		public void responseReceived(Command c) {
			replies.add(c);
		}
	};

	/**
	 * Removes all waiting commands from the queue.
	 */
	public void clear() {
		replies.clear();
	}

	/**
	 * De-registers a previously registered command.
	 * The current implementation just sets the handler of the supplied command to null.
	 * @param c The command to deregister.
	 */
	public void deregister(Command c) {
		c.setHandler(null);
	}


	/**
	 * Retrieves and removes the first command in the queue that has
	 * received a reply, or null if there are no commands waiting in
	 * the queue.
	 * @return The command on which a reply arrived, or null if no
	 *   replies have been received.
	 * @throws InterruptedException if another thread has interrupted the current thread.
	 */
	public Command poll() {
		return replies.poll();
	}

	/**
	 * Registers a command that this queue should handle replies for.
	 * @param c The command.
	 */
	public void register(Command c) {
		c.setHandler(handler);
	}

	/**
	 * Retrieves and removes the first command in the queue that has
	 * received a reply, waiting if necessary until a command receives
	 * a reply.
	 * @return The command on which a reply arrived.
	 * @throws InterruptedException if another thread has interrupted the current thread.
	 */
	public Command take() throws InterruptedException {
		return replies.take();
	}
}
