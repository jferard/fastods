/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.Stack;

/**
 * A tester for children of a node.
 *
 * @author Julien Férard
 */
public abstract class ChildrenTester {
    protected final Stack<String> state1;
    protected final Stack<String> state2;
    private String firstDifference;

    protected ChildrenTester() {
        this.state1 = new Stack<String>();
        this.state2 = new Stack<String>();
        this.firstDifference = null;
    }

    /**
     * @param element1 the first node
     * @param element2 the second node
     * @return true if the attributes of the first node are equal to the attributes of the second
     * node
     */
    protected boolean attributesEquals(final Node element1, final Node element2) {
        if (element1 == element2) {
            return true;
        }

        final NamedNodeMap attributes1 = element1.getAttributes();
        final NamedNodeMap attributes2 = element2.getAttributes();
        if (attributes1 == null) {
            if (attributes2 != null) {
                this.logFail("Node %s has attributes while node %s hasn't");
                return false;
            }
        } else if (attributes2 == null) {
            this.logFail("Node %s has no attribute while node %s has some");
            return false;
        } else {
            final int l1 = attributes1.getLength();
            final int l2 = attributes2.getLength();
            if (l1 != l2) {
                this.logFail("Different attributes number: %s vs %s (%d vs %d)", l1, l2);
                return false;
            }

            final AttrList list1 = AttrList.create(attributes1);
            final AttrList list2 = AttrList.create(attributes2);
            if (!list1.equals(list2)) {
                this.logFail("Different attributes: %s vs %s (%s vs %s)", list1, list2);
                return false;
            }
        }
        return true;
    }

    private boolean namesEquals(final Node element1, final Node element2) {
        final boolean typesEqual = element1.getNodeType() == element2.getNodeType();
        if (!typesEqual) {
            this.logFail("Different nodes types: %s vs %s");
            return false;
        }
        final boolean namesEqual = Objects.equal(element1.getNodeName(), element2.getNodeName());
        if (!namesEqual) {
            this.logFail("Different nodes names: %s vs %s");
            return false;
        }
        final boolean valuesEqual = Objects.equal(element1.getNodeValue(), element2.getNodeValue());
        if (!valuesEqual) {
            this.logFail("Different nodes values: %s vs %s");
            return false;
        }
        final boolean nsURIEqual = Objects
                .equal(element1.getNamespaceURI(), element2.getNamespaceURI());
        if (!nsURIEqual) {
            this.logFail("Different nodes namespaces: %s vs %s");
            return false;
        }
        return true;
    }

    /**
     * Entry point
     *
     * @param element1 the first node
     * @param element2 the second node
     * @return true if the first node is equal the second node
     */
    boolean equals(final Node element1, final Node element2) {
        if (element1 == null) {
            return element2 == null;
        } else if (element2 == null) {
            return false;
        } else { // element1 != null && element2 != null
            this.pushNode(this.state1, element1);
            this.pushNode(this.state2, element2);
            final boolean ne = this.namesEquals(element1, element2);
            final boolean ae = this.attributesEquals(element1, element2);
            final boolean ce = this.childrenEquals(element1, element2);
            this.state1.pop();
            this.state2.pop();
            return ne && ae && ce;
        }
    }

    private void pushNode(final Stack<String> state1, final Node element1) {
        if (element1.getNodeType() == Node.TEXT_NODE) {
            state1.push(element1.getNodeValue());
        } else {
            state1.push(element1.getNodeName());
        }
    }

    /**
     * @param element1 the first node
     * @param element2 the second node
     * @return true if the children of the first node are equal to the children of the second node
     */
    public abstract boolean childrenEquals(final Node element1, final Node element2);


    private String getStateStr(final Stack<String> p) {
        if (p.size() <= 1) {
            return "<root>";
        } else {
            return Joiner.on("/").join(p.subList(1, p.size()));
        }
    }

    protected void logFail(final String format, final Object... extra) {
        final Object[] objects = Iterables.toArray(Iterables
                .concat(Arrays.asList(this.getStateStr(this.state1), this.getStateStr(this.state2)),
                        Arrays.asList(extra)), Object.class);

        this.firstDifference = String.format(format, objects);
    }

    public Optional<String> getFirstDifference() {
        return Optional.fromNullable(this.firstDifference);
    }

    public void setFirstDifference(final String difference) {
        this.firstDifference = difference;
    }
}
