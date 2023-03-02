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

package com.github.jferard.fastods.util;

import java.util.HashMap;
import java.util.Map;

/**
 * The FastOdsXMLEscaper class is an utility class to escape XML special chars.
 *
 * @author Julien Férard
 */
public class FastOdsXMLEscaper implements XMLEscaper {
    private static final int BUFFER_SIZE = 65536;
    private static final char[][] CHAR_SUBSTITUTES_IN_ATTRIBUTE;
    private static final char[][] CHAR_SUBSTITUTES_IN_CONTENT;

    /**
     * @return an xml escaper with the default buffer size (65536 bytes)
     */
    public static FastOdsXMLEscaper create() {
        return new FastOdsXMLEscaper(FastOdsXMLEscaper.BUFFER_SIZE);
    }

    static {
        final char[] REPLACEMENT_CHAR = "\\uFFFD".toCharArray();
        CHAR_SUBSTITUTES_IN_ATTRIBUTE =
                new char[][]{REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, "&#x9;".toCharArray(), "&#xA;".toCharArray(),
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, "&#xD;".toCharArray(), REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, null, null, "&quot;".toCharArray(), null, null, null,
                        "&amp;".toCharArray(), "&apos;".toCharArray(), null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null,
                        null, null, null, "&lt;".toCharArray(), null, "&gt;".toCharArray()};
        CHAR_SUBSTITUTES_IN_CONTENT =
                new char[][]{REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, null, null, REPLACEMENT_CHAR, REPLACEMENT_CHAR, null,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR, REPLACEMENT_CHAR,
                        REPLACEMENT_CHAR, REPLACEMENT_CHAR, null, null, null, null, null, null,
                        "&amp;".toCharArray(), null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null,
                        "&lt;".toCharArray(), null, "&gt;".toCharArray()};
    }

    private final Map<String, String> attrCacheMap;
    private final Map<String, String> contentCacheMap;
    private char[] buffer;

    /**
     * Creates an xml escaper with a specified buffer size
     *
     * @param bufferSize the buffer size
     */
    public FastOdsXMLEscaper(final int bufferSize) {
        this.attrCacheMap = new HashMap<String, String>();
        this.contentCacheMap = new HashMap<String, String>();
        this.buffer = new char[bufferSize];
    }

    @Override
    public String escapeXMLAttribute(final String s) {
        if (s == null) {
            return null;
        }

        final String cached = this.attrCacheMap.get(s);
        if (cached != null) {
            return cached;
        }

        final String escaped = this.getEscapedString(s, CHAR_SUBSTITUTES_IN_ATTRIBUTE);
        this.attrCacheMap.put(s, escaped);
        return escaped;
    }

    private String getEscapedString(final String s, final char[][] charSubstitutes) {
        final int sourceLength = s.length();
        int previousDestIndex = 0;
        int firstIdenticalCharInSourceIndex = 0;
        boolean oneSpecialChar = false;
        for (int sourceIndex = 0; sourceIndex < sourceLength; sourceIndex++) {
            final char c = s.charAt(sourceIndex);
            if (c <= '>') {
                final char[] toCopy = charSubstitutes[c];
                if (toCopy != null) {
                    oneSpecialChar = true;
                    previousDestIndex = this.fillBufferAndGetPreviousDestIndex(s, previousDestIndex,
                            firstIdenticalCharInSourceIndex, sourceIndex, toCopy);
                    firstIdenticalCharInSourceIndex = sourceIndex + 1; // next one
                }
            }
        }

        final String s2;
        if (oneSpecialChar) {
            s2 = this.getString(s, sourceLength, previousDestIndex,
                    firstIdenticalCharInSourceIndex);
        } else {
            s2 = s;
        }
        return s2;
    }

    private int fillBufferAndGetPreviousDestIndex(final String s, int previousDestIndex,
                                                  final int firstIdenticalCharInSourceIndex,
                                                  final int firstDifferentCharInSourceIndex,
                                                  final char[] toCopy) {
        final int identicalCount =
                firstDifferentCharInSourceIndex - firstIdenticalCharInSourceIndex;

        // first : ensure buffer size
        final int tcl = toCopy.length;
        if (previousDestIndex + tcl + identicalCount >= this.buffer.length) {
            final char[] newBuffer = new char[2 * this.buffer.length];
            System.arraycopy(this.buffer, 0, newBuffer, 0, previousDestIndex);
            this.buffer = newBuffer;
        }
        // second : put in the buffer the identical chars, from sourceIndex
        if (identicalCount > 0) {
            s.getChars(firstIdenticalCharInSourceIndex, firstDifferentCharInSourceIndex,
                    this.buffer, previousDestIndex);
            previousDestIndex += identicalCount;
        }
        // third : put the new chars in the buffer
        for (final char c : toCopy) {
            this.buffer[previousDestIndex++] = c;
        }
        return previousDestIndex;
    }

    private String getString(final String s, final int sourceLength, int previousDestIndex,
                             final int firstIdenticalCharInSourceIndex) {
        final int identicalCount = sourceLength - firstIdenticalCharInSourceIndex;

        // first : ensure buffer size
        if (previousDestIndex + identicalCount >= this.buffer.length) {
            final char[] newBuffer = new char[2 * this.buffer.length];
            System.arraycopy(this.buffer, 0, newBuffer, 0, previousDestIndex);
            this.buffer = newBuffer;
        }
        // second : put in the buffer the identical chars, from sourceIndex
        if (identicalCount > 0) {
            s.getChars(firstIdenticalCharInSourceIndex, sourceLength, this.buffer,
                    previousDestIndex);
            previousDestIndex += identicalCount;
        }
        // third : get the string
        return new String(this.buffer, 0, previousDestIndex);
    }

    @Override
    public String escapeXMLContent(final String s) {
        if (s == null) {
            return null;
        }

        final String cached = this.contentCacheMap.get(s);
        if (cached != null) {
            return cached;
        }

        final String escaped = this.getEscapedString(s, CHAR_SUBSTITUTES_IN_CONTENT);
        this.contentCacheMap.put(s, escaped);
        return escaped;
    }
}
