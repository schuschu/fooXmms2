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
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import se.fnord.xmms2.client.types.TypeId;

public class MessageReader {
	private enum ReadState {
		HEADER, PAYLOAD, DONE
	}
	private IpcObject object;
	private IpcCommand cmd;
	private int cookie;
	private final ArrayList<Object> params = new ArrayList<Object>();
	
	private final ByteBuffer header = ByteBuffer
			.allocate(AbstractMessage.HEADER_LENGTH);

	private ByteBuffer data = ByteBuffer.allocate(1024);
	private final ReadableByteChannel channel;

	private final ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<Message>();

	ReadState state = ReadState.HEADER;

	public MessageReader(ReadableByteChannel channel) {
		this.channel = channel;
	}

	public Message poll() {
		return messages.poll();
	}

	private Message readMessage() throws IOException {
		if (state == ReadState.HEADER)
			state = readHeader();

		if (state == ReadState.PAYLOAD)
			state = readPayload();

		if (state == ReadState.DONE) {
			Message m = MessageFactory.incoming(object, cmd, cookie, params.toArray());
			params.clear();
			state = ReadState.HEADER;
			return m;
		}
		
		return null;
	}
	
	public void read() throws IOException {
		Message m;
		while ((m = readMessage()) != null) {
			messages.add(m);
		}
	}

	private ReadState readHeader() throws IOException {
		channel.read(header);
		if (header.remaining() == 0) {
			header.flip();

			object = IpcObject.fromOrdinal(header.getInt());
			cmd = IpcCommand.fromOrdinal(header.getInt());
			cookie = header.getInt();
			int length = header.getInt();

			if (length > data.capacity()) {
				data = ByteBuffer.allocate(length);
			}
			else {
				data.position(0);
				data.limit(length);
			}
			header.clear();
			return ReadState.PAYLOAD;
		}
		return ReadState.HEADER;
	}

	private ReadState readPayload() throws IOException {
		channel.read(data);
		if (data.remaining() == 0) {
			data.flip();
			while (data.remaining() > 0) {
				if (cmd == IpcCommand.ERROR)
					params.add(TypeId.STRING.read(data));
				else params.add(TypeId.readType(data).read(data));
			}
			return ReadState.DONE;
		}
		return ReadState.PAYLOAD;
	}
}
