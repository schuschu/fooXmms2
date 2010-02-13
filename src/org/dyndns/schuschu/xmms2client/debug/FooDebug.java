package org.dyndns.schuschu.xmms2client.debug;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class FooDebug extends OutputStream implements Runnable {

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

	public FooDebug() {

		this.display = Display.getDefault();
		buffer = new Vector<BufferEntry>();
		sb = new StringBuffer();

		setForeground(defForeground);
		setBackground(defBackground);

		createSShell();

		Thread self = new Thread(this);
		self.start();

	}

	public void done() {
		run = false;
	}

	private void writeline() {
		BufferEntry entry = new BufferEntry();
		entry.setBackground(background);
		entry.setForeground(foreground);
		entry.setText(sb.toString());
		sb = new StringBuffer();
		buffer.add(entry);
		setForeground(defForeground);
		setBackground(defBackground);
	}

	@Override
	public void run() {
		while (run) {
			if (buffer.size() != 0) {
				if (!display.isDisposed()) {
					display.syncExec(new Runnable() {
						public void run() {
							if (sShell.isDisposed()) {
								createSShell();
							}
							TableItem item = new TableItem(table, SWT.NONE);
							item.setText(buffer.get(0).getText());
							item.setBackground(display.getSystemColor(buffer
									.get(0).getBackground()));
							item.setForeground(display.getSystemColor(buffer
									.get(0).getForeground()));
							buffer.remove(0);
							table.getColumn(0).pack();
							if (!buttonScroll.getSelection()) {
								table.showItem(item);
							}
						}
					});
				} else {
					this.done();
				}
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
