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

package com.github.jferard.fastods.util;

/**
 * A WriteUtil helps to write data to file.
 *
 * @author Julien Férard
 */
public class WriteUtil {
    private static final int DEFAULT_MAX_INT = 1000;

    /**
     * @return a WriteUtil with the default max int in cache.
     */
    public static WriteUtil create() {
        return new WriteUtil(DEFAULT_MAX_INT);
    }

    private final int maxInt;
    private final String[] ints;

    /**
     * @param maxInt the max int in cache
     */
    WriteUtil(final int maxInt) {
        this.maxInt = maxInt;
        this.ints = new String[2 * maxInt];
    }

    /**
     * @param value the value to convert to String
     * @return the same value as a String
     */
    public String toString(final int value) {
        if (-this.maxInt <= value && value < this.maxInt) {
            final int i = value + this.maxInt;
            if (this.ints[i] == null) {
                this.ints[i] = Integer.toString(value);
            }
            return this.ints[i];
        } else {
            return Integer.toString(value);
        }
    }
}
