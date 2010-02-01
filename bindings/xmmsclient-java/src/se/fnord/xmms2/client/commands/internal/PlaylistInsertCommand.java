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

package se.fnord.xmms2.client.commands.internal;

import java.util.ArrayList;

import se.fnord.xmms2.client.internal.IpcCommand;
import se.fnord.xmms2.client.internal.IpcObject;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionType;

public final class PlaylistInsertCommand extends AbstractSimpleCommand {
	private static CollectionExpression buildCollection(int[] tracks) {
		final CollectionBuilder builder = new CollectionBuilder();

		builder.setType(CollectionType.IDLIST);
		if (tracks != null) {
			for (final int id : tracks) {
				builder.addId(id);
			}
		}
		return builder.build();
	}

	public PlaylistInsertCommand(String playlist, CollectionExpression expression,
			int to) {
		super(IpcObject.PLAYLIST, IpcCommand.INSERT_COLL, playlist, to, expression, new ArrayList<String>());
	}

	public PlaylistInsertCommand(String playlist, int[] tracks,
			int to) {
		super(IpcObject.PLAYLIST, IpcCommand.INSERT_COLL, playlist, to, buildCollection(tracks), new ArrayList<String>());
	}
}
