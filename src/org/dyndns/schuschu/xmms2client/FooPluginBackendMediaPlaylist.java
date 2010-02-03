package org.dyndns.schuschu.xmms2client;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;
import se.fnord.xmms2.client.commands.Playlist;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.Dict;
import se.fnord.xmms2.client.types.InfoQuery;

public class FooPluginBackendMediaPlaylist extends FooPluginBackendMedia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6791163548568077012L;

	public FooPluginBackendMediaPlaylist(String format, String filter,
			Client client, FooInterfaceViewElement view) {
		super(format, filter, client, view);
	}
	
	public void playSelection() {

		int[] ids = getView().getIndices();

		if (ids.length == 1) {

			Command cs = Playlist.setPos(ids[0]);
			Command cp = Playback.tickle();
			try {

				cs.executeSync(getClient());
				cp.executeSync(getClient());

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public void removeSelection() {
		
		int[] ids = getView().getIndices();
		
		Command c = Playlist.removeEntries(Playlist.ACTIVE_PLAYLIST, ids);
		
		try {
			c.executeSync(getClient());
			// TODO: replace with broadcast!
			getContentProvider().generateFilteredContent();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected Vector<String> createContent(List<Dict> Database) {
		Vector<String> Content = new Vector<String>();
		
		FooPluginBackendPlaylist hack = (FooPluginBackendPlaylist) getContentProvider();

		List<Integer> ids = hack.getPlayListOrder();

		try {

			for (int id : ids) {

				CollectionBuilder cb = new CollectionBuilder();
				cb.setType(CollectionType.IDLIST);
				cb.addId(id);

				Command c = Collection.query(new InfoQuery(cb.build(), 0, 0,
						Arrays.asList(new String[0]), getQueryFields(), Arrays
								.asList(new String[0])));

				List<Dict> all = c.executeSync(getClient());

				for (Dict token : all) {
					Content.add(createTokenString(getFormat(), token));
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Content;
	}
	
	@Override
	public void selectionChanged() {
		//not needed
	}
}
