/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.testlib;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A node list without order
 * @author Julien Férard
 */
public class UnsortedNodeList implements Iterable<Node>, Comparable<UnsortedNodeList> {
    /**
     * a comparator between two nodes
     */
    static final Comparator<Node> cmp = new Comparator<Node>() {
		@Override
		public int compare(final Node o1, final Node o2) {
			final int cmpType = o1.getNodeType() - o2.getNodeType();
			if (cmpType != 0)
				return cmpType;

			final int cmpName = o1.getNodeName().compareTo(o2.getNodeName());
			if (cmpName != 0)
				return cmpName;

			final NamedNodeMap attributes1 = o1.getAttributes();
			final NamedNodeMap attributes2 = o2.getAttributes();

            if (attributes1 == null)
				return attributes2 == null ? 0 : 1;
			if (attributes2 == null)
				return -1;

			final int cmpAttrs = AttrList.create(attributes1).compareTo(AttrList.create(attributes2));
			if (cmpAttrs != 0)
				return cmpAttrs;

			return 0; // o1.getTextContent().compareTo(o2.getTextContent());
		}
	};

	private final int length;
	private final List<Node> list;

    /**
     * @param nodes the wrapped node list
     */
    UnsortedNodeList(final NodeList nodes) {
		this.length = nodes.getLength();
		this.list = new ArrayList<Node>(this.length);
		for (int i = 0; i < this.length; i++) {
			this.list.add(nodes.item(i));
		}
		Collections.sort(this.list, cmp);
	}

	@Override
	public Iterator<Node> iterator() {
		return this.list.iterator();
	}

	@Override
	public int compareTo(final UnsortedNodeList other) {
		if (this.length != other.length)
			return this.length - other.length;

		final Iterator<Node> i1 = this.list.iterator();
		final Iterator<Node> i2 = other.list.iterator();

		while (i1.hasNext()) {
			final int c = cmp.compare(i1.next(), i2.next());
			if (c != 0)
				return c;
		}
		return 0;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UnsortedNodeList))
			return false;

		final UnsortedNodeList other = (UnsortedNodeList) o;
		return this.compareTo(other) == 0;
	}

	@Override
	public String toString() {
		return this.list.toString();
	}

	@Override
	public int hashCode() {
		return this.list.hashCode();
	}

    /**
     * @param n the node
     * @return the string representation of the node
     */
    public static String toString(final Node n) {
		if (n == null)
			return "[null]";

		final NamedNodeMap attributes = n.getAttributes();
		final String s = attributes == null ? "" : AttrList.create(attributes).toString();
		return "Node["+n.getNodeName()+", "+n.getNodeValue()+", "+ s +"]";
	}

    /**
     * @return the node list count
     */
    public int size() {
		return this.length;
	}
}
