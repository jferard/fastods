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

package com.github.jferard.fastods.testlib;

import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * A helper class for odftoolkit comparisons
 *
 * @author Julien Férard
 */
public class OdfToolkitUtil {
    public static String getStringValue(final Cell cell) {
        return cell.getOdfElement().getOfficeStringValueAttribute();
    }

    public static String getParentStyleName(final Cell cell) {
        return cell.getOdfElement().getAutomaticStyle().getStyleParentStyleNameAttribute();
    }

    public static OdfStyle getDocumentStyle(final SpreadsheetDocument document, final String styleName,
                                            final OdfStyleFamily styleFamily) throws Exception {
        return document.getStylesDom().getOfficeStyles().getStyle(styleName, styleFamily);
    }

    public static Node getFirstElement(final Element element, final String tagname) {
        return element.getElementsByTagName(tagname).item(0);
    }

    public static String getAttribute(final Node node, final String sattributeName) {
        final NamedNodeMap attributes = node.getAttributes();
        return attributes.getNamedItem(sattributeName).getTextContent();
    }

    public static String getStyleName(final Cell cell) {
        return cell.getOdfElement().getStyleName();
    }

    public static String getStyleFamilyName(final Cell cell) {
        return cell.getOdfElement().getStyleFamily().getName();
    }
}
