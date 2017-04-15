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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.logging.Logger;

public class UnsortedChildrenTester extends ChildrenTester {
	static Logger logger = Logger.getLogger("DomTester");

	@Override
	public boolean childrenEquals(final Node element1, final Node element2) {
		final NodeList nodes1 = element1.getChildNodes();
		final NodeList nodes2 = element2.getChildNodes();
		final UnsortedNodeList list1 = new UnsortedNodeList(nodes1);
		final UnsortedNodeList list2 = new UnsortedNodeList(nodes2);

		if (list1.size() != list2.size())
			return false;

		final Iterator<Node> i1 = list1.iterator();
		final Iterator<Node> i2 = list2.iterator();

		while (i1.hasNext()) {
			if (!this.equals(i1.next(), i2.next()))
				return false;
		}

		return true;
	}
}
