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
package com.github.jferard.fastods.testutil;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 */
public class SortedChildrenTester extends ChildrenTester {
	@Override
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

		for (int i = 0; i < l1; i++) {
			final Node c1 = nodes1.item(i);
			final Node c2 = nodes2.item(i);
			if (!this.equals(c1, c2)) {
				UnsortedChildrenTester.logger.info("Different children " + c1 + " vs " + c2);
				return false;
			}
		}

		return true;
	}
}
