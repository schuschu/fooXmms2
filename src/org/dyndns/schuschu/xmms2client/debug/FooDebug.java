package org.dyndns.schuschu.xmms2client.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.dyndns.schuschu.xmms2client.view.dialog.FooConfirmationDialog;
import org.dyndns.schuschu.xmms2client.view.dialog.FooInputDialog;
import org.dyndns.schuschu.xmms2client.view.dialog.FooMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class FooDebug extends OutputStream {

	// TODO: replace with fooview elements?

	private static final FooColor defForeground = FooColor.BLACK;
	private static final FooColor defBackground = FooColor.WHITE;

	private static FooColor foreground;
	private static FooColor background;

	private boolean run = true;
	private boolean pause = false;

	private Shell sShell;
	private Display display;
	private Table table;
	private Image image;
	private Menu menuBar, fileMenu, editMenu;
	private MenuItem fileMenuHeader, editMenuHeader;
	private MenuItem fileExitItem, fileSaveItem, editAutoScrollItem,
			editScrollbackItem, editClearItem;

	private StringBuffer sb;

	private Vector<BufferEntry> buffer;
	private int limit;

	private Thread updater;

	public FooDebug() {

		this.display = Display.getDefault();
		buffer = new Vector<BufferEntry>();
		sb = new StringBuffer();
		limit = 1000;

		setForeground(defForeground);
		setBackground(defBackground);

		createSShell();

		Runnable runable = new Runnable() {

			@Override
			public void run() {
				while (run) {
					waiter();
					if (!display.isDisposed()) {
						display.syncExec(new Runnable() {
							public void run() {
								if (sShell.isDisposed()) {
									createSShell();
								}

								TableItem item = new TableItem(table, SWT.NONE);
								item.setText(buffer.get(0).getText());
								item.setBackground(display
										.getSystemColor(buffer.get(0)
												.getBackground().getCode()));
								item.setForeground(display
										.getSystemColor(buffer.get(0)
												.getForeground().getCode()));
								buffer.remove(0);
								if (editAutoScrollItem.getSelection()) {
									table.showItem(item);
								}
								if (limit !=0 && table.getItemCount() > limit) {
									table.remove(0);
								}
							}
						});
					}
				}
			}
		};

		updater = new Thread(runable);
		updater.start();

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

	private synchronized void refresh() {
		notify();
	}

	@Override
	public void write(int b) throws IOException {
		if (sShell.isDisposed()) {
			createSShell();
		}

		if ((char) b != '\n' && (char) b != '\r') {
			sb.append((char) b);
		} else {
			if ((char) b == '\n') {
				writeline();
			}
		}
	}

	private void createSShell() {
		sShell = new Shell(display);
		sShell.setText("debug console");
		sShell.setSize(new Point(400, 400));

		sShell.setLayout(new FillLayout());

		createImage();
		sShell.setImage(image);

		createMenuBar();
		sShell.setMenuBar(menuBar);

		createTable();

		sShell.open();
	}

	public void createImage() {
		InputStream stream = this.getClass().getResourceAsStream(
				"/pixmaps/xmms2-128.png");
		if (stream != null) {
			try {
				image = new Image(display, stream);
			} catch (IllegalArgumentException e) {
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		} else {
			// TODO: find better way to do this
			image = new Image(display, "pixmaps/xmms2-128.png");
		}
	}

	public void createTable() {
		table = new Table(sShell, SWT.FULL_SELECTION);

		// TODO: Windows: width of list

		// use list mode
		// new TableColumn(table, SWT.NONE);
		// table.getColumn(0).pack();
	}

	public void createMenuBar() {
		menuBar = new Menu(sShell, SWT.BAR);

		createFileMenu();
		createEditMenu();

	}

	public void createFileMenu() {
		fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&File");

		fileMenu = new Menu(sShell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);

		fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
		fileSaveItem.setText("&Save");
		fileSaveItem.addListener(SWT.Selection, createSaveListener());

		fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("E&xit");
		fileExitItem.addListener(SWT.Selection, createExitListener());
	}

	public void createEditMenu() {
		editMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		editMenuHeader.setText("&Edit");

		editMenu = new Menu(sShell, SWT.DROP_DOWN);
		editMenuHeader.setMenu(editMenu);

		editAutoScrollItem = new MenuItem(editMenu, SWT.CHECK);
		editAutoScrollItem.setText("&Autoscroll");
		editAutoScrollItem.setSelection(true);

		editScrollbackItem = new MenuItem(editMenu, SWT.PUSH);
		editScrollbackItem.setText("&Scrollback");
		editScrollbackItem.setSelection(true);
		editScrollbackItem.addListener(SWT.Selection,
				createScrollbackListener());

		editClearItem = new MenuItem(editMenu, SWT.PUSH);
		editClearItem.setText("&Clear");
		editClearItem.addListener(SWT.Selection, createClearListener());
	}

	public Listener createExitListener() {
		return new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				sShell.close();
			}
		};
	}

	public Listener createScrollbackListener() {
		return new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				String temp = FooInputDialog.show(sShell,
						"Please enter new scrollback limit (0 to disable)", "scrollback",
						limit + "");
				if (temp != null) {
					try {
						int i = Integer.parseInt(temp);
						limit = i;
						if (limit != 0) {
							while (table.getItemCount() > limit) {
								table.remove(0);
							}
						}
					} catch (NumberFormatException e) {
						FooMessageDialog.show(sShell,
								"Please enter a valid number", "Error");
					}
				}
			}
		};
	}

	public Listener createClearListener() {
		return new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				table.clearAll();
				table.setItemCount(0);

			}
		};
	}

	public Listener createSaveListener() {
		return new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				pause = true;
				FileDialog fd = new FileDialog(sShell, SWT.SAVE);
				fd.setText("Save logfile");
				fd.setFileName("fooXmms2.log");
				String selected = fd.open();

				if (selected != null) {
					try {
						File file = new File(selected);

						boolean write = true;

						if (!file.createNewFile()) {
							write = SWT.YES == FooConfirmationDialog
									.show(
											sShell,
											file.getName()
													+ " already exists. Do you want to replace it?",
											"Replace file");
						}

						if (write) {
							if (file.canWrite()) {
								FileWriter fstream = new FileWriter(file);
								BufferedWriter out = new BufferedWriter(fstream);
								for (TableItem item : table.getItems()) {
									out.write(item.getText() + "\n");
								}
								out.close();
							} else {
								FooMessageDialog.show(sShell,
										"Can not write to file!", "Error");
							}
						}
					} catch (IOException e) {
						FooMessageDialog.show(sShell, e.getMessage(), "Error");
					}
				}
				pause = false;
				refresh();
			}
		};
	}

	public static void setForeground(FooColor foreground) {
		FooDebug.foreground = foreground;
	}

	public static void setBackground(FooColor background) {
		FooDebug.background = background;
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
