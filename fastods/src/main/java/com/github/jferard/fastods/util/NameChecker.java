/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
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

import com.github.jferard.fastods.FastOdsException;

/**
 * 18.3.32 styleNameRef
 * A NCName as specified in [xmlschema-2] that is the name of a referenced style, or an empty value.
 * <br/>
 * In [xmlschema-2] (https://www.w3.org/TR/xmlschema-2/):
 * NCName represents XML "non-colonized" Names. The ·value space· of NCName is the set of all strings which ·match·
 * the NCName production of [Namespaces in XML]. "Non-colonized" equals non qualified.
 * <br/>
 * In [Namespaces in XML] (https://www.w3.org/TR/REC-xml-names/):
 * [4]   	NCName	   ::=   	Name - (Char* ':' Char*)	// An XML Name, minus the ":"
 * <br/>
 * In https://www.w3.org/TR/REC-xml/#NT-Name:
 * [4]   	NameStartChar	   ::=   	":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
 * [4a]   	NameChar	   ::=   	NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
 * [5]   	Name	   ::=   	NameStartChar (NameChar)*
 */
public class NameChecker {
    public void checkStyleName(final String name) throws FastOdsException {
        if (name.isEmpty()) return;

        final int firstCodePoint = name.codePointAt(0);
        if (!this.isNCNameStartChar(firstCodePoint)) {
            throw new FastOdsException(
                    String.format("Bad first char %c (#x%x) in %s", firstCodePoint, firstCodePoint, name));
        }

        for (int i = 1; i < name.length(); i++) {
            final int codePoint = name.codePointAt(i);
            if (!this.isNCNameChar(codePoint)) {
                throw new FastOdsException(
                        String.format("Bad char %d: %c (#x%x) in %s", i, codePoint, codePoint, name));
            }
        }
        // ok
    }

    public boolean isNameStartChar(final int codePoint) {
        return 'a' <= codePoint && codePoint <= 'z' || 'A' <= codePoint && codePoint <= 'Z' || codePoint == ':' ||
                codePoint == '-' || 0xc0 <= codePoint && codePoint <= 0x2ff && codePoint != 0xd7 && codePoint != 0xf7
                || 0x370 <= codePoint && codePoint <= 0x1fff && codePoint != 0x37e || codePoint == 0x200c ||
                codePoint == 0x200d || 0x2070 <= codePoint && codePoint <= 0x218f || 0x2c00 <= codePoint && codePoint
                <= 0x2fef || 0x3001 <= codePoint && codePoint <= 0xd7ff || 0xf900 <= codePoint && codePoint <= 0xfdcf
                || 0xfdf0 <= codePoint && codePoint <= 0xfffd || 0x10000 <= codePoint && codePoint <= 0xeffff;
    }

    public boolean isNCNameStartChar(final int codePoint) {
        return 'a' <= codePoint && codePoint <= 'z' || 'A' <= codePoint && codePoint <= 'Z' ||
                codePoint == '-' || 0xc0 <= codePoint && codePoint <= 0x2ff && codePoint != 0xd7 && codePoint != 0xf7
                || 0x370 <= codePoint && codePoint <= 0x1fff && codePoint != 0x37e || codePoint == 0x200c ||
                codePoint == 0x200d || 0x2070 <= codePoint && codePoint <= 0x218f || 0x2c00 <= codePoint && codePoint
                <= 0x2fef || 0x3001 <= codePoint && codePoint <= 0xd7ff || 0xf900 <= codePoint && codePoint <= 0xfdcf
                || 0xfdf0 <= codePoint && codePoint <= 0xfffd || 0x10000 <= codePoint && codePoint <= 0xeffff;
    }

    public boolean isNameChar(final int codePoint) {
        return 'a' <= codePoint && codePoint <= 'z' || 'A' <= codePoint && codePoint <= 'Z' || codePoint == ':' ||
                codePoint == '_' || codePoint == '-' || codePoint == '.' || '0' <= codePoint && codePoint <= '9' ||
                codePoint == 0xb7 || 0xc0 <= codePoint && codePoint <= 0x2ff && codePoint != 0xd7 && codePoint !=
                0xf7 || 0x300 <= codePoint && codePoint <= 0x36F || 0x370 <= codePoint && codePoint <= 0x1fff &&
                codePoint != 0x37e || codePoint == 0x200c || codePoint == 0x200d || 0x203F <= codePoint && codePoint
                <= 0x2040 || 0x2070 <= codePoint && codePoint <= 0x218f || 0x2c00 <= codePoint && codePoint <= 0x2fef
                || 0x3001 <= codePoint && codePoint <= 0xd7ff || 0xf900 <= codePoint && codePoint <= 0xfdcf || 0xfdf0
                <= codePoint && codePoint <= 0xfffd || 0x10000 <= codePoint && codePoint <= 0xeffff;
    }

    public boolean isNCNameChar(final int codePoint) {
        return 'a' <= codePoint && codePoint <= 'z' || 'A' <= codePoint && codePoint <= 'Z' ||
                codePoint == '_' || codePoint == '-' || codePoint == '.' || '0' <= codePoint && codePoint <= '9' ||
                codePoint == 0xb7 || 0xc0 <= codePoint && codePoint <= 0x2ff && codePoint != 0xd7 && codePoint !=
                0xf7 || 0x300 <= codePoint && codePoint <= 0x36F || 0x370 <= codePoint && codePoint <= 0x1fff &&
                codePoint != 0x37e || codePoint == 0x200c || codePoint == 0x200d || 0x203F <= codePoint && codePoint
                <= 0x2040 || 0x2070 <= codePoint && codePoint <= 0x218f || 0x2c00 <= codePoint && codePoint <= 0x2fef
                || 0x3001 <= codePoint && codePoint <= 0xd7ff || 0xf900 <= codePoint && codePoint <= 0xfdcf || 0xfdf0
                <= codePoint && codePoint <= 0xfffd || 0x10000 <= codePoint && codePoint <= 0xeffff;
    }
}
