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

package se.fnord.xmms2.types;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import se.fnord.xmms2.client.types.CollectionBuilder;
import se.fnord.xmms2.client.types.CollectionExpression;
import se.fnord.xmms2.client.types.CollectionType;


public class CollectionBuilderTest {
	@Test
	public void testEquals() {
		CollectionExpression allMedia = CollectionBuilder.getAllMediaReference();
		CollectionExpression allMedia2 = new CollectionBuilder().setType(CollectionType.REFERENCE).addAttribute("reference", CollectionExpression.ALL_MEDIA).build();
		CollectionExpression someMedia = CollectionBuilder.createReference("Some Media");
		CollectionExpression emptyExpression = CollectionBuilder.getEmptyExpression();
		
		assertFalse(allMedia.equals(null));
		assertTrue(allMedia.equals(allMedia));
		assertTrue(allMedia.equals(allMedia2));
		assertTrue(allMedia2.equals(allMedia));
		
		assertFalse(someMedia.equals(allMedia));
		assertFalse(emptyExpression.equals(allMedia));
		
		
	}
}
