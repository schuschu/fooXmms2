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

package se.fnord.xmms2.client.types;

import java.util.List;



public class InfoQuery implements Query {
	private final CollectionExpression coll;
	private final int start;
	private final int length;
	private final List<String> ordering;
	private final List<String> fields;
	private final List<String> grouping;

	public InfoQuery(CollectionExpression coll, int limit_start, int limit_length, List<String> ordering, List<String> fields, List<String> grouping) {
		this.coll = coll;
		this.start = limit_start;
		this.length = limit_length;
		
		this.ordering = ordering;
		this.fields = fields;
		this.grouping = grouping;
	}
	/* (non-Javadoc)
	 * @see se.fnord.xmms2.types.Query#getCollection()
	 */
	public CollectionExpression getCollection() {
		return coll;
	}

	public List<String> getFields() {
		return fields;
	}

	public List<String> getGrouping() {
		return grouping;
	}

	/* (non-Javadoc)
	 * @see se.fnord.xmms2.types.Query#getLength()
	 */
	public int getLength() {
		return length;
	}

	/* (non-Javadoc)
	 * @see se.fnord.xmms2.types.Query#getOrdering()
	 */
	public List<String> getOrdering() {
		return ordering;
	}

	/* (non-Javadoc)
	 * @see se.fnord.xmms2.types.Query#getStart()
	 */
	public int getStart() {
		return start;
	}
	@Override
	public boolean equals(Object obj) {
		try {
			InfoQuery other = (InfoQuery) obj;
			return (this == other) ||
				(start == other.getStart() && length == other.getLength() &&
				 ordering.equals(other.getOrdering()) && fields.equals(other.getFields()) &&
				 grouping.equals(other.getGrouping()) && coll.equals(other.getCollection()));
		}
		catch (ClassCastException e) {}
		return false;
	}
}
