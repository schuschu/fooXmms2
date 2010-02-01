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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class CollectionBuilder {

	public static final class CollectionImpl implements CollectionExpression {
		private final CollectionType type;
		private final List<CollectionExpression> ops;
		private final List<Integer> idlist;
		private final Map<String, String> attrs;

		public CollectionImpl(CollectionType type, List<CollectionExpression> ops, List<Integer> idlist, Map<String, String> attrs) {
			this.type = type;
			this.ops = Collections.unmodifiableList(new ArrayList<CollectionExpression>(ops));
			this.idlist = Collections.unmodifiableList(new ArrayList<Integer>(idlist));
			this.attrs = Collections.unmodifiableMap(new HashMap<String, String>(attrs));
		}

		public Map<String, String> getAttributes() {
			return attrs;
		}

		public List<Integer> getIdList() {
			return idlist;
		}

		public List<CollectionExpression> getOps() {
			return ops;
		}

		public CollectionType getType() {
			return type;
		}
		
		@Override
		public int hashCode() {
			assert false;
			return 42;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			
			if (!(obj instanceof CollectionExpression))
				return false;
			
			CollectionExpression rhs = (CollectionExpression) obj;
			
			if (!type.equals(rhs.getType()))
				return false;
			if (!ops.equals(rhs.getOps()))
				return false;
			if (!idlist.equals(rhs.getIdList()))
				return false;
			if (!attrs.equals(rhs.getAttributes()))
				return false;
			
			return true;
		}
		
		@Override
		public String toString() {
			final Map<String, String> m = getAttributes();
			final List<Integer> idlist = getIdList();
			final List<CollectionExpression> ops = getOps();

			final StringBuilder b = new StringBuilder();

			b.append(getType());
			b.append(' ');

			b.append('{');
			for (final Map.Entry<String, String> entry : m.entrySet()) {
				b.append(" \"");
				b.append(entry.getKey());
				b.append("\"=\"");
				b.append(entry.getValue());
				b.append('"');
			}
			b.append(" }");


			b.append(" ids [");
			for (final int id : idlist) {
				b.append(' ');
				b.append(id);
			}
			b.append(" ]");

			b.append(" ops [");
			for (final CollectionExpression op : ops) {
				b.append(' ');
				b.append(op.toString());
			}
			b.append(" ]");
			return b.toString();
		}

	}
	private static final List<CollectionExpression> NO_OPS = Collections.emptyList();
	private static final List<Integer> NO_IDS = Collections.emptyList();
	private static final Map<String, String> ALL_MEDIA_ATTRS = Collections.singletonMap("reference", CollectionExpression.ALL_MEDIA);

	private static final Map<String, String> NO_ATTRS = Collections.emptyMap();
	private static final CollectionExpression ALL_MEDIA_REFERENCE = new CollectionImpl(CollectionType.REFERENCE, NO_OPS, NO_IDS, ALL_MEDIA_ATTRS);
	private static final CollectionExpression EMPTY_EXPRESSION = new CollectionImpl(CollectionType.IDLIST, NO_OPS, NO_IDS, NO_ATTRS);
	
	public static CollectionExpression createReference(CollectionNamespace namespace, String collection) {
		final Map<String, String> ref = new HashMap<String, String>();
		ref.put("namespace", namespace.toString());
		ref.put("reference", collection);
		return new CollectionImpl(CollectionType.REFERENCE, NO_OPS, NO_IDS, ref);
	}

	public static CollectionExpression createReference(String collection) {
		return new CollectionImpl(CollectionType.REFERENCE, NO_OPS, NO_IDS, Collections.singletonMap("reference", collection));
	}
	public static CollectionExpression getAllMediaReference() {
		return ALL_MEDIA_REFERENCE;
	}

	public static CollectionExpression getEmptyExpression() {
		return EMPTY_EXPRESSION;
	}

	private CollectionType type = CollectionType.IDLIST;

	private final List<CollectionExpression> oplist = new LinkedList<CollectionExpression>();

	private final List<Integer> idlist = new LinkedList<Integer>();

	private final Map<String, String> attrs = new HashMap<String, String>();

	public CollectionBuilder addAttribute(String attribute, String value) {
		attrs.put(attribute, value);
		return this;
	}

	public CollectionBuilder addAttributes(Map<String, String> attributes) {
		attrs.putAll(attributes);
		return this;
	}

	public CollectionBuilder addId(int id) {
		idlist.add(id);
		return this;
	}

	public CollectionBuilder addIds(Collection<Integer> ids) {
		idlist.addAll(ids);
		return this;
	}

	public CollectionBuilder addOp(CollectionExpression c) {
		oplist.add(c);
		return this;
	}

	public CollectionBuilder addOps(Collection<CollectionExpression> ops) {
		oplist.addAll(ops);
		return this;
	}

	public CollectionBuilder setType(CollectionType type) {
		this.type = type;
		return this;
	}

	public CollectionExpression build() {
		return new CollectionImpl(type, oplist, idlist, attrs);
	}

	public void clear() {
		type = CollectionType.IDLIST;
		oplist.clear();
		idlist.clear();
		attrs.clear();
	}

}
