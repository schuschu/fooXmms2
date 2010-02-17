package org.dyndns.schuschu.xmms2client.view.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class FooComboDialog {

	public static String show(Shell parent, String message, String text,
			String[] values, int style) {
		ComboDialog dialog = new ComboDialog(parent, style);
		return setup(dialog, message, text, values);
	}

	public static String show(Shell parent, String message, String text,
			String[] values) {
		ComboDialog dialog = new ComboDialog(parent);
		return setup(dialog, message, text, values);
	}

	private static String setup(ComboDialog dialog, String message,
			String text, String[] values) {
		dialog.setMessage(message);
		dialog.setText(text);
		dialog.setValues(values);

		return dialog.open();
	}

	private static class ComboDialog extends Dialog {
		private String message;
		private String input;
		private String[] values;

		public ComboDialog(Shell parent) {
			this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		}

		public ComboDialog(Shell parent, int style) {
			super(parent, style);
			setText("Input Dialog");
			setMessage("Please enter a value:");
			setValues(new String[] { "choose a value" });
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setValues(String[] values) {
			this.values = values;
		}

		public String open() {
			Shell shell = new Shell(getParent(), getStyle());
			shell.setText(getText());
			createContents(shell);
			shell.pack();

			int x = getParent().getLocation().x
					+ (getParent().getSize().x - shell.getSize().x) / 2;
			int y = getParent().getLocation().y
					+ (getParent().getSize().y - shell.getSize().y) / 2;
			shell.setLocation(x, y);

			shell.open();
			Display display = getParent().getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			return input;
		}

		private void createContents(final Shell shell) {
			shell.setLayout(new GridLayout(2, true));

			Label label = new Label(shell, SWT.NONE);
			label.setText(message);
			GridData data = new GridData();
			data.horizontalSpan = 2;
			label.setLayoutData(data);

			final Combo combo = new Combo(shell, SWT.READ_ONLY);
			data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			combo.setLayoutData(data);
			combo.setItems(values);

			Button ok = new Button(shell, SWT.PUSH);
			ok.setText("OK");
			data = new GridData(GridData.FILL_HORIZONTAL);
			ok.setLayoutData(data);
			ok.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					input = combo.getItem(combo.getSelectionIndex());
					shell.close();
				}
			});

			Button cancel = new Button(shell, SWT.PUSH);
			cancel.setText("Cancel");
			data = new GridData(GridData.FILL_HORIZONTAL);
			cancel.setLayoutData(data);
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					input = null;
					shell.close();
				}
			});

			shell.setDefaultButton(ok);
		}
	}

}
