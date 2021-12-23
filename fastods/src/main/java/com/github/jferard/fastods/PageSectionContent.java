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

package com.github.jferard.fastods;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 16.10 style:header
 * 16.11 style:footer
 * <p>
 * "The style:header element represents the content of a header in a style:master-page element."
 * "The style:footer element represents the content of a footer in a style:master-page element."
 * <p>
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public interface PageSectionContent extends ElementWithEmbeddedStyles {

    /**
     * Append the XML representation of a page section to a stream
     *
     * @param util       an util
     * @param appendable the stream
     * @throws IOException If an I/O error occurs
     */
    void appendXMLToMasterStyle(final XMLUtil util, final Appendable appendable) throws IOException;

    /**
     * A region of the header/footer.
     */
    enum Region {
        /**
         * the center region
         */
        CENTER,
        /**
         * the left region
         */
        LEFT,
        /**
         * the right region
         */
        RIGHT
    }
}
