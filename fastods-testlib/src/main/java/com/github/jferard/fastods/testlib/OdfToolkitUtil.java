/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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
public final class OdfToolkitUtil {
    private OdfToolkitUtil() {
    }

    /**
     * @param cell the cell
     * @return the value of the cell as a string
     */
    public static String getStringValue(final Cell cell) {
        return cell.getOdfElement().getOfficeStringValueAttribute();
    }

    /**
     * @param cell the cell
     * @return the parent style name
     */
    public static String getParentStyleName(final Cell cell) {
        return getAutomaticStyle(cell).getStyleParentStyleNameAttribute();
    }

    public static OdfStyle getAutomaticStyle(final Cell cell) {
        return cell.getOdfElement().getAutomaticStyle();
    }

    /**
     * Return styles.xml representation
     *
     * @param document    the document
     * @param styleName   the style name
     * @param styleFamily the style family
     * @return the styles
     * @throws Exception if an exception occurs
     */
    public static OdfStyle getDocumentStyle(final SpreadsheetDocument document,
                                            final String styleName,
                                            final OdfStyleFamily styleFamily) throws Exception {
        return document.getStylesDom().getOfficeStyles().getStyle(styleName, styleFamily);
    }

    /**
     * Get the first tag with a name
     *
     * @param element the root
     * @param tagName the name of the tag
     * @return the first node with that tag name inside element
     */
    public static Node getFirstElement(final Element element, final String tagName) {
        return element.getElementsByTagName(tagName).item(0);
    }

    /**
     * @param node          the node
     * @param attributeName the name of the attribute
     * @return the value of the attribute as a string
     */
    public static String getAttribute(final Node node, final String attributeName) {
        final NamedNodeMap attributes = node.getAttributes();
        return attributes.getNamedItem(attributeName).getTextContent();
    }

    /**
     * @param cell the cell
     * @return the cell style name
     */
    public static String getStyleName(final Cell cell) {
        return cell.getOdfElement().getStyleName();
    }

    /**
     * @param cell the cell
     * @return the name of the style family of the cell
     */
    public static String getStyleFamilyName(final Cell cell) {
        return cell.getOdfElement().getStyleFamily().getName();
    }
}