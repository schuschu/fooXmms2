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
import java.util.List;

import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.IdQuery;
import se.fnord.xmms2.client.types.TypeId;

public class IdQueryMarshaller implements TypeMarshaller {
	public Object read(ByteBuffer buffer) {
		CollectionExpression coll = TypeId.COLL.read(buffer);
		int limit_start = TypeId.INT32.read(buffer);
		int limit_end = TypeId.INT32.read(buffer);
		List<String> ordering = TypeId.LIST.read(buffer);
		return new IdQuery(coll, limit_start, limit_end, ordering.toArray(new String[ordering.size()]));
	}

	public void write(ByteBuffer buffer, Object o) {
		IdQuery q = (IdQuery) o;
		TypeId.COLL.write(buffer, q.getCollection());
		TypeId.INT32.write(buffer, q.getStart());
		TypeId.INT32.write(buffer, q.getLength());
		TypeId.LIST.write(buffer, q.getOrdering());
	}

}
