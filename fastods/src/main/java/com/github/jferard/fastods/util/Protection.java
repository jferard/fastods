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

import java.io.IOException;

/**
 * The Protection instances should be created using the fastods.crypto submodule:
 *
 *     ProtectionFactory.createSha256(new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd' });
 */
public class Protection {
    private final String base64key;
    private final String algorithm;

    /**
     * **Do not use this constructor, use ProtectionFactory instead**
     *
     * @param base64key the digest of the password in base 64 format
     * @param algorithm the IRI of the algorithm.
     */
    public Protection(final String base64key, final String algorithm) {
        this.base64key = base64key;
        this.algorithm = algorithm;
    }

    public void appendAttributes(final XMLUtil util, final Appendable appendable) throws IOException {
        util.appendAttribute(appendable, "table:protected", true);
        util.appendAttribute(appendable, "table:protection-key", this.base64key);
        util.appendAttribute(appendable, "table:protection-key-digest-algorithm", this.algorithm);
    }
}
