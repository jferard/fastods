/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.testutil;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 */
public class UnsortedNodeList implements Iterable<Node>, Comparable<UnsortedNodeList> {
	static Comparator<Node> cmp = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			int cmpType = o1.getNodeType() - o2.getNodeType();
			if (cmpType != 0)
				return cmpType;

			int cmpName = o1.getNodeName().compareTo(o2.getNodeName());
			if (cmpName != 0)
				return cmpName;

			NamedNodeMap attributes1 = o1.getAttributes();
			NamedNodeMap attributes2 = o2.getAttributes();

			if (attributes1 == null)
				return attributes2 == null ? 0 : 1;
			if (attributes2 == null)
				return -1;

			int cmpAttrs = new AttrList(attributes1).compareTo(new AttrList(attributes2));
			if (cmpAttrs != 0)
				return cmpAttrs;

			return 0; // o1.getTextContent().compareTo(o2.getTextContent());
		}
	};

	private final int length;
	private final List<Node> list;

	UnsortedNodeList(final NodeList nodes) {
		length = nodes.getLength();
		list = new ArrayList<Node>(length);
		for (int i = 0; i < length; i++) {
			list.add(nodes.item(i));
		}
		Collections.sort(list, cmp);
	}

	@Override
	public Iterator<Node> iterator() {
		return list.iterator();
	}

	@Override
	public int compareTo(UnsortedNodeList other) {
		if (this.length != other.length)
			return this.length - other.length;

		Iterator<Node> i1 = this.list.iterator();
		Iterator<Node> i2 = other.list.iterator();

		while (i1.hasNext()) {
			int c = cmp.compare(i1.next(), i2.next());
			if (c != 0)
				return c;
		}
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UnsortedNodeList))
			return false;

		UnsortedNodeList other = (UnsortedNodeList) o;
		return this.compareTo(other) == 0;
	}

	@Override
	public String toString() {
		return list.toString();
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}

	public static String toString(Node n) {
		if (n == null)
			return "[null]";

		NamedNodeMap attributes = n.getAttributes();
		String s = attributes == null ? "" : new AttrList(attributes).toString();
		return "Node["+n.getNodeName()+", "+n.getNodeValue()+", "+ s +"]";
	}

	public int size() {
		return this.length;
	}
}
