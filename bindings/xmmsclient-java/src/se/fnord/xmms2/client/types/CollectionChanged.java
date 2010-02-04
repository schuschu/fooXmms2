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

public class CollectionChanged {
	public final CollectionNamespace namespace;
	public final String name;
	public final CollectionChangeAction action;
	public final String newname;

	public CollectionChanged(Dict d) {
		final int type_id = (Integer) d.get("type");
		final String namespace_id = (String) d.get("namespace");

		this.name = (String) d.get("name");
		this.newname = (String) d.get("newname");
		this.namespace = CollectionNamespace.fromString(namespace_id);
		this.action = CollectionChangeAction.fromOrdinal((int) type_id);
	}
}
