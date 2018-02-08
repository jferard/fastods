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

package com.github.jferard.fastods.util;

import java.util.HashMap;
import java.util.Map;

/**
 * The FastOdsXMLEscaper class is an utility class to escape XML special chars.
 * @author Julien Férard
 */
@SuppressWarnings("PMD.UnusedLocalVariable")
public class FastOdsXMLEscaper implements XMLEscaper {
	private static final int BUFFER_SIZE = 65536;
	/**
	 * <pre>
	 * & : 0,4
	 * < : 4,4
	 * > : 8,4
	 * ' : 12,6
	 * " : 18,6
	 * unknown : 24,6
	 * \t : 30,5
	 * \n : 35,5
	 * \r : 40,5
	 * </pre>
	 *
	 * Beware to the backlash : it has to be escaped or \uFFFD will be
	 * interpreted as a single char
	 */
	private static final char[] TO_COPY = "amp;&lt;&gt;&apos;&quot;\\uFFFD&#x9;&#xA;&#xD;"
			.toCharArray();
	private static final int SPACE = 0x20;
	private final Map<String, String> attrCacheMap;

	private char[] buffer;
	private final Map<String, String> contentCacheMap;

	/**
	 * @return an xml escaper with the default buffer size (65536 bytes)
	 */
	public static FastOdsXMLEscaper create() {
		return new FastOdsXMLEscaper(FastOdsXMLEscaper.BUFFER_SIZE);
	}

	/**
	 * Creates an xml escaper with a specified buffer size
	 * @param bufferSize the buffer size
	 */
	public FastOdsXMLEscaper(final int bufferSize) {
		this.attrCacheMap = new HashMap<String, String>();
		this.contentCacheMap = new HashMap<String, String>();
		this.buffer = new char[bufferSize];
	}

	@Override
	public String escapeXMLAttribute(final String s) {
		if (s == null)
			return null;

		String s2 = this.attrCacheMap.get(s);
		if (s2 == null) {
			final int sourceLength = s.length();
			int previousDestIndex = 0;
			int firstIdenticalCharInSourceIndex = 0;
			int firstDifferentCharInSourceIndex = 0;
			boolean oneSpecialChar = false;
			for (int sourceIndex = 0; sourceIndex < sourceLength; sourceIndex++) {
				final char c = s.charAt(sourceIndex);
				final int toCopyIndex;
				final int toCopyLen;
				if (c == '&') {
					firstDifferentCharInSourceIndex = sourceIndex + 1; // gobble
																		// the
																		// ampersand
					toCopyIndex = 0;
					toCopyLen = 4;
				} else if (c == '<') {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 4;
					toCopyLen = 4;
				} else if (c == '>') {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 8;
					toCopyLen = 4;
				} else if (c == '\'') { // begin attribute
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 12;
					toCopyLen = 6;
				} else if (c == '"') {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 18;
					toCopyLen = 6;
				} else if (c == '\t') {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 30;
					toCopyLen = 5;
				} else if (c == '\n') {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 35;
					toCopyLen = 5;
				} else if (c == '\r') {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 40;
					toCopyLen = 5;
				} else if (c < SPACE) {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 24;
					toCopyLen = 6;
				} else {
					toCopyIndex = -1;
					toCopyLen = -1;
				}

				// trigger copy only if a special char appears
				if (toCopyLen != -1) {
					oneSpecialChar = true;
					final int identicalCount = firstDifferentCharInSourceIndex
							- firstIdenticalCharInSourceIndex;

					// first : ensure buffer size
					if (previousDestIndex + toCopyLen + identicalCount >= this.buffer.length) {
						final char[] newBuffer = new char[2
								* this.buffer.length];
						System.arraycopy(this.buffer, 0, newBuffer, 0,
								previousDestIndex);
						this.buffer = newBuffer;
					}
					// second : put in the buffer the identical chars, from sourceIndex
					// (one exception = &)
					if (identicalCount > 0) {
						s.getChars(firstIdenticalCharInSourceIndex,
								firstDifferentCharInSourceIndex, this.buffer,
								previousDestIndex);
						previousDestIndex += identicalCount;
					}
					// third : put the new chars in the buffer
					for (int i = 0; i < toCopyLen; i++) {
						this.buffer[previousDestIndex++] = FastOdsXMLEscaper.TO_COPY[toCopyIndex
								+ i];
					}
					firstIdenticalCharInSourceIndex = sourceIndex + 1; // next one
				}
			}

			// we might have some chars to copy
			if (oneSpecialChar) {
				final int identicalCount = sourceLength - firstIdenticalCharInSourceIndex;
				if (previousDestIndex + identicalCount >= this.buffer.length) {
					final char[] newBuffer = new char[2 * this.buffer.length];
					System.arraycopy(this.buffer, 0, newBuffer, 0, previousDestIndex);
					this.buffer = newBuffer;
				}
				if (identicalCount > 0) {
					s.getChars(firstIdenticalCharInSourceIndex, sourceLength,
							this.buffer, previousDestIndex);
					previousDestIndex += identicalCount;
				}
				s2 = new String(this.buffer, 0, previousDestIndex);
			} else
				s2 = s;
			this.attrCacheMap.put(s, s2);
		}
		return s2;
	}

