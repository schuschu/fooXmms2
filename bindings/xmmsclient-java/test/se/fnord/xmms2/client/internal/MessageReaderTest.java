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

import java.io.IOException;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.DictBuilder;


public class MessageReaderTest {
  @Test
  public void testRead() throws IOException {
	  Pipe p = Pipe.open();
	  p.source().configureBlocking(false);
	  
	  List<Integer> l = new ArrayList<Integer>();
	  l.add(0);
	  
	  Message m = MessageFactory.incoming(IpcObject.PLAYLIST, IpcCommand.REPLY, 23, 
			  "A",
			  CollectionBuilder.getEmptyExpression(),
			  Arrays.asList("B"),
			  new byte[] {0},
			  new DictBuilder().add("A", "B").build(),
			  l
	  );
	  Message m2;
	  MessageWriter writer = new MessageWriter(p.sink());
	  writer.add(m);
	  writer.write();

	  MessageReader reader = new MessageReader(p.source());
	  reader.read();
	  m2 = reader.poll();
	  
	  assertEquals(m, m2);
	  
  }
}
