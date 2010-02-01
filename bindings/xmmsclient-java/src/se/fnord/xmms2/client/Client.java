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

package se.fnord.xmms2.client;

import se.fnord.xmms2.client.internal.SendMessage;

/**
 * An XMMS2 client. The user of this interface can send messages to the client and generate new message cookie values.
 * @author gsson
 *
 */
public interface Client {

	/**
	 * Register a status listener with the client.
	 * @return status.
	 */
	void addStatusListener(ClientStatusListener l);

	/**
	 * Send message m to the XMMS2 daemon and notify c when a reply is received.
	 * @param c Listener to notify when a reply is received
	 * @param m Message to send
	 */
	void enqueue(MessageListener c, SendMessage m);

	/**
	 * Fetch the status of the client.
	 * @return status.
	 */
	ClientStatus getStatus();

	/**
	 * Generates a new cookie value that is unique during the runtime of this client.
	 * @return new cookie value
	 */
	int newCookie();

	/**
	 * Resets the client. Note that all outstanding commands, broad-
	 * casts and signals will be canceled.
	 */
	void reset();

	/**
	 * Connects to the xmms2 daemon.
	 */
	void start();
	
	/**
	 * Disconnects the client and stops the worker thread.
	 */
	void stop();
	
}