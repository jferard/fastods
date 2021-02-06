/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

/**
 * An utility class for creating directories, join strings, etc.
 *
 * @author Julien Férard
 */
public final class Util {
    public static final String XML_PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * @param dirName the name of the directory to create
     * @return true if the directory was created
     */
    public static boolean mkdir(final String dirName) {
        return Util.mkdir(new File(dirName));
    }

    /**
     * @param file the directory to create
     * @return true if the directory was created
     */
    public static boolean mkdir(final File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                return false;
            } else {
                throw new IllegalStateException();
            }
        }
        return file.mkdir();
    }

    /**
     * Join a collection on a separator
     * @param separator the separator
     * @param segments the collection
     * @return the joined collection.
     */
    public static String join(final String separator, final Collection<String> segments) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> it = segments.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(separator).append(it.next());
            }
        }
        return sb.toString();
    }

    /**
     * Safe check if objects are equal.
     * @param first the first object
     * @param second the second object
     * @return if objects are equal
     */
    public static boolean equal(final Object first, final Object second) {
        if (first == null) {
            return second == null;
        } else {
            return first.equals(second);
        }
    }

    private Util() {
    }
}
