package se.fnord.xmms2.client.snippets;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientFactory;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Playback;

public class StartNextStop {
	public static void main(String[] args) throws InterruptedException, CommandErrorException {
		Client client = ClientFactory.create("MyClient", "tcp://127.0.0.1:1234");
		client.start();
		
		try {
			Playback.play().executeSync(client);
			Thread.sleep(4000);
			Playback.next().executeSync(client);
			Thread.sleep(2000);			
			Playback.stop().executeSync(client);
		}
		catch(CommandErrorException e) {
			e.printStackTrace();
		}
		finally {
			client.stop();
		}
	}
}
