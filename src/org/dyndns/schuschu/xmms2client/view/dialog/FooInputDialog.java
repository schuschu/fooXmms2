package org.dyndns.schuschu.xmms2client.view.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FooInputDialog {
	public static String show(Shell parent, String message, String text,
			int style) {
		InputDialog dialog = new InputDialog(parent, style);
		return setup(dialog, message, text);
	}

	public static String show(Shell parent, String message, String text) {
		InputDialog dialog = new InputDialog(parent);
		return setup(dialog, message, text);
	}

	private static String setup(InputDialog dialog, String message, String text) {
		dialog.setMessage(message);
		dialog.setText(text);

		return dialog.open();
	}
}

class InputDialog extends Dialog {
	private String message;
	private String input;

	public InputDialog(Shell parent) {
		this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	public InputDialog(Shell parent, int style) {
		super(parent, style);
		setText("Input Dialog");
		setMessage("Please enter a value:");
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
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

		final Text text = new Text(shell, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		text.setLayoutData(data);

		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		data = new GridData(GridData.FILL_HORIZONTAL);
		ok.setLayoutData(data);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				input = text.getText();
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