	@Override
	public String escapeXMLContent(final String s) {
		if (s == null)
			return null;

		String s2 = this.contentCacheMap.get(s);
		if (s2 == null) {
			final int sourceLength = s.length();
			int previousDestIndex = 0;
			int firstIdenticalCharInSourceIndex = 0;
			int firstDifferentCharInSourceIndex = 0;
			boolean oneSpecialChar = false;
			for (int sourceIndex = 0; sourceIndex < sourceLength; sourceIndex++) {
				final char c = s.charAt(sourceIndex);
				final int toCopyIndex;
				final int toCopyLen;
				if (c == '&') {
					firstDifferentCharInSourceIndex = sourceIndex + 1; // gobble
																		// the
																		// ampersand
					toCopyIndex = 0;
					toCopyLen = 4;
				} else if (c == '<') {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 4;
					toCopyLen = 4;
				} else if (c == '>') {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 8;
					toCopyLen = 4;
				} else if (c == '\t' || c == '\n' || c == '\r') {
					// do nothing, but avoid replacement by \\uFFFD
					toCopyIndex = -1;
					toCopyLen = -1;
				} else if (c < SPACE) {
					firstDifferentCharInSourceIndex = sourceIndex;
					toCopyIndex = 24;
					toCopyLen = 6;
				} else {
					toCopyIndex = -1;
					toCopyLen = -1;
				}

				// trigger copy only if a special char appears
				if (toCopyLen != -1) {
					oneSpecialChar = true;
					final int identicalCount = firstDifferentCharInSourceIndex
							- firstIdenticalCharInSourceIndex;
					// ensure buffer size
					if (previousDestIndex + toCopyLen + identicalCount >= this.buffer.length) {
						final char[] newBuffer = new char[2
								* this.buffer.length];
						System.arraycopy(this.buffer, 0, newBuffer, 0,
								previousDestIndex);
						this.buffer = newBuffer;
					}
					// put in the buffer the identical chars
					if (identicalCount > 0) {
						s.getChars(firstIdenticalCharInSourceIndex,
								firstDifferentCharInSourceIndex, this.buffer,
								previousDestIndex);
						previousDestIndex += identicalCount;
					}
					// put the new chars in the buffer
					for (int i = 0; i < toCopyLen; i++) {
						this.buffer[previousDestIndex++] = FastOdsXMLEscaper.TO_COPY[toCopyIndex
								+ i];
					}
					firstIdenticalCharInSourceIndex = sourceIndex + 1; // next one
				}
			}

			// we might have some chars to copy
			if (oneSpecialChar) { // at least
				final int identicalCount = sourceLength - firstIdenticalCharInSourceIndex;
				if (previousDestIndex + identicalCount >= this.buffer.length) {
					final char[] newBuffer = new char[2 * this.buffer.length];
					System.arraycopy(this.buffer, 0, newBuffer, 0, previousDestIndex);
					this.buffer = newBuffer;
				}
				if (identicalCount > 0) {
					s.getChars(firstIdenticalCharInSourceIndex, sourceLength,
							this.buffer, previousDestIndex);
					previousDestIndex += identicalCount;
				}
				s2 = new String(this.buffer, 0, previousDestIndex);
			} else
				s2 = s;
			this.contentCacheMap.put(s, s2);
		}
		return s2;
	}
}
