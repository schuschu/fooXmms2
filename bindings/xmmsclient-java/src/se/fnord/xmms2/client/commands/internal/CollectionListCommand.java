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

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.MessageListener;
import se.fnord.xmms2.client.internal.IpcCommand;
import se.fnord.xmms2.client.internal.IpcObject;
import se.fnord.xmms2.client.internal.SendMessage;
import se.fnord.xmms2.client.types.CollectionNamespace;

public final class CollectionListCommand extends AbstractCommand {

	private final CollectionNamespace namespace;

	private Set<String> collections;
	private Set<String> playlists;
	private AtomicInteger countdown;

	private final MessageListener playlist_handler = new MessageListener() {
		@SuppressWarnings("unchecked")
		public void handleReply(Object[] reply) {
			if (reply.length != 1)
				throw new IllegalArgumentException();

			playlists = new HashSet<String>((List<String>) reply[0]);
			countdown.decrementAndGet();
			collate();
		}
	};

	private final MessageListener collection_handler = new MessageListener() {
		@SuppressWarnings("unchecked")
		public void handleReply(Object[] reply) {
			if (reply.length != 1)
				throw new IllegalArgumentException();

			collections = new HashSet<String>((List<String>) reply[0]);
			countdown.decrementAndGet();
			collate();
		}
	};

	public CollectionListCommand(CollectionNamespace namespace) {
		this.namespace = namespace;
	}

	private final void collate() {
		if (countdown.get() == 0) {
			final Map<CollectionNamespace, Set<String>> result = new EnumMap<CollectionNamespace, Set<String>>(CollectionNamespace.class);
			if (collections != null) {
				result.put(CollectionNamespace.COLLECTIONS, collections);
			}
			if (playlists != null) {
				result.put(CollectionNamespace.PLAYLISTS, playlists);
			}
			addReply(Collections.unmodifiableMap(result));
		}
	}


	public void execute(Client client) {
		switch (namespace) {
		case ALL:
			this.countdown = new AtomicInteger(2);
			client.enqueue(collection_handler, new SendMessage(IpcObject.COLLECTION, IpcCommand.COLLECTION_LIST, client.newCookie(), CollectionNamespace.COLLECTIONS.toString()));
			client.enqueue(playlist_handler, new SendMessage(IpcObject.COLLECTION, IpcCommand.COLLECTION_LIST, client.newCookie(), CollectionNamespace.PLAYLISTS.toString()));
			break;
		case COLLECTIONS:
			this.countdown = new AtomicInteger(1);
			client.enqueue(collection_handler, new SendMessage(IpcObject.COLLECTION, IpcCommand.COLLECTION_LIST, client.newCookie(), CollectionNamespace.COLLECTIONS.toString()));
			break;
		case PLAYLISTS:
			this.countdown = new AtomicInteger(1);
			client.enqueue(playlist_handler, new SendMessage(IpcObject.COLLECTION, IpcCommand.COLLECTION_LIST, client.newCookie(), CollectionNamespace.PLAYLISTS.toString()));
			break;
		}
	}

}
