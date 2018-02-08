/*
 * FastODS - a Martin Schulz's SimpleODS fork
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

import com.google.common.base.Objects;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.logging.Logger;

public abstract class ChildrenTester {
	static Logger logger = Logger.getLogger("ChildrenTester");

	protected boolean attributesEquals(final Node element1, final Node element2) {
		final NamedNodeMap attributes1 = element1.getAttributes();
		final NamedNodeMap attributes2 = element2.getAttributes();
		if (attributes1 == null) {
			return attributes2 == null;
		} else if (attributes2 == null)
			return false;
		else {
			if (attributes1.getLength() != attributes2.getLength())
				return false;

			final AttrList list1 = new AttrList(attributes1);
			final AttrList list2 = new AttrList(attributes2);
			return list1.equals(list2);
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
		logger.fine("element1" + UnsortedNodeList.toString(element1) + ",\nelement2" + UnsortedNodeList.toString(element2));
		if (element1 == null)
			return element2 == null;
		else if (element2 == null)
			return false;
		else { // element1 != null && element2 != null
			logger.fine("" + this.namesEquals(element1, element2)
					+ this.attributesEquals(element1, element2)
					+ this.childrenEquals(element1, element2));
			return this.namesEquals(element1, element2)
					&& this.attributesEquals(element1, element2)
					&& this.childrenEquals(element1, element2);
		}
	}

	public abstract boolean childrenEquals(final Node element1, final Node element2);
}
