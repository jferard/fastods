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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;

import static com.github.jferard.fastods.odselement.MetaElement.OFFICE_VERSION;

/**
 * styles.xml/office:document-styles
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class StylesElement implements OdsElement {
    public static final Map<String, String> STYLES_NAMESPACE_BY_PREFIX = new HashMap<String, String>();

    static {
        STYLES_NAMESPACE_BY_PREFIX.putAll(MetaElement.META_NAMESPACE_BY_PREFIX);

        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:style", "urn:oasis:names:tc:opendocument:xmlns:style:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:text", "urn:oasis:names:tc:opendocument:xmlns:text:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:table", "urn:oasis:names:tc:opendocument:xmlns:table:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:draw", "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:fo",
                "urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:number",
                "urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:presentation",
                "urn:oasis:names:tc:opendocument:xmlns:presentation:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:svg",
                "urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:chart", "urn:oasis:names:tc:opendocument:xmlns:chart:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:dr3d", "urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:math", "http://www.w3.org/1998/Math/MathML");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:form", "urn:oasis:names:tc:opendocument:xmlns:form:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:script", "urn:oasis:names:tc:opendocument:xmlns:script:1.0");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:ooow", "http://openoffice.org/2004/writer");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:oooc", "http://openoffice.org/2004/calc");
        STYLES_NAMESPACE_BY_PREFIX.put("xmlns:dom", "http://www.w3.org/2001/xml-events");
    }

    private static void appendDefaultFooterHeaderStyle(final XMLUtil util,
                                                       final Appendable appendable,
                                                       final String name) throws IOException {
        appendable.append("<style:style");
        util.appendEAttribute(appendable, "style:name", name);
        util.appendAttribute(appendable, "style:family", "paragraph");
        util.appendAttribute(appendable, "style:parent-style-name", "Standard");
        util.appendAttribute(appendable, "style:class", "extra");
        appendable.append("><style:paragraph-properties");
        util.appendAttribute(appendable, "text:number-lines", false);
        util.appendAttribute(appendable, "text:line-number", 0);
        appendable.append("/></style:style>");
    }

    private final StylesContainerImpl stylesContainer;

    /**
     * @param stylesContainer the container for all styles
     */
    public StylesElement(final StylesContainerImpl stylesContainer) {
        this.stylesContainer = stylesContainer;
    }

    /**
     * @return the container of the styles
     */
    public StylesContainer getStyleTagsContainer() {
        return this.stylesContainer;
    }

    @Override
    public void write(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        final HasFooterHeader hasFooterHeader = this.stylesContainer.hasFooterHeader();

        writer.putNextEntry(new ZipEntry("styles.xml"));
        writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.append("<office:document-styles");
        for (final Map.Entry<String, String> entry: STYLES_NAMESPACE_BY_PREFIX.entrySet()) {
            util.appendAttribute(writer, entry.getKey(), entry.getValue());
        }
        util.appendAttribute(writer, "office:version", OFFICE_VERSION);
        writer.append(">");
        this.stylesContainer.writeFontFaceDecls(util, writer);
        writer.append("<office:styles>");
        this.stylesContainer.writeStylesCommonStyles(util, writer); // table-cell
        this.stylesContainer.writeVisibleDataStyles(util, writer); // table-cell

        if (hasFooterHeader.hasHeader()) {
            StylesElement.appendDefaultFooterHeaderStyle(util, writer, "Header");
        }
        if (hasFooterHeader.hasFooter()) {
            StylesElement.appendDefaultFooterHeaderStyle(util, writer, "Footer");
        }

        writer.append("</office:styles>");
        writer.append("<office:automatic-styles>");

        this.stylesContainer.writeStylesAutomaticStyles(util, writer);
        this.stylesContainer.writePageLayoutStyles(util, writer);

        writer.append("</office:automatic-styles>");
        writer.append("<office:master-styles>");

        this.stylesContainer.writeMasterPageStyles(util, writer);

        writer.append("</office:master-styles>");
        writer.append("</office:document-styles>");
        writer.flush();
        writer.closeEntry();
    }
}
