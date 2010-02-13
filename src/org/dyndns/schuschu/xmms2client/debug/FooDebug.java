package org.dyndns.schuschu.xmms2client.debug;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class FooDebug extends OutputStream {

	private static final int defForeground = SWT.COLOR_BLACK;
	private static final int defBackground = SWT.COLOR_WHITE;

	private static int foreground;
	private static int background;

	private Shell sShell;
	private Display display;
	private Table table;

	private StringBuffer sb;

	public FooDebug() {

		this.display = Display.getDefault();
		sb = new StringBuffer();

		setForeground(defForeground);
		setBackground(defBackground);

		createSShell();
	}

	private void writeline() {
		if (sShell.isDisposed()) {
			createSShell();
		}
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(sb.toString());
		item.setBackground(display.getSystemColor(getBackground()));
		item.setForeground(display.getSystemColor(getForeground()));
		setForeground(defForeground);
		setBackground(defBackground);
		sb = new StringBuffer();
		table.getColumn(0).pack();
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

		sShell.setLayout(new FillLayout());

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

		table = new Table(sShell, SWT.NONE);
		new TableColumn(table, SWT.NONE);

		sShell.open();
	}

	public static void setForeground(int foreground) {
		FooDebug.foreground = foreground;
	}

	public int getForeground() {
		return foreground;
	}

	public static void setBackground(int background) {
		FooDebug.background = background;
	}

	public int getBackground() {
		return background;
	}
}
