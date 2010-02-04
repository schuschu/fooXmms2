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

package se.fnord.xmms2.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.DictBuilder;
import se.fnord.xmms2.client.types.IdQuery;
import se.fnord.xmms2.client.types.InfoQuery;
import se.fnord.xmms2.client.types.TypeId;

public class TypeMarshallingTest {
	private final static String TEST = "test ŒŠš ";
	private static final Charset utf8 = Charset.forName("UTF-8");
	
	private void roundTrip(ByteBuffer in, Object expected) {
		ByteBuffer out = ByteBuffer.allocate(256);
		Object o = TypeId.readType(in).read(in);
		TypeId.fromType(o.getClass()).writeType(out).write(out, o);
		in.flip();
		out.flip();

		assertEquals(expected.getClass(), o.getClass());
		if (expected.getClass() == byte[].class)
			assertArrayEquals((byte[]) expected,(byte[])  o);
		else
			assertEquals(expected, o);
		assertEquals(0, in.compareTo(out));
	}
	
	@Test
	public void testString() {
		ByteBuffer teststring = utf8.encode(TEST);
		ByteBuffer bb = ByteBuffer.allocate(256);
		
		/* Test \0-term */
		bb.putInt(TypeId.STRING.ordinal())
			.putInt(teststring.remaining() + 1)
			.put(teststring)
			.put((byte)0)
			.flip();
	}

	@Test
	public void testInteger() {
		ByteBuffer bb = ByteBuffer.allocate(256)
			.putInt(TypeId.INT32.ordinal())
			.putInt(1);
		bb.flip();
		
		roundTrip(bb, Integer.valueOf(1));
	}

	@Test
	public void testList() {
		ByteBuffer bb = ByteBuffer.allocate(256)
			.putInt(TypeId.LIST.ordinal())
			.putInt(2)
			.putInt(TypeId.INT32.ordinal())
			.putInt(1)
			.putInt(TypeId.INT32.ordinal())
			.putInt(2);
		bb.flip();
		
		List<Integer> l = new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		
		roundTrip(bb, l);
	}

	@Test
	public void testDict() {
		ByteBuffer teststring = utf8.encode(TEST);

		ByteBuffer bb = ByteBuffer.allocate(256)
			.putInt(TypeId.DICT.ordinal())
			.putInt(2)
			/* Key-string */
			.putInt(6)
			.put(utf8.encode("key 0"))
			.put((byte) 0)
			/* Value */
			.putInt(TypeId.INT32.ordinal())
			.putInt(1)
			/* Key-string */
			.putInt(6)
			.put(utf8.encode("key 1"))
			.put((byte) 0)
			/* Value */
			.putInt(TypeId.STRING.ordinal())
			.putInt(teststring.remaining() + 1)
			.put(teststring)
			.put((byte) 0);
		bb.flip();
		
		DictBuilder db = new DictBuilder();
		db.add("key 0", Integer.valueOf(1));
		db.add("key 1", TEST);
		
		roundTrip(bb, db.build());
	}

	@Test
	public void testBlob() {
		byte[] data = new byte[] { 1, 2, 3, 4 };
		ByteBuffer bb = ByteBuffer.allocate(256)
			.putInt(TypeId.BIN.ordinal())
			.putInt(data.length)
			.put(data);
		bb.flip();
		
		roundTrip(bb, data);
	}

	@Test
	public void testIdQuery() {
		
		IdQuery idq = new IdQuery(CollectionBuilder.getAllMediaReference(), 32, 14, new String[] { "Banan", "Bacon" });

		ByteBuffer bb = ByteBuffer.allocate(256);
		TypeId.INT32.write(bb, TypeId.ID_QUERY.ordinal()); /* This would normally never get written */
		TypeId.COLL.write(bb, CollectionBuilder.getAllMediaReference());
		TypeId.INT32.write(bb, 32);
		TypeId.INT32.write(bb, 14);
		TypeId.LIST.write(bb, Arrays.asList(new String[] { "Banan", "Bacon" }));
		bb.flip();
		roundTrip(bb, idq);
	}

	@Test
	public void testInfoQuery() {
		
		InfoQuery nfoq = new InfoQuery(CollectionBuilder.getAllMediaReference(), 32, 14,
				Arrays.asList(new String[] { "Banan", "Bacon" }),
				Arrays.asList(new String[] { "Snowboard", "Badboll" }),
				Arrays.asList(new String[] { "Skruvmejsel", "MŒlarverkstad" }));

		ByteBuffer bb = ByteBuffer.allocate(256);
		TypeId.INT32.write(bb, TypeId.INFO_QUERY.ordinal()); /* This would normally never get written */
		TypeId.COLL.write(bb, CollectionBuilder.getAllMediaReference());
		TypeId.INT32.write(bb, 32);
		TypeId.INT32.write(bb, 14);
		TypeId.LIST.write(bb, Arrays.asList(new String[] { "Banan", "Bacon" }));
		TypeId.LIST.write(bb, Arrays.asList(new String[] { "Snowboard", "Badboll" }));
		TypeId.LIST.write(bb, Arrays.asList(new String[] { "Skruvmejsel", "MŒlarverkstad" }));
		bb.flip();
		roundTrip(bb, nfoq);
	}

	@Test
	public void testCollection() {
		ByteBuffer bb = ByteBuffer.allocate(256);
		TypeId.INT32.write(bb, TypeId.COLL.ordinal());
		TypeId.INT32.write(bb, CollectionType.REFERENCE.ordinal());
		TypeId.INT32.write(bb, 1);
		TypeId.STRING.write(bb, "reference");
		TypeId.STRING.write(bb, "All Media");
		TypeId.INT32.write(bb, 0);
		TypeId.INT32.write(bb, 0);
		bb.flip();
		
		roundTrip(bb, CollectionBuilder.getAllMediaReference());
	}
	
}
