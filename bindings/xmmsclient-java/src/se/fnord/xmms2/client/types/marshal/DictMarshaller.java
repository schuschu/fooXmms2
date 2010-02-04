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

import java.nio.ByteBuffer;
import java.util.Map;

import se.fnord.xmms2.client.Ok;
import se.fnord.xmms2.client.types.DictBuilder;
import se.fnord.xmms2.client.types.TypeId;

public class DictMarshaller implements TypeMarshaller {
	public Object read(ByteBuffer buffer) {
		final int len = TypeId.INT32.read(buffer);

		final DictBuilder builder = new DictBuilder();
		for (int  i = 0; i < len; i++) {
			final String key = TypeId.STRING.read(buffer);
			
			Object value = TypeId.readType(buffer).read(buffer);
			if (value instanceof Ok) value = null;
			builder.add(key, value);
		}
		return builder.build();
	}
	
	@SuppressWarnings("unchecked")
	public void write(ByteBuffer buffer, Object o) {
		final Map<String, ?> m = (Map<String, ?>) o;
		TypeId.INT32.write(buffer, m.size());
		for (final Map.Entry<String, ?> entry : m.entrySet()) {
			TypeId.STRING.write(buffer, entry.getKey());
			TypeId.fromType(entry.getValue().getClass()).writeType(buffer).write(buffer, entry.getValue());
		}
	}

}
