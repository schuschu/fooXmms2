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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DictBuilder {
	private static final class DictImpl implements Dict {
		private final Map<String, Object> entries;

		public DictImpl(Map<String, Object> entries) {
			this.entries = Collections.unmodifiableMap(entries);
		}

		public void clear() {
			entries.clear();
		}

		public boolean containsKey(Object key) {
			return entries.containsKey(key);
		}

		public boolean containsValue(Object value) {
			return entries.containsValue(value);
		}

		public Set<Entry<String, Object>> entrySet() {
			return entries.entrySet();
		}

		@Override
		public boolean equals(Object o) {
			return entries.equals(o);
		}

		public Object get(Object key) {
			return entries.get(key);
		}

		@Override
		public int hashCode() {
			return entries.hashCode();
		}

		public boolean isEmpty() {
			return entries.isEmpty();
		}

		public Set<String> keySet() {
			return entries.keySet();
		}

		public Object put(String key, Object value) {
			return entries.put(key, value);
		}

		public void putAll(Map<? extends String, ? extends Object> m) {
			entries.putAll(m);
		}

		public Object remove(Object key) {
			return entries.remove(key);
		}

		public int size() {
			return entries.size();
		}

		public Collection<Object> values() {
			return entries.values();
		}
	}

	private final Map<String, Object> entries  = new HashMap<String, Object>();

	public DictBuilder add(String key, Object value) {
		entries.put(key, value);
		return this;
	}

	public Dict build() {
		return new DictImpl(entries);
	}

	public void clear() {
		entries.clear();
	}

}
