package se.fnord.xmms2.client.snippets;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientFactory;
import se.fnord.xmms2.client.CommandErrorException;
import se.fnord.xmms2.client.commands.Medialib;
import se.fnord.xmms2.client.types.Dict;

public class MediaInfo {

	public static void main(String[] args) throws InterruptedException, CommandErrorException {
		Client client = ClientFactory.create("MyClient", "tcp://127.0.0.1:1234");
		client.start();

		Dict d = Medialib.info(7).executeSync(client);
		for (String key: d.keySet()) {
			Dict dd = (Dict) d.get(key);
			for (String key2 : dd.keySet())
				System.out.println(key + "  " + key2 + "  " + dd.get(key2));
		}
	}
}
