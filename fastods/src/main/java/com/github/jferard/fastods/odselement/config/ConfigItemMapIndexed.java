/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.odselement.config;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Map with indexed elements: no name is required.
 * 3.10.4 config:config-item-map-indexed
 *
 * @author Julien Férard
 */
public class ConfigItemMapIndexed implements ConfigItemCollection<ConfigItemMapEntry> {
    private final String name;
    private final List<ConfigItemMapEntry> list;

    /**
     * @param name the name of the item (= map)
     */
    public ConfigItemMapIndexed(final String name) {
        this.name = name;
        this.list = new ArrayList<ConfigItemMapEntry>();
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    /**
     * @param o the object to find
     * @return true iff the object is in the map
     */
    public boolean contains(final Object o) {
        return this.list.contains(o);
    }

    @Override
    public Iterator<ConfigItemMapEntry> iterator() {
        return this.list.iterator();
    }

    /**
     * @param configItemMapEntry the entry to add
     * @return true if the new entry was added
     */
    public boolean add(final ConfigItemMapEntry configItemMapEntry) {
        return this.list.add(configItemMapEntry);
    }

    /**
     * @param index the index of the element to be removed
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &ge; size())
     */
    public void remove(final int index) {
        this.list.remove(index);
    }

    /**
     * @param index the index of the entry to return
     * @return the entry
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &ge; size())
     */
    public ConfigItemMapEntry get(final int index) {
        return this.list.get(index);
    }

    /**
     * @param index   the index of the entry to set
     * @param element the new element to set
     * @return the previous element
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &ge; size())
     */
    public ConfigItemMapEntry set(final int index, final ConfigItemMapEntry element) {
        return this.list.set(index, element);
    }

    /**
     * @param index   the index of the entry to add
     * @param element the new element to add
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &ge; size())
     */
    public void add(final int index, final ConfigItemMapEntry element) {
        this.list.add(index, element);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<config:config-item-map-indexed");
        util.appendEAttribute(appendable, "config:name", this.name);
        appendable.append(">");
        for (final ConfigItemMapEntry entry : this.list) {
            entry.appendXMLContent(util, appendable);
        }
        appendable.append("</config:config-item-map-indexed>");
    }
}
