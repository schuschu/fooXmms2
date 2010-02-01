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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.fnord.xmms2.client.types.TypeId;

public class ListMarshaller implements TypeMarshaller {

	public Object read(ByteBuffer buffer) {
		final int len = buffer.getInt();
		final ArrayList<Object> v = new ArrayList<Object>(len);
		if (len > 0) {
			Object o = TypeId.readType(buffer).read(buffer);
			Class<?> type = o.getClass();
			v.add(o);

			for (int i = 1; i < len; i++) {
				o = TypeId.readType(buffer).read(buffer);
				assert(type.isInstance(o));
				v.add(o);
			}
		}
		return v;
	}

	public void write(ByteBuffer buffer, Object o) {
		final List<?> l = (List<?>) o;
		TypeId.INT32.write(buffer, l.size());
		final Iterator<?> i = l.iterator();
		if (i.hasNext()) {
			final Object first = i.next();
			final TypeId t = TypeId.fromType(first.getClass());
			t.writeType(buffer).write(buffer, first);
			while (i.hasNext()) {
				t.writeType(buffer).write(buffer, i.next());
			}
		}
	}

}
