package se.fnord.xmms2.client.snippets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientFactory;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.Dict;
import se.fnord.xmms2.client.types.InfoQuery;

public class ListPlaylist2 {

	@SuppressWarnings("unchecked")
	private static Map<Integer, Dict> getTrackInfo(Client client, List<Integer> ids) throws InterruptedException, CommandErrorException {
		final CollectionBuilder builder = new CollectionBuilder();
		
		builder.setType(CollectionType.IDLIST);
		builder.addIds(ids);

		final InfoQuery query = new InfoQuery(builder.build(),
				0, 0,
				Arrays.asList(new String[0]),
				Arrays.asList(new String[] {
						"id",
						"artist",
						"title",
					}),
				Arrays.asList(new String[0]));

		final Command command = se.fnord.xmms2.client.commands.Collection.query(query);
		
		/* The result is not sorted in playlist order, so add the
		 * entries to a map for later sorting.
		 */
		final Map<Integer, Dict> trackMap = new HashMap<Integer, Dict>();
		for (Dict track : (List<Dict>) command.executeSync(client)) {
			trackMap.put((Integer) track.get("id"), track); 
		}
		
		return trackMap;
	}

	private static List<Integer> getPlaylistIds(Client client) throws InterruptedException, CommandErrorException {
		Command command = Playlist.listEntries(Playlist.ACTIVE_PLAYLIST);
		
		return command.executeSync(client);
	}
	
	public static void main(String[] args) throws InterruptedException, CommandErrorException {
		Client client = ClientFactory.create("MyClient", "tcp://127.0.0.1:1234");
		client.start();

		List<Integer> ids = getPlaylistIds(client);
		Map<Integer, Dict> tracks = getTrackInfo(client, ids);
		
		for (Integer id : ids) {
			Dict track = tracks.get(id); 
			if (track != null) {
				System.out.printf("%s - %s\n", track.get("artist"), track.get("title"));
			}
			else {
				System.out.println("???\n");
			}
		}
	}
}
