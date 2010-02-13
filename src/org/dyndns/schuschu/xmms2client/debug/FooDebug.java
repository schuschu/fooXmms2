package org.dyndns.schuschu.xmms2client.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class FooDebug extends OutputStream {

	// TODO: replace with fooview elements?

	private static final int defForeground = SWT.COLOR_BLACK;
	private static final int defBackground = SWT.COLOR_WHITE;

	private static int foreground;
	private static int background;

	private boolean run = true;

	private Shell sShell;
	private Display display;
	private Table table;
	private Composite buttonArea;

	private Button buttonScroll;

	private StringBuffer sb;

	private Vector<BufferEntry> buffer;

	private Thread updater;

	public FooDebug() {

		this.display = Display.getDefault();
		buffer = new Vector<BufferEntry>();
		sb = new StringBuffer();

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
												.getBackground()));
								item.setForeground(display
										.getSystemColor(buffer.get(0)
												.getForeground()));
								buffer.remove(0);
								if (!buttonScroll.getSelection()) {
									table.showItem(item);
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
		notify();
	}

	private synchronized void waiter() {
		while (buffer.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void write(int b) throws IOException {
		if (sShell.isDisposed()) {
			createSShell();
		}

		if ((char) b != '\n') {
			sb.append((char) b);
		} else {
			writeline();
		}
	}

	private void createSShell() {
		sShell = new Shell(display);
		sShell.setText("debug console");
		sShell.setSize(new Point(400, 400));

		FormLayout layout = new FormLayout();
		sShell.setLayout(layout);

		Image image = null;

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

		sShell.setImage(image);

		createButtonArea();
		createTable();

		sShell.open();
	}

	public void createTable() {
		table = new Table(sShell, SWT.NONE);

		FormData tableData = new FormData();
		tableData.top = new FormAttachment(buttonArea, 0);
		tableData.left = new FormAttachment(0, 0);
		tableData.right = new FormAttachment(100, 0);
		tableData.bottom = new FormAttachment(100, 0);
		table.setLayoutData(tableData);

		new TableColumn(table, SWT.NONE);
		table.getColumn(0).pack();
	}

	public void createButtonArea() {
		buttonArea = new Composite(sShell, SWT.NONE);
		buttonArea.setLayout(new FillLayout());

		FormData buttonData = new FormData();
		buttonData.top = new FormAttachment(0, 0);
		buttonData.left = new FormAttachment(0, 0);
		buttonData.right = new FormAttachment(100, 0);
		buttonArea.setLayoutData(buttonData);

		buttonScroll = new Button(buttonArea, SWT.TOGGLE);
		buttonScroll.setText("Scroll-lock");

		Button buttonClear = new Button(buttonArea, SWT.NONE);
		buttonClear.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				table.clearAll();
				table.setItemCount(0);

			}
		});
		buttonClear.setText("Clear");

		Button buttonSave = new Button(buttonArea, SWT.NONE);
		buttonSave.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				FileDialog fd = new FileDialog(sShell, SWT.SAVE);
				fd.setText("Save logfile");
				fd.setFileName("fooXmms2.log");
				String selected = fd.open();

				if (selected != null) {
					try {
						File file = new File(selected);

						boolean write = true;

						if (!file.createNewFile()) {
							MessageBox mb = new MessageBox(sShell,
									SWT.ICON_WARNING | SWT.YES | SWT.NO);

							mb.setText("Replace file");
							mb
									.setMessage(file.getName()
											+ " already exists. Do you want to replace it?");

							write = mb.open() == SWT.YES;
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
								MessageBox mb = new MessageBox(sShell,
										SWT.ICON_ERROR | SWT.OK);
								mb.setText("Error");
								mb.setMessage("Can not write to file!");
								mb.open();
							}
						}
					} catch (IOException e) {
						MessageBox mb = new MessageBox(sShell,
								SWT.ICON_ERROR | SWT.OK);
						mb.setText("Error");
						mb.setMessage(e.getMessage());
						mb.open();
					}
				}

			}
		});
		buttonSave.setText("Save");
	}

	public static void setForeground(int foreground) {
		FooDebug.foreground = foreground;
	}

	public static void setBackground(int background) {
		FooDebug.background = background;
	}

}

class BufferEntry {
	private int foreground;
	private int background;
	private String text;

	public void setForeground(int foreground) {
		this.foreground = foreground;
	}

	public int getForeground() {
		return foreground;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public int getBackground() {
		return background;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
