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

package se.fnord.xmms2.client.internal;

import java.util.HashMap;

public enum IpcObject {
	MAIN, PLAYLIST, CONFIG, OUTPUT, MEDIALIB, COLLECTION, SIGNAL, VISUALISATION, MEDIAINFO_READER, XFORM, BINDATA, END;

	private static final HashMap<Integer, IpcObject> ordinal_map;

	static {
		ordinal_map = new HashMap<Integer, IpcObject>(IpcObject.values().length);
		for (final IpcObject t : IpcObject.values()) {
			ordinal_map.put(t.ordinal(), t);
		}
	}

	public static IpcObject fromOrdinal(int o) {
		return ordinal_map.get(o);
	}
}
