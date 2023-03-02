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

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 16.6 style:header-style
 * 16.7 style:footer-style
 * 16.10 style:header
 * 16.11 style:footer
 * <p>
 * The PageSection class represents the union of a page section content and a page section
 * style. It may be a footer or a header.
 * <p>
 * Some styles are embedded in the content (e.g. text styles)
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class PageSection implements ElementWithEmbeddedStyles {
    /**
     * Secure version of {@code pageSection.appendPageSectionStyleXMLToAutomaticStyle}: if the
     * footer (or header) is
     * null,
     * then the default type is used.
     *
     * @param header     the footer or header, could be null
     * @param util       an util to write XML data
     * @param appendable the object ot which append footer/header style
     * @throws IOException if footer/header style wasn't appended.
     */
    public static void appendPageSectionStyleXMLToAutomaticStyle(final Header header,
                                                                 final XMLUtil util,
                                                                 final Appendable appendable)
            throws IOException {
        if (header == null) {
            appendable.append("<style:header-style />");
        } else {
            header.appendPageSectionStyleXMLToAutomaticStyle(util, appendable);
        }
    }

    /**
     * Secure version of {@code pageSection.appendPageSectionStyleXMLToAutomaticStyle}: if the
     * footer (or header) is
     * null,
     * then the default type is used.
     *
     * @param footer     the footer or header, could be null
     * @param util       an util to write XML data
     * @param appendable the object ot which append footer/header style
     * @throws IOException if footer/header style wasn't appended.
     */
    public static void appendPageSectionStyleXMLToAutomaticStyle(final Footer footer,
                                                                 final XMLUtil util,
                                                                 final Appendable appendable)
            throws IOException {
        if (footer == null) {
            appendable.append("<style:footer-style />");
        } else {
            footer.appendPageSectionStyleXMLToAutomaticStyle(util, appendable);
        }
    }

    /**
     * Create a builder for a region builder, ie a builder for a footer/header with left, center
     * and right
     *
     * @return the builder
     */
    public static RegionPageSectionBuilder regionBuilder() {
        return new RegionPageSectionBuilder();
    }

    /**
     * Create a builder for a simple builder, ie a builder for a footer/header with only a center
     * region
     *
     * @return the builder
     */
    public static SimplePageSectionBuilder simpleBuilder() {
        return new SimplePageSectionBuilder();
    }

    /**
     * Create a simple footer, with a styled text
     *
     * @param text the text
     * @param ts   the style
     * @return the footer
     */
    public static Footer simpleFooter(final String text, final TextStyle ts) {
        return new SimplePageSectionBuilder().text(Text.styledContent(text, ts)).buildFooter();
    }

    /**
     * Create a simple header, with a styled text
     *
     * @param text the text
     * @param ts   the style
     * @return the header
     */
    public static Header simpleHeader(final String text, final TextStyle ts) {
        return new SimplePageSectionBuilder().text(Text.styledContent(text, ts)).buildHeader();
    }

    private final PageSectionContent content;
    private final PageSectionStyle style;

    /**
     * Create a new footer/header object.
     *
     * @param content the content of the footer/header
     * @param style   the style
     */
    PageSection(final PageSectionContent content, final PageSectionStyle style) {
        this.content = content;
        this.style = style;
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        this.content.addEmbeddedStyles(stylesContainer);
    }

    /**
     * @param util            an instance of the util class for XML writing
     * @param appendable      the appendable element where the method will write the XML
     * @param pageSectionType the type (FOOTER or HEADER)
     * @throws IOException If an I/O error occurs
     */
    public void appendPageSectionStyleXMLToAutomaticStyle(final XMLUtil util,
                                                          final Appendable appendable,
                                                          final Type pageSectionType)
            throws IOException {
        PageSectionStyle
                .appendFooterHeaderStyleXMLToAutomaticStyle(this.style, pageSectionType, util,
                        appendable);
    }

    /**
     * @param util       an instance of the util class for XML writing
     * @param appendable the appendable element where the method will write the XML
     * @throws IOException If an I/O error occurs
     */
    public void appendXMLToMasterStyle(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.content.appendXMLToMasterStyle(util, appendable);
    }

    /**
     * Footer or Header ?
     */
    public enum Type {
        /**
         * Footer
         */
        FOOTER("footer"),
        /**
         * Header
         */
        HEADER("header");

        private final String typeName;

        /**
         * create a type
         *
         * @param typeName the name
         */
        Type(final String typeName) {
            this.typeName = typeName;
        }

        /**
         * @return the type name
         */
        public String getTypeName() {
            return this.typeName;
        }
    }
}
