package se.fnord.xmms2.client.snippets;

import java.util.Arrays;
import java.util.List;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientFactory;
import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.Dict;
import se.fnord.xmms2.client.types.InfoQuery;

public class ListAll {
	final static List<String> ORDER_BY = Arrays.asList(new String[0]);
	final static List<String> GROUP_BY = Arrays.asList(new String[0]);
	final static List<String> QUERY_FIELDS = Arrays.asList(new String[] {
		"id",
		"artist",
		"title"
	});

	public static void main(String[] args) throws InterruptedException  {
		Client client = ClientFactory.create("MyClient", "tcp://127.0.0.1:1234");
		client.start();

		Command c = Collection.query(new InfoQuery(CollectionBuilder.getAllMediaReference(), 0, 0, ORDER_BY, QUERY_FIELDS, GROUP_BY));
		List<Dict> l = c.executeSync(client);
		for (Dict track : l) {
			System.out.println(track.get("artist") + " - " + track.get("title"));
		}
	}
}

