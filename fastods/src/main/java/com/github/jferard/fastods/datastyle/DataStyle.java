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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.style.AddableToOdsElements;
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * A data style. 16.27. Data Styles
 * @author Julien Férard
 */
public interface DataStyle extends NamedObject, AddableToOdsElements {
    /**
     * Add this style to an OdsDocument.
     *
     * @param util       XML util for escaping characters and write data.
     * @param appendable the destination
     * @throws IOException if can't write data to file
     */
    void appendXML(final XMLUtil util,
                   final Appendable appendable) throws IOException;

    /**
     * @return true if the style is an automatic style (3.15.3. office:automatic-styles)
     */
    boolean isHidden();
}
