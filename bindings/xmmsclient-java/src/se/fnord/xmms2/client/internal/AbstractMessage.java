/* Copyright (c) 2009 Henrik Gustafsson <henrik.gustafsson@fnord.se>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package se.fnord.xmms2.client.internal;

import java.util.Arrays;


abstract class AbstractMessage implements Message {
	static final int HEADER_LENGTH = 16;
	protected final IpcObject object;
	protected final IpcCommand command;
	protected final int cookie;

	protected final Object[] params;

	protected AbstractMessage(IpcObject object, IpcCommand cmd, int cookie, Object ... params) {
		this.object = object;
		this.command = cmd;
		this.cookie = cookie;
		this.params = params;
	}

	public final IpcCommand getCommand() {
		return command;
	}

	public final int getCookie() {
		return cookie;
	}

	public final Object[] getData() {
		return params;
	}

	public final IpcObject getObject() {
		return object;
	}

	@Override
	public String toString() {
		return "[" + object + "/" + command + " c:" + cookie + "]";
	}

	private boolean hashed = false;
	private int hash = 0;
	
	@Override
	public int hashCode() {
		if (!hashed) {
			hash = object.hashCode() ^ command.hashCode() ^ cookie ^ Arrays.deepHashCode(params); 
			hashed = true;
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			final AbstractMessage other = (AbstractMessage) obj;
		
			return (this == obj) || 
				(object == other.getObject() && command == other.getCommand() &&
				 cookie == other.getCookie() && Arrays.deepEquals(params, other.getData()));
		}
		catch (ClassCastException e) {
			return false;
		}
	}
}
