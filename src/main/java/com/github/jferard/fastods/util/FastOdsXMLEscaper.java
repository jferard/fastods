/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods.util;

import java.util.HashMap;
import java.util.Map;

/**
 * The FastOdsXMLEscaper escapes xml chars as fast as possible. That means : all
 * is grouped in a single method body to avoid unnecessary method calls.
 *
 * The design was inspired by Google Guava's Escapers, but the implementation
 * aims to be faster in that specific case.
 *
 * @author Julien Férard Copyright (C) 2016 J. Férard
 *
 *         This file Util.java is part of FastODS.
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
	private final Map<String, String> attrCacheMap;

	private char[] buffer;
	private final Map<String, String> contentCacheMap;

	public FastOdsXMLEscaper() {
		this(FastOdsXMLEscaper.BUFFER_SIZE);
	}

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
			final int length = s.length();
			int destIndex = 0;
			int copyFromIndex = 0;
			int copyToIndex = 0;
			boolean oneSpecialChar = false;
			for (int sourceIndex = 0; sourceIndex < length; sourceIndex++) {
				final char c = s.charAt(sourceIndex);
				int toCopyIndex;
				int toCopyLen;
				if (c == '&') {
					copyToIndex = sourceIndex + 1; // gobble the ampersand
					toCopyIndex = 0;
					toCopyLen = 4;
				} else if (c == '<') {
					copyToIndex = sourceIndex;
					toCopyIndex = 4;
					toCopyLen = 4;
				} else if (c == '>') {
					copyToIndex = sourceIndex;
					toCopyIndex = 8;
					toCopyLen = 4;
				} else if (c == '\'') { // begin attribute
					copyToIndex = sourceIndex;
					toCopyIndex = 12;
					toCopyLen = 6;
				} else if (c == '"') {
					copyToIndex = sourceIndex;
					toCopyIndex = 18;
					toCopyLen = 6;
				} else if (c == '\t') {
					copyToIndex = sourceIndex;
					toCopyIndex = 30;
					toCopyLen = 5;
				} else if (c == '\n') {
					copyToIndex = sourceIndex;
					toCopyIndex = 35;
					toCopyLen = 5;
				} else if (c == '\r') {
					copyToIndex = sourceIndex;
					toCopyIndex = 40;
					toCopyLen = 5;
				} else if (c < 0x20) {
					copyToIndex = sourceIndex;
					toCopyIndex = 24;
					toCopyLen = 6;
				} else {
					toCopyIndex = -1;
					toCopyLen = -1;
				}

				if (toCopyLen != -1) {
					oneSpecialChar = true;
					// ensure buffer size
					if (destIndex + toCopyLen + 1 >= this.buffer.length - 1) {
						final char[] newBuffer = new char[2
								* this.buffer.length];
						System.arraycopy(this.buffer, 0, newBuffer, 0,
								destIndex);
						this.buffer = newBuffer;
					}
					// put in the buffer the identical chars
					if (copyToIndex > copyFromIndex) {
						s.getChars(copyFromIndex, copyToIndex, this.buffer,
								destIndex);
						destIndex += copyToIndex - copyFromIndex;
					}
					copyFromIndex = sourceIndex + 1;
					// put the new chars in the buffer
					for (int i = 0; i < toCopyLen; i++) {
						this.buffer[destIndex++] = FastOdsXMLEscaper.TO_COPY[toCopyIndex
								+ i];
					}
				}
			}

			if (oneSpecialChar) { // at least
				if (destIndex >= this.buffer.length) {
					final char[] newBuffer = new char[2 * this.buffer.length];
					System.arraycopy(this.buffer, 0, newBuffer, 0, destIndex);
					this.buffer = newBuffer;
				}
				if (length > copyFromIndex) {
					s.getChars(copyFromIndex, length, this.buffer, destIndex);
					destIndex += length - copyFromIndex;
				}
				s2 = new String(this.buffer, 0, destIndex);
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
			final int length = s.length();
			int destIndex = 0;
			int copyFromIndex = 0;
			int copyToIndex = 0;
			boolean oneSpecialChar = false;
			for (int sourceIndex = 0; sourceIndex < length; sourceIndex++) {
				final char c = s.charAt(sourceIndex);
				int toCopyIndex;
				int toCopyLen;
				if (c == '&') {
					copyToIndex = sourceIndex + 1; // gobble the ampersand
					toCopyIndex = 0;
					toCopyLen = 4;
				} else if (c == '<') {
					copyToIndex = sourceIndex;
					toCopyIndex = 4;
					toCopyLen = 4;
				} else if (c == '>') {
					copyToIndex = sourceIndex;
					toCopyIndex = 8;
					toCopyLen = 4;
				} else if (c == '\t' || c == '\n' || c == '\r') {
					// do nothing, but avoid replacement by \\uFFFD
					toCopyIndex = -1;
					toCopyLen = -1;
				} else if (c < 0x20) {
					copyToIndex = sourceIndex;
					toCopyIndex = 24;
					toCopyLen = 6;
				} else {
					toCopyIndex = -1;
					toCopyLen = -1;
				}

				if (toCopyLen != -1) {
					oneSpecialChar = true;
					// ensure buffer size
					if (destIndex + toCopyLen + 1 >= this.buffer.length - 1) {
						final char[] newBuffer = new char[2
								* this.buffer.length];
						System.arraycopy(this.buffer, 0, newBuffer, 0,
								destIndex);
						this.buffer = newBuffer;
					}
					// put in the buffer the identical chars
					if (copyToIndex > copyFromIndex) {
						s.getChars(copyFromIndex, copyToIndex, this.buffer,
								destIndex);
						destIndex += copyToIndex - copyFromIndex;
					}
					copyFromIndex = sourceIndex + 1;
					// put the new chars in the buffer
					for (int i = 0; i < toCopyLen; i++) {
						this.buffer[destIndex++] = FastOdsXMLEscaper.TO_COPY[toCopyIndex
								+ i];
					}
				}
			}

			if (oneSpecialChar) { // at least
				if (destIndex >= this.buffer.length) {
					final char[] newBuffer = new char[2 * this.buffer.length];
					System.arraycopy(this.buffer, 0, newBuffer, 0, destIndex);
					this.buffer = newBuffer;
				}
				if (length > copyFromIndex) {
					s.getChars(copyFromIndex, length, this.buffer, destIndex);
					destIndex += length - copyFromIndex;
				}
				s2 = new String(this.buffer, 0, destIndex);
			} else
				s2 = s;
			this.contentCacheMap.put(s, s2);
		}
		return s2;

		/*
		if (s == null)
			return null;

		String s2 = this.contentCacheMap.get(s);
		if (s2 == null) {
			final int length = s.length();
			int destIndex = 0;
			int copyFromIndex = 0;
			int copyToIndex = 0;
			String toCopy = "";
			boolean specialChar = false;
			boolean oneSpecialChar = false;
			for (int sourceIndex = 0; sourceIndex < length; sourceIndex++) {
				final char c = s.charAt(sourceIndex);
				if (c == '&') {
					copyToIndex = sourceIndex + 1;
					toCopy = "amp;";
					specialChar = true;
				} else if (c == '<') {
					copyToIndex = sourceIndex;
					toCopy = "&lt;";
					specialChar = true;
				} else if (c == '>') {
					copyToIndex = sourceIndex;
					toCopy = "&gt;";
					specialChar = true;
				} else if (c == '\t' || c == '\n' || c == '\r') {
					// do nothing !
				} else if (c < 0x20) {
					copyToIndex = sourceIndex;
					toCopy = "\uFFFD";
					specialChar = true;
				}
				if (specialChar) {
					oneSpecialChar = true;
					if (destIndex + toCopy.length() + 1 >= this.buffer.length
							- 1) {
						final char[] newBuffer = new char[2
								* this.buffer.length];
						System.arraycopy(this.buffer, 0, newBuffer, 0,
								destIndex);
						this.buffer = newBuffer;
					}
					if (copyToIndex > copyFromIndex) {
						s.getChars(copyFromIndex, copyToIndex, this.buffer,
								destIndex);
						destIndex += copyToIndex - copyFromIndex;
					}
					copyFromIndex = sourceIndex + 1;
					specialChar = false;
					for (final char c2 : toCopy.toCharArray())
						this.buffer[destIndex++] = c2;
				}
			}

			if (oneSpecialChar) {
				if (destIndex >= this.buffer.length) {
					final char[] newBuffer = new char[2 * this.buffer.length];
					System.arraycopy(this.buffer, 0, newBuffer, 0, destIndex);
					this.buffer = newBuffer;
				}
				if (length > copyFromIndex) {
					s.getChars(copyFromIndex, length, this.buffer, destIndex);
					destIndex += length - copyFromIndex;
				}
				s2 = new String(this.buffer, 0, destIndex);
			} else
				s2 = s;
			this.contentCacheMap.put(s, s2);
		}
		return s2;
		*/
	}
}
