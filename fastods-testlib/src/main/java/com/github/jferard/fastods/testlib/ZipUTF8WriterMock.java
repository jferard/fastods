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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;

/**
 * See https://github.com/jferard/fastods/issues/29
 *
 * Usage:
 * <pre><code>
 *     final ZipUTF8WriterMockHandler mockHandler = ZipUTF8WriterMockHandler.create();
 *     ZiPUTF8Writer instance = mockHandler.getInstance()
 *     // do something with instance
 *     // mockHandler.getEntryAsDocument(name) returns the chosen entry.
 * </code></pre>
 *
 *
 * @author Julien Férard
 */
public class ZipUTF8WriterMock implements Appendable {
    private final Map<String, StringBuilder> builderByEntryName;
    private StringBuilder curBuilder;

    ZipUTF8WriterMock(final Map<String, StringBuilder> builderByEntryName) {
        this.builderByEntryName = builderByEntryName;
        this.curBuilder = null;
    }

    public static ZipUTF8WriterMock createMock() {
            return new ZipUTF8WriterMock(new HashMap<String, StringBuilder>());
    }

    public Appendable append(final char c) throws IOException {
        if (this.curBuilder == null)
            throw new IOException();

        this.curBuilder.append(c);
        return this;
    }

    public Appendable append(final CharSequence arg0) throws IOException {
        if (this.curBuilder == null)
            throw new IOException();

        return this.curBuilder.append(arg0);
    }

    public Appendable append(final CharSequence csq, final int start, final int end)
            throws IOException {
        if (this.curBuilder == null)
            throw new IOException();

        return this.curBuilder.append(csq, start, end);
    }

    public void close() throws IOException {
        if (this.curBuilder != null)
            throw new IOException();

        this.curBuilder = null;
    }

    public void closeEntry() throws IOException {
        if (this.curBuilder == null)
            throw new IOException();

        this.curBuilder = null;
    }

    public void finish() throws IOException {
        this.curBuilder = null;
    }

    public void flush() throws IOException {
    }

    public void putNextEntry(final ZipEntry arg0) throws IOException {
        this.curBuilder = new StringBuilder();
        this.builderByEntryName.put(arg0.getName(), this.curBuilder);
    }

    public void setComment(final String comment) { throw new RuntimeException(); }

    public void write(final String str) throws IOException {
        if (this.curBuilder == null)
            throw new IOException();

        this.curBuilder.append(str);
    }

    public StringBuilder getBuilder(final String name) {
        return this.builderByEntryName.get(name);
    }
}

