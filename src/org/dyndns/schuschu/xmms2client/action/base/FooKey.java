package org.dyndns.schuschu.xmms2client.action.base;

import org.eclipse.swt.SWT;

public enum FooKey {
	//TODO: add all the other
	NONE(0), CR(SWT.CR), DEL(SWT.DEL), ESC(SWT.ESC);
	
	private int code;

	private FooKey(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
