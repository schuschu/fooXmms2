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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/* Objects of this class should never be serialized */
@SuppressWarnings("serial")
public final class OrdinalMap<V extends Enum<V>> extends HashMap<Integer, V> {
	@SuppressWarnings("unchecked")
	public static <U extends Enum<U>> OrdinalMap<U> populate(Class<U> c) {
		try {
			final U[] entries = (U[]) c.getMethod("values").invoke(null);
			final OrdinalMap<U> map = new OrdinalMap<U>(entries.length);
			for (final U e : entries) {
				map.put(e.ordinal(), e);
			}
			return map;
		} catch (final IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (final NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	private OrdinalMap(int length) {
		super(length);
	}
}