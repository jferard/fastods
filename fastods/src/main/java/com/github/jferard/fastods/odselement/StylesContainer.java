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
import com.github.jferard.fastods.style.FontFaceContainerStyle;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.ObjectStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Container;

/**
 * A container for styles
 */
public interface StylesContainer extends StylesModeSetter {
    /**
     * Add a child style that mixes the cell style with a data style to the container
     *
     * @param style     the cell style
     * @param dataStyle the data style
     * @return the mixed cell style
     */
    TableCellStyle addChildCellStyle(TableCellStyle style, DataStyle dataStyle);

    /**
     * Add a cell style to the content container and register the font face
     *
     * @param ffcStyle the cell style or the text style
     * @return true if the style was created or updated
     */
    boolean addContentFontFaceContainerStyle(FontFaceContainerStyle ffcStyle);

    /**
     * Add a cell style to the content container and register the font face
     *
     * @param ffcStyle the cell style or the text style
     * @return true if the style was created or updated
     */
    boolean addStylesFontFaceContainerStyle(FontFaceContainerStyle ffcStyle);

    /**
     * Create a new data style into styles container. No duplicate style name is allowed.
     * Must be used if the table-cell style already exists.
     *
     * @param dataStyle the data style to add
     * @return true if the style was added
     */
    boolean addDataStyle(DataStyle dataStyle);

    /**
     * Create a new master page style into styles container. No duplicate style name is allowed.
     *
     * @param masterPageStyle the data style to add
     * @return true if the style was created
     */
    boolean addMasterPageStyle(MasterPageStyle masterPageStyle);

    /**
     * Add the data style taken from a cell style
     *
     * @param style the cell style
     * @return true if the style was created
     */
    boolean addNewDataStyleFromCellStyle(TableCellStyle style);

    /**
     * Add a page layout style
     *
     * @param pageLayoutStyle the style
     * @return true if the style was created or updated
     */
    boolean addPageLayoutStyle(PageLayoutStyle pageLayoutStyle);

    /**
     * Add a page style
     *
     * @param ps the style
     * @return true if the master page style and the style layout where added
     */
    boolean addPageStyle(PageStyle ps);

    /**
     * Add an object style (style:style) to content.xml/automatic-styles
     *
     * @param objectStyle the style
     * @return true if the style was created or updated
     */
    boolean addContentStyle(ObjectStyle objectStyle);

    /**
     * Add an object style to styles.xml/automatic-styles
     *
     * @param objectStyle the style
     * @return true if the style was created or updated
     */
    boolean addStylesStyle(ObjectStyle objectStyle);
}
