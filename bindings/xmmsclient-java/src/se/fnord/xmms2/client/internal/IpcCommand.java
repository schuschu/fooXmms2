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


public enum IpcCommand {
	/* Main */
	HELLO, // 0
	QUIT,
	REPLY,
	ERROR,
	PLUGIN_LIST,
	STATS,

	/* Playlist */
	SHUFFLE, // 6
	SET_POS,
	SET_POS_REL,
	ADD_URL,
	ADD_ID,
	ADD_IDLIST,
	ADD_COLL,
	REMOVE_ENTRY,
	MOVE_ENTRY,
	CLEAR,
	SORT,
	LIST,
	CURRENT_POS,
	CURRENT_ACTIVE,
	INSERT_URL,
	INSERT_ID,
	INSERT_COLL,
	LOAD,
	RADD,
	RINSERT,

	/* Config */
	GETVALUE, // 25
	SETVALUE,
	REGVALUE,
	LISTVALUES,

	/* output */
	START,  // 29
	STOP,
	PAUSE,
	DECODER_KILL,
	CPLAYTIME,
	SEEKMS,
	SEEKMS_REL,
	SEEKSAMPLES,
	SEEKSAMPLES_REL,
	OUTPUT_STATUS,
	CURRENTID,
	VOLUME_SET,
	VOLUME_GET,

	/* Medialib */
	INFO, // 42
	PATH_IMPORT,
	REHASH,
	GET_ID,
	REMOVE_ID,
	PROPERTY_SET_STR,
	PROPERTY_SET_INT,
	PROPERTY_REMOVE,
	MOVE_URL,

	/* Collection */
	COLLECTION_GET,
	COLLECTION_LIST,
	COLLECTION_SAVE,
	COLLECTION_REMOVE,
	COLLECTION_FIND,
	COLLECTION_RENAME,
	QUERY_IDS,
	QUERY_INFOS,
	IDLIST_FROM_PLS,
	COLLECTION_SYNC,

	/* Signal subsystem */
	SIGNAL,
	BROADCAST,

	/* xform object */
	BROWSE,

	/* bindata object */
	GET_DATA,
	ADD_DATA,
	REMOVE_DATA,
	LIST_DATA,

	/* visualization */
	VISUALIZATION_QUERY_VERSION,
	VISUALIZATION_REGISTER,
	VISUALIZATION_INIT_SHM,
	VISUALIZATION_INIT_UDP,
	VISUALIZATION_PROPERTY,
	VISUALIZATION_PROPERTIES,
	VISUALIZATION_SHUTDOWN,
	
	/* end */
	END;

    private static final OrdinalMap<IpcCommand> ordinal_map = OrdinalMap.populate(IpcCommand.class);

	public static IpcCommand fromOrdinal(int o) {
		return ordinal_map.get(o);
	}
}
