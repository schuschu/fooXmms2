package org.dyndns.schuschu.xmms2client.backend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.Dict;
import se.fnord.xmms2.client.types.InfoQuery;

public class FooBackendMediaPlaylist extends FooBackendMedia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6791163548568077012L;

	public FooBackendMediaPlaylist(String format, String filter, Client client,
			FooInterfaceViewElement view) {
		super(format, filter, client, view);
		debug("FooBackendMediaPlaylist");
	}

	public void playSelection() {
		debug("playSelection");

		int[] ids = getView().getIndices();

		if (ids.length == 1) {

			Command cs = Playlist.setPos(ids[0]);
			Command ct = Playback.tickle();
			Command cp = Playback.play();
			try {

				cs.executeSync(client);
				ct.executeSync(client);
				cp.executeSync(client);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}
	}

	public void removeSelection() {
		debug("removeSelection");

		int[] ids = getView().getIndices();
		if (ids.length > 0) {
			Command c = Playlist.removeEntries(Playlist.ACTIVE_PLAYLIST, ids);
			c.execute(client);

		}
	}

	private List<Integer> getPlaylistIds(Client client)
			throws InterruptedException, CommandErrorException {
		Command command = Playlist.listEntries(Playlist.ACTIVE_PLAYLIST);

		return command.executeSync(client);
	}

	private Map<Integer, Dict> getTrackInfo(Client client, List<Integer> ids)
			throws InterruptedException, CommandErrorException {
		CollectionBuilder builder = new CollectionBuilder();

		builder.setType(CollectionType.IDLIST);
		builder.addIds(ids);

		List<String> temp_query = query_fields;
		if (!temp_query.contains("id")) {
			temp_query.add("id");
		}

		InfoQuery query = new InfoQuery(builder.build(), 0, 0, Arrays
				.asList(new String[0]), temp_query, Arrays
				.asList(new String[0]));

		Command command = se.fnord.xmms2.client.commands.Collection
				.query(query);
		
		List<Dict> list = command.executeSync(client);

		Map<Integer, Dict> trackMap = new HashMap<Integer, Dict>();
		for (Dict track : list ) {
			trackMap.put((Integer) track.get("id"), track);
		}

		return trackMap;
	}

	@Override
	protected Vector<String> createContent(List<Dict> Database) {
		debug("createContent");
		Vector<String> Content = new Vector<String>();

		setCurrentPos(-1);

		try {
			List<Integer> ids = getPlaylistIds(getClient());
			Map<Integer, Dict> tracks = getTrackInfo(getClient(), ids);
			int i = 0;
			for (Integer id : ids) {

				if (id == getCurrent()) {
					setCurrentPos(i);
				}

				Dict track = tracks.get(id);
				Content.add(createTokenString(getFormat(), track));

				i++;
			}
		} catch (CommandErrorException e) {
			// TODO: think about this
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
		return Content;
	}

	@Override
	public void selectionChanged() {
		debug("selectionChanged");
		// TODO: think of use for this
	}
}
