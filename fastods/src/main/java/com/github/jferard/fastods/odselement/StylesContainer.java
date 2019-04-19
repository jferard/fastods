/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.datastyle.DataStyle;
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

/**
 * content.xml/office:document-content
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class StylesContainer {

    public static final FontFace DEFAULT_FONT_FACE = new FontFace("Liberation Sans");

    /** A cell style, child of a table cell style and a data style/
     * This class is a key for a Map */
    private static class ChildCellStyle {
        private final TableCellStyle style;
        private final DataStyle dataStyle;

        ChildCellStyle(final TableCellStyle style, final DataStyle dataStyle) {
            this.style = style;
            this.dataStyle = dataStyle;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            else if (!(o instanceof ChildCellStyle)) return false;
            else {
                final ChildCellStyle other = (ChildCellStyle) o;
                return this.style.getKey().equals(other.style.getKey()) &&
                        this.dataStyle.getName().equals(other.dataStyle.getName());
            }
        }

        @Override
        public int hashCode() {
            return this.style.getKey().hashCode() * 31 + this.dataStyle.getName().hashCode();

        }
    }

    /**
     * A register of existing anonymous styles. Won't be added to document.
     */
    private final Map<ChildCellStyle, TableCellStyle> anonymousStyleByChildCellStyle;

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
     */
    StylesContainer() {
        this.objectStylesContainer = new MultiContainer<String, Dest, ObjectStyle>(Dest.class);
        this.dataStylesContainer = new MultiContainer<String, Dest, DataStyle>(Dest.class);
        this.masterPageStylesContainer = new Container<String, MasterPageStyle>();
        this.pageLayoutStylesContainer = new Container<String, PageLayoutStyle>();
        this.anonymousStyleByChildCellStyle = new HashMap<ChildCellStyle, TableCellStyle>();
        this.fontFaces = new HashSet<FontFace>();
        this.fontFaces.add(DEFAULT_FONT_FACE);
    }

    /**
     * Add a child style that mixes the cell style with a data style to the container
     *
     * @param style     the cell style
     * @param dataStyle the data style
     * @return the mixed cell style
     */
    public TableCellStyle addChildCellStyle(final TableCellStyle style, final DataStyle dataStyle) {
        final ChildCellStyle childKey = new ChildCellStyle(style, dataStyle);
        TableCellStyle anonymousStyle = this.anonymousStyleByChildCellStyle.get(childKey);
        if (anonymousStyle == null) {
            this.addDataStyle(dataStyle);
            if (!style.hasParent()) { // here, the style may already be a child style
                this.addContentFontFaceContainerStyle(style);
            }
            final String name = style.getRealName() + "-_-" + dataStyle.getName();
            final TableCellStyleBuilder anonymousStyleBuilder = TableCellStyle.builder(name)
                    .parentCellStyle(style).dataStyle(dataStyle);
            if (dataStyle.isHidden()) {
                anonymousStyleBuilder.hidden();
            }
            anonymousStyle = anonymousStyleBuilder.build();
            this.addContentFontFaceContainerStyle(anonymousStyle);
            this.anonymousStyleByChildCellStyle.put(childKey, anonymousStyle);
        }
        return anonymousStyle;
    }

    /**
     * Add a cell style to the content container and register the font face
     * @param ffcStyle the cell style or the text style
     * @return true if the style was created or updated
     */
    public boolean addContentFontFaceContainerStyle(final FontFaceContainerStyle ffcStyle) {
        final FontFace fontFace = ffcStyle.getFontFace();
        if (fontFace != null) {
            this.fontFaces.add(fontFace);
        }
        return this.addContentStyle(ffcStyle);
    }

    /**
     * Add a cell style to the content container and register the font face
     * @param ffcStyle the cell style or the text style
     * @return true if the style was created or updated
     */
    public boolean addStylesFontFaceContainerStyle(final FontFaceContainerStyle ffcStyle) {
        final FontFace fontFace = ffcStyle.getFontFace();
        if (fontFace != null) {
            this.fontFaces.add(fontFace);
        }
        return this.addStylesStyle(ffcStyle);
    }

    /**
     * Create a new data style into styles container. No duplicate style name is allowed.
     * Must be used if the table-cell style already exists.
     *
     * @param dataStyle the data style to add
     * @return true if the style was added
     */
    public boolean addDataStyle(final DataStyle dataStyle) {
        if (dataStyle.isHidden()) {
            return this.dataStylesContainer
                    .add(dataStyle.getName(), Dest.CONTENT_AUTOMATIC_STYLES, dataStyle);
        } else {
            return this.dataStylesContainer
                    .add(dataStyle.getName(), Dest.STYLES_COMMON_STYLES, dataStyle);
        }
    }

    /**
     * Set the new mode to use for data styles
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    public void setDataStylesMode(final Mode mode) {
        this.dataStylesContainer.setMode(mode);
    }

    /**
     * Create a new master page style into styles container. No duplicate style name is allowed.
     *
     * @param masterPageStyle the data style to add
     * @return true if the style was created
     */
    public boolean addMasterPageStyle(final MasterPageStyle masterPageStyle) {
        if (this.masterPageStylesContainer.add(masterPageStyle.getName(), masterPageStyle)) {
            masterPageStyle.addEmbeddedStyles(this);
            return true;
        } else return false;
    }

    /**
     * Set the new mode to use for master page style
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    public void setMasterPageStyleMode(final Mode mode) {
        this.masterPageStylesContainer.setMode(mode);
    }

    /**
     * Add the data style taken from a cell style
     *
     * @param style the cell style
     * @return true if the style was created
     */
    public boolean addNewDataStyleFromCellStyle(final TableCellStyle style) {
        final boolean ret = this.addContentStyle(style);
        return this.addDataStyle(style.getDataStyle()) && ret;
    }

    /**
     * Set the new mode to use for page layout style
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    public void setPageLayoutStyleMode(final Mode mode) {
        this.pageLayoutStylesContainer.setMode(mode);
    }


    /**
     * Add a page layout style
     *
     * @param pageLayoutStyle the style
     * @return true if the style was created or updated
     */
    public boolean addPageLayoutStyle(final PageLayoutStyle pageLayoutStyle) {
        return this.pageLayoutStylesContainer.add(pageLayoutStyle.getName(), pageLayoutStyle);
    }

    /**
     * Set the new mode to use for page style
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    public void setPageStyleMode(final Mode mode) {
        this.setMasterPageStyleMode(mode);
        this.setPageLayoutStyleMode(mode);
    }


    /**
     * Add a page style
     *
     * @param ps the style
     * @return true if the master page style and the style layout where added
     */
    public boolean addPageStyle(final PageStyle ps) {
        boolean ret = this.addMasterPageStyle(ps.getMasterPageStyle());
        ret = this.addPageLayoutStyle(ps.getPageLayoutStyle()) && ret;
        return ret;
    }

    /**
     * Set the new mode to use for styles in content.xml/automatic-styles, styles.xml/styles and
     * styles.xml/automatic-styles
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    public void setObjectStyleMode(final Mode mode) {
        this.objectStylesContainer.setMode(mode);
    }

    /**
     * Add an object style (style:style) to content.xml/automatic-styles
     *
     * @param objectStyle the style
     * @return true if the style was created or updated
     */
    public boolean addContentStyle(final ObjectStyle objectStyle) {
        if (objectStyle.isHidden()) {
            return this.objectStylesContainer
                    .add(objectStyle.getKey(), Dest.CONTENT_AUTOMATIC_STYLES, objectStyle);
        } else {
            return this.objectStylesContainer
                    .add(objectStyle.getKey(), Dest.STYLES_COMMON_STYLES, objectStyle);
        }
    }

    /**
     * Add an object style to styles.xml/automatic-styles
     *
     * @param objectStyle the style
     * @return true if the style was created or updated
     */
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
            if (hasHeader && hasFooter) break;
            hasHeader = hasHeader || ps.hasHeader();
            hasFooter = hasFooter || ps.hasFooter();
        }
        return new HasFooterHeader(hasHeader, hasFooter);
    }

    private void write(final Iterable<ObjectStyle> iterable, final XMLUtil util,
                       final Appendable appendable) throws IOException {
        for (final ObjectStyle os : iterable)
            os.appendXMLContent(util, appendable);
    }

    /**
     * Write the various styles in the automatic styles.
     *
     * @param util   an XML util
     * @param appendable the destination
     * @throws IOException if the styles can't be written
     */
    public void writeContentAutomaticStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        final Iterable<ObjectStyle> styles = this.objectStylesContainer
                .getValues(Dest.CONTENT_AUTOMATIC_STYLES);
        for (final ObjectStyle style : styles)
            assert style.isHidden() : style.toString();

        this.write(styles, util, appendable);
    }

    /**
     * Write the data styles in the automatic-styles. They belong to content.xml/automatic-styles
     *
     * @param util   an XML util
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
     * @param util   an util
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
     * @param util   an util
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
     * @param util   an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void writeStylesAutomaticStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        final Iterable<ObjectStyle> styles = this.objectStylesContainer
                .getValues(Dest.STYLES_AUTOMATIC_STYLES);
        for (final ObjectStyle style : styles)
            assert style.isHidden() : style.toString();

        this.write(styles, util, appendable);
    }

    /**
     * Write styles to styles.xml/common-styles
     *
     * @param util   an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void writeStylesCommonStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        final Iterable<ObjectStyle> styles = this.objectStylesContainer
                .getValues(Dest.STYLES_COMMON_STYLES);
        for (final ObjectStyle style : styles)
            assert !style.isHidden() : style.toString() + " - " + style.getName() +
                    TableCellStyle.DEFAULT_CELL_STYLE.toString();

        this.write(styles, util, appendable);
    }

    /**
     * Write data styles to styles.xml/common-styles
     *
     * @param util   an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void writeVisibleDataStyles(final XMLUtil util, final Appendable appendable)
            throws IOException {
        final Iterable<DataStyle> dataStyles = this.dataStylesContainer
                .getValues(Dest.STYLES_COMMON_STYLES);
        for (final DataStyle dataStyle : dataStyles) {
            assert !dataStyle.isHidden() : dataStyle.toString() + " - " + dataStyle.getName() +
                    TableCellStyle.DEFAULT_CELL_STYLE.toString();

            dataStyle.appendXMLContent(util, appendable);
        }
    }

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
        CONTENT_AUTOMATIC_STYLES, /**
         * styles.xml/automatic-styles
         */
        STYLES_AUTOMATIC_STYLES, /**
         * styles.xml/common-styles
         */
        STYLES_COMMON_STYLES,
    }
}
