package se.fnord.xmms2.client.snippets;

import java.net.URI;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientFactory;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playlist;

public class AddUrl {
	public static void main(String[] args) throws InterruptedException, CommandErrorException {
		Client client = ClientFactory.create("MyClient", "tcp://127.0.0.1:1234");
		client.start();

		Command command = Playlist.insert(Playlist.ACTIVE_PLAYLIST, URI.create("http://160.79.128.40:7234"), 1);
		
		try {
			Object o = command.executeSync(client);
		
			System.out.println(o);
		}
		catch(CommandErrorException e) {
			e.printStackTrace();
		}
	}
}
