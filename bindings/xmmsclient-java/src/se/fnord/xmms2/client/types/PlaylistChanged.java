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

public class PlaylistChanged {
	public final PlaylistChangeAction action;
	public final int position;
	public final int id;
	public final String name;

	public PlaylistChanged(Dict d) {
		this.name = (String) d.get("name");
		final int type_id = (Integer) d.get("type");

		/* "position" does not always exist */
		Integer l = (Integer) d.get("position");
		this.position = (l == null)?-1:l;

		/* "id" does not always exist */
		l = (Integer) d.get("id");
		id = (l == null)?0:l;
		this.action = PlaylistChangeAction.fromOrdinal((int) type_id);
	}
}
