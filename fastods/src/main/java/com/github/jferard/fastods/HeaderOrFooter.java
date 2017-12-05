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

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * Represents the header/footer of a print page.
 * @author Julien Férard
 */
public interface HeaderOrFooter extends StylesEmbedder {
    /**
     * Append the XML representation of the header/footer to a stream.
     * @param util an util
     * @param appendable the stream
     * @throws IOException if one can't append the content
     */
    void appendXMLToMasterStyle(XMLUtil util, Appendable appendable) throws IOException;

    /**
     * Append the page section style XML representation of the header/footer to a stream.
     * @param util an util
     * @param appendable the stream
     * @throws IOException if one can't append the content
     */
    void appendPageSectionStyleXMLToAutomaticStyle(XMLUtil util, Appendable appendable) throws IOException;
}
