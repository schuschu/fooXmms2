package org.dyndns.schuschu.xmms2client.backend;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.debug.FooColor;
import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.dyndns.schuschu.xmms2client.factory.FooFactorySub;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.backend.FooInterfaceDebug;
import org.dyndns.schuschu.xmms2client.interfaces.view.FooInterfaceView;
import org.dyndns.schuschu.xmms2client.loader.FooLoader;
import org.dyndns.schuschu.xmms2client.view.element.FooShell;
import org.dyndns.schuschu.xmms2client.view.element.FooTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.Element;

public class FooBackendDebug extends OutputStream implements
		FooInterfaceBackend {

	private String name;

	// TODO: replace with fooview elements?

	private final FooColor defForeground = FooColor.BLACK;
	private final FooColor defBackground = FooColor.WHITE;

	private FooColor foreground;
	private FooColor background;

	private boolean run = true;
	private boolean pause = false;

	private boolean autoscroll = true;

	private FooTable table = null;
	private FooShell shell = null;

	private boolean debugonly;

	private StringBuffer sb;

	private Vector<BufferEntry> buffer;
	private int limit;

	private Thread updater;

	private static FooBackendDebug instance = null;

	public static FooBackendDebug create() {
		if (instance == null) {
			instance = new FooBackendDebug();
		}
		return instance;
	}

	public FooBackendDebug() {

		buffer = new Vector<BufferEntry>();
		sb = new StringBuffer();
		limit = 1000;

		setForeground(defForeground);
		setBackground(defBackground);

		Runnable runable = new Runnable() {

			@Override
			public void run() {
				while (run) {
					waiter();
					if (table != null && !getTable().isDisposed()) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								if (!table.isDisposed()) {
									TableItem item = new TableItem(getTable().getTable(),
											SWT.NONE);
									item.setText(buffer.get(0).getText());

									item.setBackground(Display.getDefault().getSystemColor(
											buffer.get(0).getBackground().getCode()));
									item.setForeground(Display.getDefault().getSystemColor(
											buffer.get(0).getForeground().getCode()));
									buffer.remove(0);

									if (autoscroll) {
										getTable().showItem(item);
									}

									if (limit != 0 && getTable().getItemCount() > limit) {
										getTable().remove(0);
									}
								}
							}
						});
					}
				}
			}
		};

		updater = new Thread(runable);
		updater.start();

		PrintStream out = new PrintStream(this);
		System.setOut(out);

		// Register debug nodes
		registerDebugFactory();

		FooLoader.DOUTPUT = this;
		FooLoader.VISUAL = true;

	}

	public void done() {
		run = false;
	}

	private synchronized void writeline() {
		BufferEntry entry = new BufferEntry();
		entry.setBackground(background);
		entry.setForeground(foreground);
		entry.setText(sb.toString());
		sb.setLength(0);
		buffer.add(entry);
		setForeground(defForeground);
		setBackground(defBackground);
		if (!pause) {
			notify();
		}
	}

	private synchronized void waiter() {
		while (buffer.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public synchronized void refresh() {
		notify();
	}

	@Override
	public void write(int b) throws IOException {
		// TODO: sanity checks

		if ((char) b != '\n' && (char) b != '\r') {
			sb.append((char) b);
		} else {
			if ((char) b == '\n') {
				writeline();
			}
		}
	}

	/*
	 * public Listener createScrollbackListener() { return new Listener() {
	 * 
	 * @Override public void handleEvent(Event arg0) { String temp =
	 * FooInputDialog.show(sShell,
	 * "Please enter new scrollback limit (0 to disable)", "scrollback", limit +
	 * ""); if (temp != null) { try { int i = Integer.parseInt(temp); limit = i;
	 * if (limit != 0) { while (table.getItemCount() > limit) { table.remove(0); }
	 * } } catch (NumberFormatException e) { FooMessageDialog.show(sShell,
	 * "Please enter a valid number", "Error"); } } } }; }
	 */

	/*
	 * public Listener createClearListener() { return new Listener() {
	 * 
	 * @Override public void handleEvent(Event arg0) { table.clearAll();
	 * table.setItemCount(0);
	 * 
	 * } }; }
	 */

	/*
	 * public Listener createSaveListener() { return new Listener() {
	 * 
	 * @Override public void handleEvent(Event arg0) { pause = true; String
	 * selected = FooFileDialog.show(sShell,"Save logfile");
	 * 
	 * if (selected != null) { try { File file = new File(selected);
	 * 
	 * boolean write = true;
	 * 
	 * if (!file.createNewFile()) { write = SWT.YES == FooConfirmationDialog
	 * .show( sShell, file.getName() +
	 * " already exists. Do you want to replace it?", "Replace file"); }
	 * 
	 * if (write) { if (file.canWrite()) { FileWriter fstream = new
	 * FileWriter(file); BufferedWriter out = new BufferedWriter(fstream); for
	 * (TableItem item : table.getItems()) { out.write(item.getText() + "\n"); }
	 * out.close(); } else { FooMessageDialog.show(sShell,
	 * "Can not write to file!", "Error"); } } } catch (IOException e) {
	 * FooMessageDialog.show(sShell, e.getMessage(), "Error"); } } pause = false;
	 * refresh(); } }; }
	 */

	public void setForeground(FooColor foreground) {
		this.foreground = foreground;
	}

	public void setBackground(FooColor background) {
		this.background = background;
	}

	public static void registerDebugFactory() {
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public Object create(Element element) {

				String debugForeground = element.hasAttribute("fg") ? element
						.getAttribute("fg") : "BLACK";
				String debugBackground = element.hasAttribute("bg") ? element
						.getAttribute("bg") : "WHITE";

				Element father = (Element) element.getParentNode();
				String parent = father.getAttribute("name");

				FooInterfaceDebug debug = getDebug(parent);

				debug.setDebugForeground(FooColor.valueOf(debugForeground));
				debug.setDebugBackground(FooColor.valueOf(debugBackground));

				return null;
			}

			private FooInterfaceDebug getDebug(String s) {
				Object o = FooFactory.getBackend(s);
				if (o instanceof FooInterfaceDebug) {
					return (FooInterfaceDebug) o;
				}
				o = FooFactory.getWatch(s);
				if (o instanceof FooInterfaceDebug) {
					return (FooInterfaceDebug) o;
				}

				return null;
			}
		};
		FooFactory.factories.put("FooDebug", factory);
	}

	@Override
	public Vector<String> getContent() {
		// none
		return null;
	}

	@Override
	public FooInterfaceView getView() {
		return getTable();
	}

	@Override
	public void selectionChanged() {
		// ignore
	}

	public static void registerFactory() {
		FooFactorySub factory = new FooFactorySub() {

			@Override
			public Object create(Element element) {
				// name equals variable name, no default
				String name = element.getAttribute("name");

				// get the parent nodes name for view (since backends are always
				// direct
				// below (hirachical) their view element)
				Element father = (Element) element.getParentNode();
				String view = father.getAttribute("name");

				// if debugonly is set the backend will close its shell if debug is not
				// defined, default true
				boolean debugonly = element.hasAttribute("debugonly") ? element.getAttribute("debugonly").equals("true") : true;

				debug("creating FooBackendDebug " + name);

				FooBackendDebug debugBackend = FooBackendDebug.create();
				
				debugBackend.setDebugonly(debugonly);
				debugBackend.setTable(getViewText(view));
				debugBackend.setShell(getShell(element));

				debugBackend.setName(name);

				FooFactory.putBackend(name, debugBackend);

				return debugBackend;
			}

			private FooTable getViewText(String s) {
				Object o = FooFactory.getView(s);
				if (o instanceof FooTable) {
					return (FooTable) o;
				}
				return null;
			}

			private FooShell getShell(Element element) {
				Element root = element;
				do {
					root = (Element) root.getParentNode();
				} while (!root.getNodeName().equals("shell"));

				Object o = FooFactory.getView(root.getAttribute("name"));

				if (o instanceof FooShell) {
					return (FooShell) o;
				}
				return null;

			}
		};

		FooFactory.factories.put("FooBackendDebug", factory);
		FooBackendDebug.create();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setTable(FooTable table) {
		this.table = table;
	}

	public FooTable getTable() {
		return table;
	}

	public void setShell(FooShell shell) {
		this.shell = shell;
		if (!FooLoader.DEBUG && debugonly ) {
			shell.close();
		}
	}

	public FooShell getShell() {
		return shell;
	}

	public void setDebugonly(boolean debugonly) {
		this.debugonly = debugonly;
	}

	public boolean isDebugonly() {
		return debugonly;
	}

}

class BufferEntry {
	private FooColor foreground;
	private FooColor background;
	private String text;

	public void setForeground(FooColor foreground) {
		this.foreground = foreground;
	}

	public FooColor getForeground() {
		return foreground;
	}

	public void setBackground(FooColor background) {
		this.background = background;
	}

	public FooColor getBackground() {
		return background;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
