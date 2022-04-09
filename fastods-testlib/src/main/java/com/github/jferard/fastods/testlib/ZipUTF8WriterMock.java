/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.testlib;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * See https://github.com/jferard/fastods/issues/29
 * <p>
 * Usage:
 * <pre>{@code
 *     final ZipUTF8WriterMockHandler mockHandler = ZipUTF8WriterMockHandler.create();
 *     ZipUTF8Writer instance = mockHandler.getInstance(ZipUTF8Writer.class)
 *     // do something with instance
 *     // mockHandler.getEntryAsDocument(name) returns the chosen entry.
 * }</pre>
 * <p>
 * This class is just a container for data in a mock zip file.
 *
 * @author Julien Férard
 */
public class ZipUTF8WriterMock implements Appendable {
    /**
     * @return the mock
     */
    public static ZipUTF8WriterMock createMock() {
        return new ZipUTF8WriterMock(new HashMap<>(), new HashSet<>());
    }
    private final Map<String, StringBuilder> builderByEntryName;
    private StringBuilder curBuilder;
    private final Set<String> registeredEntries;

    /**
     * @param builderByEntryName the container
     * @param registeredEntries  the entries
     */
    ZipUTF8WriterMock(final Map<String, StringBuilder> builderByEntryName,
                      final Set<String> registeredEntries) {
        this.builderByEntryName = builderByEntryName;
        this.registeredEntries = registeredEntries;
        this.curBuilder = null;
    }

    @Override
    public Appendable append(final char c) throws IOException {
        if (this.curBuilder == null) {
            throw new IOException();
        }

        this.curBuilder.append(c);
        return this;
    }

    @Override
    public Appendable append(final CharSequence arg0) throws IOException {
        if (this.curBuilder == null) {
            throw new IOException();
        }

        return this.curBuilder.append(arg0);
    }

    @Override
    public Appendable append(final CharSequence csq, final int start, final int end)
            throws IOException {
        if (this.curBuilder == null) {
            throw new IOException();
        }

        return this.curBuilder.append(csq, start, end);
    }

    /**
     * close the zip mock
     *
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {
        if (this.curBuilder != null) {
            throw new IOException();
        }
    }

    /**
     * close the current entry of the zip mock
     *
     * @throws IOException if an I/O error occurs
     */
    public void closeEntry() throws IOException {
        if (this.curBuilder == null) {
            throw new IOException();
        }

        this.curBuilder = null;
    }

    /**
     * finish the zip mock
     */
    public void finish() {
        this.builderByEntryName.put("UnregisteredOdsEntry[path=META-INF/manifest.xml]",
                new StringBuilder());
        this.curBuilder = null;
    }

    /**
     * flush data
     */
    public void flush() {
    }

    public void putAndRegisterNextEntry(final Object object) {
        this.putNextEntry(object);
        this.registerEntry(object);
    }

    /**
     * @param arg0 the entry to put
     */
    public void putNextEntry(final Object arg0) {
        this.curBuilder = new StringBuilder();
        this.builderByEntryName.put(arg0.toString(), this.curBuilder);
    }

    public void registerEntry(final Object object) {
        this.registeredEntries.add(object.toString());
    }


    /**
     * Do not use this!
     *
     * @param comment set a comment
     */
    public void setComment(final String comment) {
        throw new RuntimeException();
    }

    /**
     * Write a string in the current entry
     *
     * @param str the string
     * @throws IOException if an I/O error occurs
     */
    public void write(final String str) throws IOException {
        if (this.curBuilder == null) {
            throw new IOException();
        }

        this.curBuilder.append(str);
    }

    /**
     * Write a byte array in the current entry
     *
     * @param arr the string
     * @throws IOException if an I/O error occurs
     */
    public void write(final byte[] arr) throws IOException {
        if (this.curBuilder == null) {
            throw new IOException();
        }

        this.curBuilder.append(new String(arr, StandardCharsets.ISO_8859_1));
    }

    /**
     * @param name the name of the entry
     * @return the builder
     */
    public StringBuilder getBuilder(final String name) {
        return this.builderByEntryName.get(name);
    }

    /**
     * @return the names of the entries
     */
    public Set<String> getEntryNames() {
        return this.builderByEntryName.keySet();
    }

    /**
     * @return the names of the registered entries
     */
    public Set<String> getRegisteredNames() {
        return this.registeredEntries;
    }
}

