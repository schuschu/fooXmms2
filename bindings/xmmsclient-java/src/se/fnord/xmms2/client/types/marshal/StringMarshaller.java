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

package se.fnord.xmms2.client.types.marshal;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class StringMarshaller implements TypeMarshaller {
	public Object read(ByteBuffer buffer) {
		try {
			final int len = buffer.getInt();
			byte[] bytes = new byte[len];
			buffer.get(bytes);
			if (bytes[len - 1] == 0) // Trim trailing \0-terminator
				return new String(bytes, 0, len - 1, "utf-8");
			return new String(bytes, 0, len, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void write(ByteBuffer buffer, Object o) {
		try {
			final byte[] b = ((String) o).getBytes("utf-8");
			buffer.putInt(b.length + 1);
			buffer.put(b);
			buffer.put((byte)0);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}

