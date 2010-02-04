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

import java.util.List;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.Error;
import se.fnord.xmms2.client.MessageListener;
import se.fnord.xmms2.client.internal.IpcCommand;
import se.fnord.xmms2.client.internal.IpcObject;
import se.fnord.xmms2.client.internal.SendMessage;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionNamespace;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.IdQuery;

public final class PlaylistSaveCommand extends AbstractCommand {

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
	private final CollectionExpression expression;

	private final String name;
	private Client client;

	protected final MessageListener message_handler = new MessageListener() {
		public void handleReply(Object[] reply) {
			if (reply.length != 1)
				throw new IllegalArgumentException();

			addReply(reply);
		}
	};

	private final MessageListener insert_collection = new MessageListener() {
		@SuppressWarnings("unchecked")
		public void handleReply(Object[] reply) {
			if (reply.length != 1)
				throw new IllegalArgumentException();

			if (reply[0] instanceof Error) {
				addReply(reply[0]);
			}
			else {
				List<Integer> ids = (List<Integer>) reply[0];
				final CollectionBuilder builder = new CollectionBuilder();

				builder.setType(CollectionType.IDLIST);
				builder.addIds(ids);
				client.enqueue(message_handler, new SendMessage(IpcObject.COLLECTION, IpcCommand.COLLECTION_SAVE, client.newCookie(), name, CollectionNamespace.PLAYLISTS.toString(), builder.build()));
			}
		}
	};

	public PlaylistSaveCommand(String playlist, CollectionExpression expression) {
		this.expression = expression;
		this.name = playlist;
	}

	public PlaylistSaveCommand(String playlist, int[] tracks) {
		this.expression = buildCollection(tracks);
		this.name = playlist;
	}

	public void execute(Client client) {
		this.client = client;
		if  (expression.getType() == CollectionType.IDLIST) {
			client.enqueue(message_handler, new SendMessage(IpcObject.COLLECTION, IpcCommand.COLLECTION_SAVE, client.newCookie(), name, CollectionNamespace.PLAYLISTS.toString(), expression));
		}
		else {
			client.enqueue(insert_collection , new SendMessage(IpcObject.COLLECTION, IpcCommand.QUERY_IDS, client.newCookie(), new IdQuery(expression, 0, 0, new String[0])));
		}
	}
}
