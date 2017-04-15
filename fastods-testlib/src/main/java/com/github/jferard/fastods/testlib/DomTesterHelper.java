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

/*
public class DomTesterHelper {
	static Logger logger = Logger.getLogger("DomTester");

	private final DocumentBuilder builder;

	private DomTesterHelper() throws ParserConfigurationException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		this.builder = factory.newDocumentBuilder();
	}


	private boolean childrenEquals(final Node element1, final Node element2) {
		final NodeList nodes1 = element1.getChildNodes();
		final NodeList nodes2 = element2.getChildNodes();
		final int l1 = nodes1.getLength();
		final int l2 = nodes2.getLength();
		if (l1 != l2) {
			DomTesterHelper.logger.info("Different children number " + element1 + "->"
					+ nodes1 + " vs " + element2 + "->" + nodes2);
			return false;
		}

		for (int i = 0; i < l1; i++) {
			final Node c1 = nodes1.item(i);
			final Node c2 = nodes2.item(i);
			if (!this.equals(c1, c2)) {
				DomTesterHelper.logger.info("Different children " + c1 + " vs " + c2);
				return false;
			}
		}

		return true;
	}

	private boolean unorderedChildrenEquals(final Node element1, final Node element2) {
		final NodeList nodes1 = element1.getChildNodes();
		final NodeList nodes2 = element2.getChildNodes();
		final int l1 = nodes1.getLength();
		final int l2 = nodes2.getLength();
		if (l1 != l2) {
			DomTesterHelper.logger.info("Different children number " + element1 + "->"
					+ nodes1 + " vs " + element2 + "->" + nodes2);
			return false;
		}

		SortedSet<Node> set1 = new TreeSet<Node>();
		SortedSet<Node> set2 = new TreeSet<Node>();

		for (int i = 0; i < l1; i++) {
			set1.add(nodes1.item(i));
			set2.add(nodes2.item(i));
		}

		Iterator<Node> i1 = set1.iterator();
		Iterator<Node> i2 = set2.iterator();
		while (i1.hasNext()) {
			final Node c1 = i1.next();
			final Node c2 = i2.next();
			if (!this.unorderedChildrenEquals(c1, c2)) {
				DomTesterHelper.logger.info("Different children " + c1 + " vs " + c2);
				return false;
			}
		}
		return true;
	}

	private boolean equals(final Node element1, final Node element2) {
		return this.namesEquals(element1, element2)
				&& this.attributesEquals(element1, element2)
				&& this.childrenEquals(element1, element2);
	}

	private boolean namesEquals(final Node element1, final Node element2) {
		return element1.getNodeType() == element2.getNodeType()
				&& Objects.equal(element1.getNodeName(), element2.getNodeName())
				&& Objects.equal(element1.getNodeValue(),
						element2.getNodeValue())
				&& Objects.equal(element1.getNamespaceURI(),
						element2.getNamespaceURI());
	}

	private boolean stringEquals(final String s1, final String s2)
			throws SAXException, IOException {
		final Document document1 = this.builder.parse(
				new ByteArrayInputStream(("<r>" + s1 + "</r>").getBytes()));
		final Document document2 = this.builder.parse(
				new ByteArrayInputStream(("<r>" + s2 + "</r>").getBytes()));
		return this.equals(document1.getDocumentElement().getFirstChild(),
				document2.getDocumentElement().getFirstChild());
	}

	private boolean stringUnorderedEquals(final String s1, final String s2)
			throws SAXException, IOException {
		final Document document1 = this.builder.parse(
				new ByteArrayInputStream(("<r>" + s1 + "</r>").getBytes()));
		final Document document2 = this.builder.parse(
				new ByteArrayInputStream(("<r>" + s2 + "</r>").getBytes()));
		return this.unorderedEquals(document1.getDocumentElement().getFirstChild(),
				document2.getDocumentElement().getFirstChild());
	}
}
*/