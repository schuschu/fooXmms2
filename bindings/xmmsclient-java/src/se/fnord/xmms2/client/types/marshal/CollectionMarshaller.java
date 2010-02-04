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
import java.util.Map;

import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.TypeId;

public class CollectionMarshaller implements TypeMarshaller {

	public Object read(ByteBuffer buffer) {
		final CollectionBuilder builder = new CollectionBuilder();

		builder.setType(CollectionType.fromInteger(buffer.getInt()));
		final int attr_len = TypeId.INT32.read(buffer);
		for (int i = 0; i < attr_len; i++) {
			final String key = TypeId.STRING.read(buffer);
			final String value = TypeId.STRING.read(buffer);
			builder.addAttribute(key, value);
		}

		final int idlist_len = TypeId.INT32.read(buffer);
		for (int i = 0; i < idlist_len; i++) {
			builder.addId((Integer)TypeId.INT32.read(buffer));
		}

		final int ops_len = TypeId.INT32.read(buffer);
		for (int i = 0; i < ops_len; i++) {
			builder.addOp((CollectionExpression) read(buffer));
		}

		return builder.build();
	}

	public void write(ByteBuffer buffer, Object o) {
		CollectionExpression e = (CollectionExpression) o;
		
		TypeId.INT32.write(buffer, e.getType().ordinal());

		/* I want attributes to be a normal Dict :/ */
		final Map<String, String> m = e.getAttributes();
		TypeId.INT32.write(buffer, m.size());
		for (final Map.Entry<String, String> entry : m.entrySet()) {
			TypeId.STRING.write(buffer, entry.getKey());
			TypeId.STRING.write(buffer, entry.getValue());
		}

		/* TODO: Why are these not lists? */
		List<Integer> ids = e.getIdList();
		TypeId.INT32.write(buffer, ids.size());
		for (Integer id: ids)
			TypeId.INT32.write(buffer, id);

		List<CollectionExpression> ops = e.getOps();
		TypeId.INT32.write(buffer, ops.size());
		for (CollectionExpression coll: ops)
			TypeId.COLL.write(buffer, coll);
	}

}
