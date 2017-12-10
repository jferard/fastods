/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods;

import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * A paragraph element represents an element inside paragraph (p tag)
 * @author Julien Férard
 */
public interface ParagraphElement {
    /**
     * Append this element to a paragraph
     * @param util an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    void appendXMLToParagraph(XMLUtil util,
							  Appendable appendable) throws IOException;

    /**
     * @return the text style
     */
    TextStyle getTextStyle();

    /**
     * @return the text content
     */
    String getText();
}
