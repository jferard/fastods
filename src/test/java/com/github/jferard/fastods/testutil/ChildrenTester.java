/* *****************************************************************************
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
 * ****************************************************************************/
package com.github.jferard.fastods.testutil;

import com.google.common.base.Objects;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public abstract class ChildrenTester {
	protected boolean attributesEquals(final Node element1, final Node element2) {
		final NamedNodeMap attributes1 = element1.getAttributes();
		final NamedNodeMap attributes2 = element2.getAttributes();
		if (attributes1 == null) {
			return attributes2 == null;
		} else {
			if (attributes2 == null)
				return false;
			if (attributes1.getLength() != attributes2.getLength())
				return false;

			List<Attr> list1 = new ArrayList<Attr>(attributes1.getLength());
			List<Attr> list2 = new ArrayList<Attr>(attributes1.getLength());
			for (int i = 0; i < attributes1.getLength(); i++) {
				list1.add((Attr) attributes1.item(i));
				list2.add((Attr) attributes2.item(i));
			}

			Comparator<Attr> cmp = new Comparator<Attr>() {
				@Override
				public int compare(Attr o1, Attr o2) {
					return 2 * o1.getName().compareTo(o2.getName()) + o1.getValue().compareTo(o2.getValue());
				}
			};

			Collections.sort(list1, cmp);
			Collections.sort(list2, cmp);

			Iterator<Attr> i1 = list1.iterator();
			Iterator<Attr> i2 = list2.iterator();
			while (i1.hasNext()) {
				if (cmp.compare(i1.next(), i2.next()) != 0)
					return false;
			}
			return true;
		}
	}

	private boolean namesEquals(final Node element1, final Node element2) {
		return element1.getNodeType() == element2.getNodeType()
				&& Objects.equal(element1.getNodeName(), element2.getNodeName())
				&& Objects.equal(element1.getNodeValue(),
				element2.getNodeValue())
				&& Objects.equal(element1.getNamespaceURI(),
				element2.getNamespaceURI());
	}

	protected boolean equals(final Node element1, final Node element2) {
		if (element1 == null)
			return element2 == null;
		else if (element2 == null)
			return false;
		else // element1 != null && element2 != null
			return this.namesEquals(element1, element2)
				&& this.attributesEquals(element1, element2)
				&& this.childrenEquals(element1, element2);
	}

	public abstract boolean childrenEquals(final Node element1, final Node element2);
}
