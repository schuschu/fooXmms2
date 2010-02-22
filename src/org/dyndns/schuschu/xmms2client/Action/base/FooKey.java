package org.dyndns.schuschu.xmms2client.Action.base;

import org.eclipse.swt.SWT;

public enum FooKey {
	//TODO: add all the other
	CR(SWT.CR), DEL(SWT.DEL), ESC(SWT.ESC);
	
	private int code;

	private FooKey(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
