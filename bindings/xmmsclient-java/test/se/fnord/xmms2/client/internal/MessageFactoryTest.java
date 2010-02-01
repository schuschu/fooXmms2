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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import se.fnord.xmms2.client.Error;


public class MessageFactoryTest {
	@Test
	public void testCreateOutgoing() {
		Message m = MessageFactory.outgoing(IpcObject.CONFIG, IpcCommand.PLUGIN_LIST, 0, "A", 2);
		assertNotNull(m);
		assertTrue(m instanceof SendMessage);
		assertEquals(IpcObject.CONFIG, m.getObject());
		assertEquals(IpcCommand.PLUGIN_LIST, m.getCommand());
		assertEquals(0, m.getCookie());
		assertEquals(2, m.getData().length);
		assertEquals("A", m.getData()[0]);
		assertEquals(2, m.getData()[1]);
	}

	@Test
	public void testCreateIncomingNormal() {
		Message m = MessageFactory.incoming(IpcObject.CONFIG, IpcCommand.REPLY, 0, "A", 2);
		assertNotNull(m);
		assertTrue(m instanceof ReplyMessage);
		assertEquals(IpcObject.CONFIG, m.getObject());
		assertEquals(IpcCommand.REPLY, m.getCommand());
		assertEquals(0, m.getCookie());
		assertEquals(2, m.getData().length);
		assertEquals("A", m.getData()[0]);
		assertEquals(2, m.getData()[1]);
	}

	@Test
	public void testCreateIncomingError() {
		Message m = MessageFactory.incoming(IpcObject.CONFIG, IpcCommand.ERROR, 0, "A");
		
		assertNotNull(m);
		assertTrue(m instanceof ErrorMessage);
		assertEquals(IpcCommand.ERROR, m.getCommand());
		assertEquals(1, m.getData().length);
		assertEquals("A", ((Error)m.getData()[0]).getMessage());
	}

	@Test
	public void testCreateIncomingInvalidNormal() {
		boolean exception = false;
		try {
			MessageFactory.incoming(IpcObject.CONFIG, IpcCommand.PLUGIN_LIST, 0, "A");
		}
		catch(IllegalArgumentException e) {
			exception = true;
		}
		
		assertTrue(exception);
	}

	@Test
	public void testCreateIncomingInvalidError() {
		boolean exception;

		exception = false;
		try {
			MessageFactory.incoming(IpcObject.CONFIG, IpcCommand.ERROR, 0, "A", "B");
		}
		catch(IllegalArgumentException e) {
			exception = true;
		}
		
		assertTrue(exception);

		exception = false;
		try {
			MessageFactory.incoming(IpcObject.CONFIG, IpcCommand.ERROR, 0, 0);
		}
		catch(IllegalArgumentException e) {
			exception = true;
		}
		
		assertTrue(exception);
	}
	
}
