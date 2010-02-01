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

package se.fnord.xmms2.client.types;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.fnord.xmms2.client.Ok;
import se.fnord.xmms2.client.types.marshal.BlobMarshaller;
import se.fnord.xmms2.client.types.marshal.CollectionMarshaller;
import se.fnord.xmms2.client.types.marshal.DictMarshaller;
import se.fnord.xmms2.client.types.marshal.IdQueryMarshaller;
import se.fnord.xmms2.client.types.marshal.InfoQueryMarshaller;
import se.fnord.xmms2.client.types.marshal.IntegerMarshaller;
import se.fnord.xmms2.client.types.marshal.ListMarshaller;
import se.fnord.xmms2.client.types.marshal.OkMarshaller;
import se.fnord.xmms2.client.types.marshal.StringMarshaller;
import se.fnord.xmms2.client.types.marshal.TypeMarshaller;

public enum TypeId {
	NONE(Ok.class, new OkMarshaller()),
	ERROR(null, null),
	INT32(Integer.class, new IntegerMarshaller()),
	STRING(String.class, new StringMarshaller()),
	COLL(CollectionExpression.class, new CollectionMarshaller()),
	BIN(byte[].class, new BlobMarshaller()),
	LIST(List.class, new ListMarshaller()),
	DICT(Dict.class, new DictMarshaller()),
	
	/* Non-XMMSV-types below */
	
	INFO_QUERY(InfoQuery.class, new InfoQueryMarshaller()),
	ID_QUERY(IdQuery.class, new IdQueryMarshaller());

	private final Class<?> mapped_class;
	private final TypeMarshaller reader;

	@SuppressWarnings("unchecked")
	public <A> A read(ByteBuffer buffer) {
		return (A) reader.read(buffer);
	}

	private TypeId(Class<?> c, TypeMarshaller reader) {
		this.mapped_class = c;
		this.reader = reader;
	}
	
	private static final Map<Integer, TypeId> ordinal_map;
	
	public static TypeId readType(ByteBuffer buffer) {
		return TypeId.fromOrdinal(buffer.getInt());
	}

	public TypeId writeType(ByteBuffer buffer) {
		TypeId.INT32.write(buffer, ordinal());
		return this;
	}
	
	public static void writeValue(ByteBuffer buffer, Object o) {
		assert !(o instanceof Enum<?>);

		TypeId.fromType(o.getClass()).write(buffer, o);
	}
	
	public TypeId write(ByteBuffer buffer, Object o) {
		reader.write(buffer, o);
		return this;
	}

	static {
		ordinal_map = new HashMap<Integer, TypeId>(TypeId.values().length);
		for (final TypeId t : TypeId.values()) {
			ordinal_map.put(t.ordinal(), t);
		}
	}

	static private TypeId fromOrdinal(int o) {
		return ordinal_map.get(o);
	}

	static public TypeId fromType(Class<?> clazz) {
		for (final TypeId t : TypeId.values()) {
			if (t.mapped_class != null && t.mapped_class.isAssignableFrom(clazz))
				return t;
		}
		throw new IllegalArgumentException(clazz.getName() + " not recognized");
	}

}
