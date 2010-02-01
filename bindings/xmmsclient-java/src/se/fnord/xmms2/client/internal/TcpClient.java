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

package se.fnord.xmms2.client.internal;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientStatus;
import se.fnord.xmms2.client.ClientStatusListener;
import se.fnord.xmms2.client.MessageListener;
import se.fnord.xmms2.client.commands.Main;

public class TcpClient implements Client {
	private class ClientWorker implements Runnable {
		private final Thread thread = new Thread(this, "Client Worker");
		private final AtomicBoolean stop = new AtomicBoolean(false);
		private final MessageReader reader;
		private final MessageWriter writer;

		private final Selector selector;
		private final SelectionKey selection_key;

		private final SocketChannel channel;

		private final ConcurrentMap<Integer, MessageListener> outstanding = new ConcurrentHashMap<Integer, MessageListener>();
		private final BlockingQueue<Pair> command_queue = new LinkedBlockingQueue<Pair>();

		public ClientWorker(SocketChannel channel) throws IOException {
			this.channel = channel;
			selector = Selector.open();
			selection_key = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, null);
			reader = new MessageReader(channel);
			writer = new MessageWriter(channel);
		}

		private void doRead() throws IOException {
			reader.read();

			Message m = reader.poll();
			while (m != null) {
				final int cookie = m.getCookie();

				if (outstanding.containsKey(cookie)) {
					MessageListener c;
					if (m.getCommand() != IpcCommand.BROADCAST)
						c = outstanding.remove(cookie);
					else
						c = outstanding.get(cookie);

					c.handleReply(m.getData());
				} else {
					System.err.println("Received: " + m.toString()
							+ " (for no apparent reason)");
				}
				m = reader.poll();
			}
		}

		private void doWrite() throws IOException {
			Pair a = command_queue.poll();
			while (a != null) {
				outstanding.put(a.message.getCookie(), a.handler);
				writer.add(a.message);
				a = command_queue.poll();
			}

			/* If writer.write() returns true we have nothing more
			 * to write, and are thus no longer interested in the
			 * writability of the channel.
			 */
			if (writer.write())
				selection_key.interestOps(SelectionKey.OP_READ);
			else
				selection_key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}

		public void enqueue(MessageListener c, SendMessage m) {
			command_queue.add(new Pair(c, m));
			selector.wakeup();
		}

		public void run() {
			try {
				while (!channel.finishConnect())
					;

				setStatus(ClientStatus.RUNNING);
				while (true) {
					selector.select();

					doWrite();
					doRead();
				}
			}
			catch (final IOException e) {
				/* An IO exception is quite alright */
			}
			finally {
				setStatus(ClientStatus.DEAD);
			}
		}

		public void start() {
			thread.setDaemon(true);
			thread.start();
		}

		public void stop()  {
			stop.set(true);
			outstanding.clear();
			command_queue.clear();
			thread.interrupt();
			while (status == ClientStatus.RUNNING) {
				try {
					thread.join();
				} catch (final InterruptedException e) {}
			}
		}
	}

	private static final class Pair {
		final MessageListener handler;
		final SendMessage message;

		public Pair(MessageListener command, SendMessage message) {
			this.handler = command;
			this.message = message;
		}
	}
	static private final AtomicInteger next_cookie = new AtomicInteger(0);
	private final String name;
	private final SocketAddress address;
	private ClientWorker worker;

	private SocketChannel channel;

	private volatile ClientStatus status;

	private final Set<ClientStatusListener> listeners = new CopyOnWriteArraySet<ClientStatusListener>();


	public TcpClient(String name, SocketAddress address) {
		this.name = name;
		this.address = address;

		try {
			this.channel = SocketChannel.open();
			channel.configureBlocking(false);

			worker = new ClientWorker(channel);
			status = ClientStatus.ARMED;
		} catch (final IOException e) {
			status = ClientStatus.DEAD;
		}
	}

	public void addStatusListener(ClientStatusListener l) {
		listeners.add(l);
	}


	public void enqueue(MessageListener c, SendMessage m) {
		worker.enqueue(c, m);
	}

	public synchronized ClientStatus getStatus() {
		return this.status;
	}


	public int newCookie() {
		return next_cookie.getAndIncrement();
	}

	public void reset() {
		start();
	}


	private synchronized void setStatus(ClientStatus status) {
		if (this.status != status) {
			this.status = status;
			for (final ClientStatusListener l : listeners) {
				l.statusChanged(this, this.status);
			}
		}
	}

	public void stop() {
		assert(status == ClientStatus.RUNNING && worker != null);
		worker.stop();
		
	}
	public void start() {
		try {
			switch (status) {
			case ARMED:
				channel.connect(address);

				worker.start();

				Main.hello(name, 13).execute(this);

				break;
			case DEAD:
				if (worker != null)
					worker.stop();

				if (this.channel != null)
					this.channel.close();

				this.channel = SocketChannel.open();
				channel.configureBlocking(false);
				channel.connect(address);

				worker = new ClientWorker(channel);

				worker.start();

				Main.hello(name, 13).execute(this);

				break;
			case RUNNING:
				throw new IllegalStateException();
			}
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
