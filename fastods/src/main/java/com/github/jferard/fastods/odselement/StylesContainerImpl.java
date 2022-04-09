/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.attribute.CellAlign;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.TextDataStyle;
import com.github.jferard.fastods.style.FontFace;
import com.github.jferard.fastods.style.FontFaceContainerStyle;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.ObjectStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableCellStyleBuilder;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.MultiContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * content.xml/office:document-content
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class StylesContainerImpl implements StylesContainer {
    private static final FontFace DEFAULT_FONT_FACE = new FontFace("Liberation Sans");
    /**
     * A register of existing anonymous styles. Won't be added to document.
     */
    private final Map<ChildCellStyleKey, TableCellStyle> anonymousStyleByChildCellStyle;
    /**
     * Data style that will be written in content.xml > automatic-styles
     * Those styles should be hidden
     */
    private final MultiContainer<String, Dest, DataStyle> dataStylesContainer;
    private final Container<String, MasterPageStyle> masterPageStylesContainer;
    /**
     * Should be hidden.
     */
    private final Container<String, PageLayoutStyle> pageLayoutStylesContainer;
    private final MultiContainer<String, Dest, ObjectStyle> objectStylesContainer;
    private final Set<FontFace> fontFaces;
    /**
     * Create a styles container
     *
     * @param logger the logger
     */
    StylesContainerImpl(final Logger logger) {
        this.objectStylesContainer =
                new MultiContainer<>(logger, Dest.class);
        this.dataStylesContainer = new MultiContainer<>(logger, Dest.class);
        this.masterPageStylesContainer = new Container<>(logger);
        this.pageLayoutStylesContainer = new Container<>(logger);
        this.anonymousStyleByChildCellStyle = new HashMap<>();
        this.fontFaces = new HashSet<>();
        this.fontFaces.add(DEFAULT_FONT_FACE);
    }

    @Override
    public TableCellStyle addChildCellStyle(final TableCellStyle style, final DataStyle dataStyle) {
        final ChildCellStyleKey childKey = new ChildCellStyleKey(style, dataStyle);
        TableCellStyle anonymousStyle = this.anonymousStyleByChildCellStyle.get(childKey);
        if (anonymousStyle == null) {
            this.addDataStyle(dataStyle);
            if (!style.hasParent()) { // here, the style may already be a child style
                this.addContentFontFaceContainerStyle(style);
            }
            final String name = style.getRealName() + "-_-" + dataStyle.getName();
            final TableCellStyleBuilder anonymousStyleBuilder =
                    TableCellStyle.builder(name).parentCellStyle(style).dataStyle(dataStyle);
            if (dataStyle.isHidden()) {
                anonymousStyleBuilder.hidden();
            }
            if (!(style.hasTextAlign() || dataStyle instanceof TextDataStyle)) {
                anonymousStyleBuilder.textAlign(CellAlign.RIGHT);
            }
            anonymousStyle = anonymousStyleBuilder.build();
            this.addContentFontFaceContainerStyle(anonymousStyle);
            this.anonymousStyleByChildCellStyle.put(childKey, anonymousStyle);
        }
        return anonymousStyle;
    }

    @Override
    public boolean addContentFontFaceContainerStyle(final FontFaceContainerStyle ffcStyle) {
        final FontFace fontFace = ffcStyle.getFontFace();
        if (fontFace != null) {
            this.fontFaces.add(fontFace);
        }
        return this.addContentStyle(ffcStyle);
    }

    @Override
    public boolean addStylesFontFaceContainerStyle(final FontFaceContainerStyle ffcStyle) {
        final FontFace fontFace = ffcStyle.getFontFace();
        if (fontFace != null) {
            this.fontFaces.add(fontFace);
        }
        return this.addStylesStyle(ffcStyle);
    }

    @Override
    public boolean addDataStyle(final DataStyle dataStyle) {
        if (dataStyle.isHidden()) {
            return this.dataStylesContainer
                    .add(dataStyle.getName(), Dest.CONTENT_AUTOMATIC_STYLES, dataStyle);
        } else {
            return this.dataStylesContainer
                    .add(dataStyle.getName(), Dest.STYLES_COMMON_STYLES, dataStyle);
        }
    }

    @Override
    public void setDataStylesMode(final Mode mode) {
        this.dataStylesContainer.setMode(mode);
    }

    @Override
    public boolean addMasterPageStyle(final MasterPageStyle masterPageStyle) {
        if (this.masterPageStylesContainer.add(masterPageStyle.getName(), masterPageStyle)) {
            masterPageStyle.addEmbeddedStyles(this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setMasterPageStyleMode(final Mode mode) {
        this.masterPageStylesContainer.setMode(mode);
    }

    @Override
    public boolean addNewDataStyleFromCellStyle(final TableCellStyle style) {
        final boolean ret = this.addContentStyle(style);
        return this.addDataStyle(style.getDataStyle()) && ret;
    }

    @Override
    public void setPageLayoutStyleMode(final Mode mode) {
        this.pageLayoutStylesContainer.setMode(mode);
    }

    @Override
    public boolean addPageLayoutStyle(final PageLayoutStyle pageLayoutStyle) {
        return this.pageLayoutStylesContainer.add(pageLayoutStyle.getName(), pageLayoutStyle);
    }

    @Override
    public void setPageStyleMode(final Mode mode) {
        this.setMasterPageStyleMode(mode);
        this.setPageLayoutStyleMode(mode);
    }

    @Override
    public boolean addPageStyle(final PageStyle ps) {
        boolean ret = this.addMasterPageStyle(ps.getMasterPageStyle());
        ret = this.addPageLayoutStyle(ps.getPageLayoutStyle()) && ret;
        return ret;
    }

    @Override
    public void setObjectStyleMode(final Mode mode) {
        this.objectStylesContainer.setMode(mode);
    }

    @Override
    public boolean addContentStyle(final ObjectStyle objectStyle) {
        if (objectStyle.isHidden()) {
            return this.objectStylesContainer
                    .add(objectStyle.getKey(), Dest.CONTENT_AUTOMATIC_STYLES, objectStyle);
        } else {
            return this.objectStylesContainer
                    .add(objectStyle.getKey(), Dest.STYLES_COMMON_STYLES, objectStyle);
        }
    }

    @Override
    public boolean addStylesStyle(final ObjectStyle objectStyle) {
        if (objectStyle.isHidden()) {
            return this.objectStylesContainer
                    .add(objectStyle.getKey(), Dest.STYLES_AUTOMATIC_STYLES, objectStyle);
        } else {
            return this.objectStylesContainer
                    .add(objectStyle.getKey(), Dest.STYLES_COMMON_STYLES, objectStyle);
        }
    }

    /**
     * Enable debug mode
     */
    public void debug() {
        this.objectStylesContainer.debug();
        this.dataStylesContainer.debug();
        this.masterPageStylesContainer.debug();
        this.pageLayoutStylesContainer.debug();
    }

    /**
     * Freeze the container: no more add is allowed
     */
    public void freeze() {
        this.objectStylesContainer.freeze();
        this.dataStylesContainer.freeze();
        this.masterPageStylesContainer.freeze();
        this.pageLayoutStylesContainer.freeze();
    }

    /**
     * @return a "double boolean"
     */
    public HasFooterHeader hasFooterHeader() {
        boolean hasHeader = false;
        boolean hasFooter = false;

        for (final MasterPageStyle ps : this.masterPageStylesContainer.getValues()) {
            if (hasHeader && hasFooter) {
                break;
            }
            hasHeader = hasHeader || ps.hasHeader();
            hasFooter = hasFooter || ps.hasFooter();
        }
        return new HasFooterHeader(hasHeader, hasFooter);
    }

    private void write(final Iterable<ObjectStyle> iterable, final XMLUtil util,
                       final Appendable appendable) throws IOException {
        for (final ObjectStyle os : iterable) {
            os.appendXMLContent(util, appendable);
        }
    }

    /**
     * Write the various styles in the automatic styles.
     *
     * @param util       an XML util
     * @param appendable the destination
     * @throws IOException if the styles can't be written
     */
    public void writeContentAutomaticStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        final Iterable<ObjectStyle> styles =
                this.objectStylesContainer.getValues(Dest.CONTENT_AUTOMATIC_STYLES);
        for (final ObjectStyle style : styles) {
            assert style.isHidden() : style.toString();
        }

        this.write(styles, util, appendable);
    }

    /**
     * Write the data styles in the automatic-styles. They belong to content.xml/automatic-styles
     *
     * @param util       an XML util
     * @param appendable the destination
     * @throws IOException if the styles can't be written
     */
    public void writeHiddenDataStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        for (final DataStyle dataStyle : this.dataStylesContainer
                .getValues(Dest.CONTENT_AUTOMATIC_STYLES)) {
            dataStyle.appendXMLContent(util, appendable);
        }
    }

    /**
     * Write the page layout styles. The page layout will always belong to to styles
     * .xml/automatic-styles, since
     * it's an automatic style (see 16.5) and it is not "used in a document" (3.1.3.2)
     *
     * @param util       an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void writePageLayoutStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        for (final PageLayoutStyle ps : this.pageLayoutStylesContainer.getValues()) {
            assert ps.isHidden();
            ps.appendXMLToAutomaticStyle(util, appendable);
        }
    }

    /**
     * Write master page styles. The master page style always belong to to styles
     * .xml/master-styles (3.15.4)
     *
     * @param util       an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void writeMasterPageStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        for (final MasterPageStyle ps : this.masterPageStylesContainer.getValues()) {
            ps.appendXMLToMasterStyle(util, appendable);
        }
    }

    /**
     * Write styles to styles.xml/automatic-styles
     *
     * @param util       an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void writeStylesAutomaticStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        final Iterable<ObjectStyle> styles =
                this.objectStylesContainer.getValues(Dest.STYLES_AUTOMATIC_STYLES);
        for (final ObjectStyle style : styles) {
            assert style.isHidden() : style.toString();
        }

        this.write(styles, util, appendable);
    }

    /**
     * Write styles to styles.xml/common-styles
     *
     * @param util       an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void writeStylesCommonStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        final Iterable<ObjectStyle> styles =
                this.objectStylesContainer.getValues(Dest.STYLES_COMMON_STYLES);
        for (final ObjectStyle style : styles) {
            assert !style.isHidden() : style.toString() + " - " + style.getName() +
                    TableCellStyle.DEFAULT_CELL_STYLE.toString();
        }

        this.write(styles, util, appendable);
    }

    /**
     * Write data styles to styles.xml/common-styles
     *
     * @param util       an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void writeVisibleDataStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        final Iterable<DataStyle> dataStyles =
                this.dataStylesContainer.getValues(Dest.STYLES_COMMON_STYLES);
        for (final DataStyle dataStyle : dataStyles) {
            assert !dataStyle.isHidden() : dataStyle.toString() + " - " + dataStyle.getName() +
                    TableCellStyle.DEFAULT_CELL_STYLE.toString();

            dataStyle.appendXMLContent(util, appendable);
        }
    }

    /**
     * @param util       the util to write XML
     * @param appendable where to write
     * @throws IOException if the font face declarations were not written
     */
    public void writeFontFaceDecls(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<office:font-face-decls>");
        for (final FontFace fontFace : this.fontFaces) {
            fontFace.appendXMLContent(util, appendable);
        }
        appendable.append("</office:font-face-decls>");
    }

    /**
     * A destination. This is the key of the multi container
     */
    public enum Dest {
        /**
         * content.xml/automatic-styles
         */
        CONTENT_AUTOMATIC_STYLES,
        /**
         * styles.xml/automatic-styles
         */
        STYLES_AUTOMATIC_STYLES,
        /**
         * styles.xml/common-styles
         */
        STYLES_COMMON_STYLES,
    }

    /**
     * A cell style, child of a table cell style and a data style/
     * This class is a key for a Map
     */
    static class ChildCellStyleKey {
        private final TableCellStyle style;
        private final DataStyle dataStyle;

        ChildCellStyleKey(final TableCellStyle style, final DataStyle dataStyle) {
            this.style = style;
            this.dataStyle = dataStyle;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ChildCellStyleKey)) {
                return false;
            }
            final ChildCellStyleKey other = (ChildCellStyleKey) o;
            return this.style.getKey().equals(other.style.getKey()) &&
                    this.dataStyle.getName().equals(other.dataStyle.getName());

        }

        @Override
        public int hashCode() {
            return this.style.getKey().hashCode() * 31 + this.dataStyle.getName().hashCode();

        }
    }
}
