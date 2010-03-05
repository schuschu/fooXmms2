package org.dyndns.schuschu.xmms2client.view;

import org.eclipse.swt.SWT;

public enum FooStyle {
	READ_ONLY(SWT.READ_ONLY),
	BORDER(SWT.BORDER),
	MULTI(SWT.MULTI),
	V_SCROLL(SWT.V_SCROLL),
	FULL_SELECTION(SWT.FULL_SELECTION),
	BAR(SWT.BAR);
	
	private int code;

	private FooStyle(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
