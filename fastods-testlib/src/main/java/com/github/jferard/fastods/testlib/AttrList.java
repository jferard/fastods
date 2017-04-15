/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 */
public class AttrList implements Iterable<Attr>, Comparable<AttrList> {
	/**
	 * The attributes comparator, on name then on value.
	 */
	static Comparator<Attr> cmp = new Comparator<Attr>() {
		@Override
		public int compare(final Attr o1, final Attr o2) {
			if (o1 == null)
				return o2 == null ? 0 : 1;
			if (o2 == null)
				return -1;

			final int cmpNames = o1.getName().compareTo(o2.getName());
			if (cmpNames != 0)
				return cmpNames;

			return o1.getValue().compareTo(o2.getValue());
		}
	};

	private final int length;
	private final ArrayList<Attr> attrs;
	private NamedNodeMap attributes;

	/**
	 * @param attributes the attributes
	 */
	AttrList(final NamedNodeMap attributes) {
		this.length = attributes.getLength();
		this.attrs = new ArrayList<Attr>(this.length);
		for (int i = 0; i < this.length; i++) {
			this.attrs.add((Attr) attributes.item(i));
		}
		Collections.sort(this.attrs, cmp);
	}

	public int size() {
		return this.length;
	}

	@Override
	public Iterator<Attr> iterator() {
		return this.attrs.iterator();
	}

	@Override
	public String toString() {
		return this.attrs.toString();
	}

	@Override
	public int hashCode() {
		return this.attrs.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AttrList))
			return false;

		final AttrList other = (AttrList) o;
		return this.compareTo(other) == 0;
	}

	@Override
	public int compareTo(final AttrList other) {
		if (this.length != other.length)
			return this.length - other.length;

		final Iterator<Attr> i1 = this.attrs.iterator();
		final Iterator<Attr> i2 = other.attrs.iterator();

		while (i1.hasNext()) {
			final int c = cmp.compare(i1.next(), i2.next());
			if (c != 0)
				return c;
		}
		return 0;
	}
}

