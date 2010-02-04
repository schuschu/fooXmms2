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

/**
 * Worker thread implementation for handling command replies in an
 * timely and orderly fashion without delaying the communications
 * thread unnecessarily. Example:
 * <pre>
 *    package myapp;
 *    import se.fnord.xmms2.client.Client;
 *    import se.fnord.xmms2.client.ClientFactory;
 *    import se.fnord.xmms2.client.CommandErrorException;
 *    import se.fnord.xmms2.client.commands.Command;
 *    import se.fnord.xmms2.client.commands.Playlist;
 *    import se.fnord.xmms2.client.commands.utils.CommandListener;
 *    import se.fnord.xmms2.client.commands.utils.CommandWorker;
 *
 *    public class MyMain {
 *
 *        public static void main(String[] args) throws InterruptedException {
 *            Client client = ClientFactory.create("MyClient", "tcp://127.0.0.1:9667");
 *            client.start();
 *            CommandWorker worker = new CommandWorker();
 *            worker.start();
 *            Command command = Playlist.currentActive(client);
 *            worker.execute(command, new CommandListener() {
 *                public void commandCompleted(Command c, Object param) {
 *                    try {
 *                        System.out.println((String)c.getReply());
 *                    } catch (CommandErrorException e) {
 *                        System.out.println(e.getMessage());
 *                    }
 *                }
 *            }, null);
 *            Thread.sleep(1000);
 *        }
 *    }
 * </pre>
 */
package se.fnord.xmms2.client.commands.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.SignalCommand;

public class CommandWorker {

	private class CommandWorkerRunnable implements Runnable {
		public void run() {
			try {
				while (true) {
					Command c;
					c = queue.take();
					Pair p;
					if (c instanceof SignalCommand) {
						p = outstanding.get(c);
					}
					else {
						p = outstanding.remove(c);
					}

					if (p.listener != null)
						p.listener.commandCompleted(c, p.value);
				}
			} catch (final InterruptedException e) {
				for (final Command c : outstanding.keySet())
					queue.deregister(c);

				queue.clear();
				outstanding.clear();

				System.err.println("Got interrupted. Dying now.");
			}
		}
	}

	private static final class Pair {
		public final CommandListener listener;
		public final Object value;

		public Pair(CommandListener listener, Object value) {
			this.listener = listener;
			this.value = value;
		}
	}

	private final ConcurrentMap<Command, Pair> outstanding = new ConcurrentHashMap<Command, Pair>();
	private final CommandQueue queue = new CommandQueue();
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final Runnable runnable = new CommandWorkerRunnable();
	private final Client client;
	
	/**
	 * The constructor.
	 */
	public CommandWorker(Client client) {
		this.client = client;
	}

	/**
	 * Executes a command, notifying the command listener when a reply
	 * to the command has been received. Note that the listener will
	 * execute in the command worker thread.
	 * @param command command to execute.
	 * @param listener command listener to notify when the command
	 * 	receives a reply.
	 * @param param parameter to supply to the command listener when invoked.
	 */
	public void execute(Command command, CommandListener listener, Object param) {
		queue.register(command);
		outstanding.put(command, new Pair(listener, param));
		command.execute(client);
	}

	/**
	 * Creates and starts the processing thread.
	 * @throws IllegalStateException if the thread was already started.
	 */
	public void start() {
		if (running.compareAndSet(false, true)) {
			new Thread(runnable, "Command Worker").start();
		} else
			throw new IllegalStateException();
	}

}
