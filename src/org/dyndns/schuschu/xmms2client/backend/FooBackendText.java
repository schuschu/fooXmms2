package org.dyndns.schuschu.xmms2client.backend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.debug.FooDebug;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceDebug;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackendText;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceText;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Element;

import se.fnord.xmms2.client.commands.Collection;
import se.fnord.xmms2.client.commands.Command;
import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionType;
import se.fnord.xmms2.client.types.Dict;
import se.fnord.xmms2.client.types.InfoQuery;
import se.fnord.xmms2.client.types.PlaybackStatus;

public class FooBackendText implements FooInterfaceBackendText,FooInterfaceDebug {

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
	private PlaybackStatus status;

	private String text;

	public FooBackendText(String format, FooInterfaceText view) {
		debug("FooBackendText");
		setView(view);
		setFormat(format);
	}

	public void refresh() {
		debug("refresh");

		setText(createText());
	}
	
	private String getStatusMessage() {
		switch (status) {
		case PAUSE:
			return "paused";
		case PLAY:
			return "playing";
		case STOP:
			return "stopped";
		}
		return null;
	}

	public String createText() {
		debug("createText");

		String tokenString = format;
		String current;

		for (String match : query_fields) {
			//TODO: find good message
			if (currentTrack == null) {
				return "not playing";
			}
			if (currentTrack.get(match) == null) {
				current = new String("no " + match);
			} else {
				current = currentTrack.get(match).toString();
			}

			// TODO: cleanup
			if (match.equals("duration")) {
				DateFormat df = new SimpleDateFormat("mm':'ss");
				current = df.format(new Date(Integer.parseInt(current)));
			} else if (match.equals("status")) {
				if (status != null) {
					tokenString = tokenString.replaceAll("%status%", getStatusMessage());
				} else {
					tokenString = tokenString.replaceAll("%status%",
							"FATAAAAL!");
				}
			} else if (match.equals("currentTime")) {
				if (currentTime != null) {
					tokenString = tokenString.replaceAll("%currentTime%",
							currentTime);
				} else {
					tokenString = tokenString.replaceAll("%currentTime%",
							"--:--");
				}
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
		view.setBackend(this);
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

	@Override
	public void setCurrent(int currentId) {
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

			currentTrack = list.isEmpty() ? null : list.get(0);

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

	private void setQueryFields(List<String> query_fields) {
		debug("setQueryFields");
		this.query_fields = query_fields;
	}

	public void setStatus(PlaybackStatus status) {
		debug("setStatus");
		this.status = status;
		refresh();
	}
	
	public static void registerFactory(){
		//BACKEND
		
		FooFactorySub factory = new FooFactorySub() {
			
			@Override
			public Object create(Element element) {
				
				// name equals variable name, no default
				String name = element.getAttribute("name");

				// TODO: docu
				// format (so documentation for further infos), no default
				String format = element.getAttribute("format");

				// get the parent nodes name for view (since backends are always direct
				// below (hirachical) their view element)
				Element father = (Element) element.getParentNode();
				String view = father.getAttribute("name");
				
				debug("creating FooBackendText " + name);

				FooBackendText textBackend = new FooBackendText(format,
						getViewText(view));
				textBackend.setName(name);

				FooFactory.putBackend(name, textBackend);
				return textBackend;
			}
			
			private FooInterfaceText getViewText(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooInterfaceText) {
					return (FooInterfaceText) o;
				}
				return null;
			}
		};
		
		FooFactory.factories.put("FooBackendText", factory);
		
	}

}
