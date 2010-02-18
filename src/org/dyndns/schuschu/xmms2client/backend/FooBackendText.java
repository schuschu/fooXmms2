package org.dyndns.schuschu.xmms2client.backend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceText;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.eclipse.swt.widgets.Display;

import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.commands.Playback;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.Dict;
import se.fnord.xmms2.client.types.InfoQuery;

public class FooBackendText {

	private static final boolean DEBUG = FooLoader.DEBUG;
	private String name;

	private FooColor debugForeground = FooColor.BLACK;
	private FooColor debugBackground = FooColor.WHITE;

	private void debug(String message) {
		if (DEBUG) {
			if (FooLoader.VISUAL) {
				FooDebug.setForeground(getDebugForeground());
				FooDebug.setBackground(getDebugBackground());
			}
			System.out.println("debug: " + getName() + " " + message);
		}
	}

	private Vector<String> possible_values = new Vector<String>();
	private List<String> query_fields = Arrays.asList(new String[0]);

	private FooInterfaceText view;
	private String format;
	private Dict currentTrack;
	private String currentTime;

	private String text;

	private Thread trackUpdater;
	private Command c;
	private boolean run;

	public FooBackendText(String format) {
		debug("FooBackendText");
		setFormat(format);
		createTrackUpdater();
	}

	private void createTrackUpdater() {

		Command t = Playback.currentId();
		try {
			int id = t.executeSync(FooLoader.CLIENT);
			setCurrentId(id);
		} catch (InterruptedException e1) {
			Thread.currentThread().interrupt();
		}

		c = Playback.currentIdBroadcast();
		c.execute(FooLoader.CLIENT);

		run = true;

		Runnable updater = new Runnable() {

			@Override
			public void run() {
				while (run)
					try {
						int id = c.waitReply();
						setCurrentId(id);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
			}
		};

		trackUpdater = new Thread(updater);
		trackUpdater.start();
	}

	public void done() {
		run = false;
	}

	public void refresh() {
		debug("refresh");

		setText(createText());
	}

	public String createText() {
		debug("createText");

		String tokenString = format;
		String current;

		if (currentTime != null) {
			tokenString = tokenString.replaceAll("%currentTime%", currentTime);
		}
		for (String match : query_fields) {
			if (currentTrack == null) {
				return "error";
			}
			if (currentTrack.get(match) == null) {
				current = new String("no " + match);
			} else {
				current = currentTrack.get(match).toString();
			}

			if (match.equals("duration")) {
				DateFormat df = new SimpleDateFormat("mm':'ss");
				current = df.format(new Date(Integer.parseInt(current)));
			}

			tokenString = tokenString.replaceAll("%" + match + "%", current);

		}

		return tokenString;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDebugForeground(FooColor debugForeground) {
		this.debugForeground = debugForeground;
	}

	public FooColor getDebugForeground() {
		return debugForeground;
	}

	public void setDebugBackground(FooColor debugBackground) {
		this.debugBackground = debugBackground;
	}

	public FooColor getDebugBackground() {
		return debugBackground;
	}

	public void setView(FooInterfaceText view) {
		debug("setView");
		this.view = view;
	}

	public FooInterfaceText getView() {
		debug("getView");
		return view;
	}

	public void setText(String string) {
		debug("setText");
		this.text = string;
		if (view != null) {

			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					view.setText(text);

				}
			});
		}
	}

	public void setFormat(String format) {
		debug("setFormat");

		Vector<String> newQuery = new Vector<String>();

		evaluateFields(format);

		for (String match : possible_values) {
			if (format.contains("%" + match + "%")) {
				newQuery.add(match);
			}
		}
		this.format = format;

		setQueryFields(newQuery);
	}

	public void evaluateFields(String format) {
		debug("evaluateFields");
		// TODO: need to find a better way to do this! Don't understand regex
		// that well
		Vector<String> possible = new Vector<String>();
		boolean found = false;
		StringBuffer temp = new StringBuffer();

		for (int i = 0; i < format.length(); i++) {
			if (format.charAt(i) == '%') {
				found = !found;
				if (!found) {
					possible.add(temp.toString());
					temp = new StringBuffer();
				}
				i++;
			}
			if (found) {
				temp.append(format.charAt(i));
			}
		}

		possible_values = possible;
	}

	public String getFormat() {
		debug("getFormat");
		return format;
	}

	private void setCurrentId(int currentId) {
		debug("setCurrentId");

		List<String> temp_query = query_fields;
		if (!temp_query.contains("id")) {
			temp_query.add("id");
		}

		CollectionBuilder cb = new CollectionBuilder();
		cb.setType(CollectionType.IDLIST);
		cb.addId(currentId);

		Command command = Collection.query(new InfoQuery(cb.build(), 0, 0,
				Arrays.asList(new String[0]), query_fields, Arrays
						.asList(new String[0])));

		try {
			List<Dict> list = command.executeSync(FooLoader.CLIENT);

			currentTrack = list.get(0);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		refresh();
	}

	public void setCurrentTime(int time) {
		debug("setTime");

		DateFormat df = new SimpleDateFormat("mm':'ss");
		currentTime = df.format(new Date(time));

		refresh();
	}

	public void setQueryFields(List<String> query_fields) {
		this.query_fields = query_fields;
	}

	public List<String> getQueryFields() {
		return query_fields;
	}

}
