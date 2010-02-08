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

package se.fnord.xmms2.client.commands;

import java.net.URI;
import java.util.List;

import se.fnord.xmms2.client.commands.internal.BasicCommand;
import se.fnord.xmms2.client.commands.internal.PlaylistAppendCommand;
import se.fnord.xmms2.client.commands.internal.PlaylistChangedBroadcastCommand;
import se.fnord.xmms2.client.commands.internal.PlaylistInsertCommand;
import se.fnord.xmms2.client.commands.internal.PlaylistMoveCommand;
import se.fnord.xmms2.client.commands.internal.PlaylistRemoveEntriesCommand;
import se.fnord.xmms2.client.commands.internal.PlaylistSaveCommand;
import se.fnord.xmms2.client.internal.IpcCommand;
import se.fnord.xmms2.client.internal.IpcObject;
import se.fnord.xmms2.client.internal.IpcSignal;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionNamespace;

/**
 * Contains static factory methods to create commands for playlist
 * related activities.
 */

public class Playlist {
	public static final String ACTIVE_PLAYLIST = "_active";
	public static final String DEFAULT_PLAYLIST = "Default";

	/**
	 * Creates a command that requests the given ids to be appended
	 * to a playlist.
	 * @param playlist the playlist.
	 * @param tracks array of ids to append to the playlist.
	 * @return The command that will perform the request.
	 */
	public static Command append(String playlist,
			int[] tracks) {
		return new PlaylistAppendCommand(playlist, tracks);
	}

	/**
	 * Creates a command that requests a broadcast notification
	 * whenever a playlist has been updated.
	 * @return The command that will perform the request.
	 */
	public static SignalCommand changeBroadcast() {
		return new PlaylistChangedBroadcastCommand();
	}

	/**
	 * Creates a command that requests the removal of all items in the
	 *   given playlist.
	 * @param playlist the playlist to clear.
	 * @return The command that will perform the request.
	 */
	public static Command clear(String playlist) {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.CLEAR, playlist);
	}

	/**
	 * Creates a command that requests the name of the currently active playlist.
	 * @return The command that will perform the request.
	 */
	public static Command currentActive() {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.CURRENT_ACTIVE);
	}

	/**
	 * Creates a command that requests the current playback position of
	 * the given playlist.
	 * @param playlist the playlist.
	 * @return The command that will perform the request.
	 */
	public static Command currentPos(String playlist) {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.CURRENT_POS, playlist);
	}

	/**
	 * Creates a command that requests a signal notification
	 * whenever the currently playing entry has been changed.
	 * @return The command that will perform the request.
	 */
	public static SignalCommand currentPosBroadcast() {
		return Signal.broadcast(IpcSignal.PLAYLIST_CURRENT_POS);
	}

	/**
	 * Creates a command that requests the result of the given
	 * collection expression to be inserted into a playlist.
	 * @param client The (connected) client to query.
	 * @param playlist the playlist.
	 * @param expression the collection expression.
	 * @param to the destination index.
	 * @return The command that will perform the request.
	 */
	public static Command insert(String playlist,
			CollectionExpression expression, int to) {
		return new PlaylistInsertCommand(playlist, expression, to);
	}

	/**
	 * Creates a command that requests the given ids to be inserted
	 * into a playlist.
	 * @param playlist the playlist.
	 * @param tracks array of ids to add to the playlist.
	 * @param to the destination index.
	 * @return The command that will perform the request.
	 */
	public static Command insert(String playlist,
			int[] ids, int to) {
		return new PlaylistInsertCommand(playlist, ids, to);
	}

	/**
	 * Creates a command that requests the given URI to be inserted
	 * into a playlist.
	 * @param playlist the playlist.
	 * @param uri the uri.
	 * @param to the destination index.
	 * @return The command that will perform the request.
	 */
	public static Command insert(String playlist, URI uri, int to) {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.INSERT_URL, playlist, to, uri.toString());
	}

	/**
	 * Creates a command that requests the id of all entries in
	 * the given playlist.
	 * @param playlist the playlist for which to request the id list.
	 * @return The command that will perform the request.
	 */
	public static Command listEntries(String playlist) {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.LIST, playlist);
	}

	/**
	 * Creates a command that requests a list of all collections in the
	 *   Playlists namespace.
	 * @return The command that will perform the request.
	 */
	public static Command listPlaylists() {
		return Collection.list(CollectionNamespace.PLAYLISTS);
	}

	public static Command load(String playlist) {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.LOAD, playlist);
	}

	/**
	 * Creates a command that requests a broadcast notification
	 * whenever a new playlist has been loaded.
	 * @return The command that will perform the request.
	 */
	public static SignalCommand loadBroadcast() {
		return Signal.broadcast(IpcSignal.PLAYLIST_LOADED);
	}

	/**
	 * Creates a command that requests the playlist items at the
	 * indices listed in from will be moved to a consecutive array
	 * starting at index 'to'.
	 * @param playlist the playlist.
	 * @param from an array of source indices
	 * @param to the destination index.
	 * @return The command that will perform the request.
	 */
	public static Command move(String playlist, int from[], int to) {
		return new PlaylistMoveCommand(playlist, from, to);
	}

	/**
	 * Creates a command that requests the playlist items with the
	 * given ids to be removed from the playlist.
	 * @param playlist the playlist.
	 * @param ids an array of ids.
	 * @return The command that will perform the request.
	 */
	public static Command removeEntries(String playlist, int ids[]) {
		return new PlaylistRemoveEntriesCommand(playlist, ids);
	}

	public static Command save(String playlist,
			CollectionExpression expression) {
		return new PlaylistSaveCommand(playlist, expression);
	}

	public static Command save(String playlist,
			int[] tracks) {
		return new PlaylistSaveCommand(playlist, tracks);
	}
	
	public static Command remove(String playlist) {
		return new BasicCommand(IpcObject.COLLECTION, IpcCommand.COLLECTION_REMOVE, playlist,CollectionNamespace.PLAYLISTS.toString());
	}

	public static Command setPos(int pos) {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.SET_POS, pos);
	}

	public static Command setPosRel(int delta) {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.SET_POS_REL, delta);
	}

	public static Command shuffle(String playlist) {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.SHUFFLE, playlist);
	}

	public static Command sort(String playlist, List<String> properties) {
		return new BasicCommand(IpcObject.PLAYLIST, IpcCommand.SORT, playlist, properties);
	}


}
