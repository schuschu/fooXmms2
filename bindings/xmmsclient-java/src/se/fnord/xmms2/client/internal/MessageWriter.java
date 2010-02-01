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
 *
 */
package se.fnord.xmms2.client.internal;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import se.fnord.xmms2.client.types.TypeId;

public class MessageWriter {

	private final ConcurrentLinkedQueue<ByteBuffer[]> messages = new ConcurrentLinkedQueue<ByteBuffer[]>();
	private final GatheringByteChannel channel;
	private ByteBuffer[] current = new ByteBuffer[] { ByteBuffer.allocate(0), ByteBuffer.allocate(0) };

	public MessageWriter(GatheringByteChannel channel) {
		this.channel = channel;
	}

	public void add(Message message) {
		ByteBuffer[] packet;
		
		if (message instanceof SendMessage)
			packet = createPacket(message);
		else
			packet = createTypedPacket(message);

		messages.add(packet);
	}

	private ByteBuffer createHeader(Message message, int payload) {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16);
		TypeId.INT32.write(buffer, message.getObject().ordinal());
		TypeId.INT32.write(buffer, message.getCommand().ordinal());
		TypeId.INT32.write(buffer, message.getCookie());
		TypeId.INT32.write(buffer, payload);
		buffer.flip();
		return buffer;
	}

	private ByteBuffer createPayload(Object... params) {
		int capacity = 1024;
		while (true) {
			try {
				final ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);
				for (final Object param : params) {
					TypeId.writeValue(buffer, param);
				}
				buffer.flip();
				return buffer;
			}
			catch (BufferOverflowException e) {
				capacity *= 2;
			}
		}
	}
	
	private ByteBuffer createTypedPayload(Object... params) {
		int capacity = 1024;
		while (true) {
			try {
				final ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);
				for (final Object param : params) {
					TypeId.fromType(param.getClass()).writeType(buffer).write(buffer, param);
				}
				buffer.flip();
				return buffer;
			}
			catch (BufferOverflowException e) {
				capacity *= 2;
			}
		}
	}
	
	private ByteBuffer[] createPacket(Message message) {
		final ByteBuffer payload = createPayload(message.getData());

		return new ByteBuffer[] { createHeader(message, payload.remaining()), payload };
	}

	private ByteBuffer[] createTypedPacket(Message message) {
		final ByteBuffer payload = createTypedPayload(message.getData());
		
		return new ByteBuffer[] { createHeader(message, payload.remaining()), payload };
	}

	public boolean write()
			throws IOException {

		try {
			if (current[1].remaining() != 0) {
				channel.write(current);
			}

			while (current[1].remaining() == 0) {
				current = messages.remove();
				channel.write(current);
			}

			/* As long as there are bytes left to write we should return false */
			if (current[1].remaining() > 0)
				return false;
		}
		catch (final NoSuchElementException e) {
			/* This is quite all right */
		}
		return true;
	}
}
