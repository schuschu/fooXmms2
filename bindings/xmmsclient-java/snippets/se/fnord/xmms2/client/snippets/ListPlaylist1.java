package se.fnord.xmms2.client.snippets;

import java.util.List;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientFactory;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;

public class ListPlaylist1 {
	
	private static List<Integer> getPlaylistIds(Client client) throws InterruptedException, CommandErrorException {
		Command command = Playlist.listEntries(Playlist.ACTIVE_PLAYLIST);
		
		return command.executeSync(client);
	}

	public static void main(String[] args) throws InterruptedException, CommandErrorException {
		Client client = ClientFactory.create("MyClient", "tcp://127.0.0.1:1234");
		client.start();

		for (Integer id : getPlaylistIds(client)) {
			System.out.println(id);
		}
	}
}

