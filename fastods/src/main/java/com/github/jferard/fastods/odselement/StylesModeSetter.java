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

import com.github.jferard.fastods.util.Container;

public interface StylesModeSetter {
    /**
     * Set the new mode to use for data styles
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    void setDataStylesMode(Container.Mode mode);

    /**
     * Set the new mode to use for master page style
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    void setMasterPageStyleMode(Container.Mode mode);

    /**
     * Set the new mode to use for page layout style
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    void setPageLayoutStyleMode(Container.Mode mode);

    /**
     * Set the new mode to use for page style
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    void setPageStyleMode(Container.Mode mode);

    /**
     * Set the new mode to use for styles in content.xml/automatic-styles, styles.xml/styles and
     * styles.xml/automatic-styles
     *
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
    void setObjectStyleMode(Container.Mode mode);
}
