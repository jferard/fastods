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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class UnsortedChildrenTester extends ChildrenTester {
	static Logger logger = Logger.getLogger("DomTester");

	public boolean childrenEquals(final Node element1, final Node element2) {
		final NodeList nodes1 = element1.getChildNodes();
		final NodeList nodes2 = element2.getChildNodes();
		final int l1 = nodes1.getLength();
		final int l2 = nodes2.getLength();
		if (l1 != l2) {
			UnsortedChildrenTester.logger.info("Different children number " + element1 + "->"
					+ nodes1 + " vs " + element2 + "->" + nodes2);
			return false;
		}

		List<Node> list1 = new ArrayList<Node>(l1);
		List<Node> list2 = new ArrayList<Node>(l1);

		for (int i = 0; i < l1; i++) {
			list1.add(nodes1.item(i));
			list2.add(nodes2.item(i));
		}

		Comparator<Node> cmp = new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return (int) (4*Math.signum(o1.getNodeType() - o2.getNodeType()) + 2*o1.getNodeName().compareTo(o2.getNodeName()));
			}
		};

		Collections.sort(list1, cmp);
		Collections.sort(list2, cmp);

		Iterator<Node> i1 = list1.iterator();
		Iterator<Node> i2 = list2.iterator();
		while (i1.hasNext()) {
			final Node c1 = i1.next();
			final Node c2 = i2.next();
			if (!this.equals(c1, c2)) {
				UnsortedChildrenTester.logger.info("Different children " + c1 + " vs " + c2);
				return false;
			}
		}
		return true;
	}
}
