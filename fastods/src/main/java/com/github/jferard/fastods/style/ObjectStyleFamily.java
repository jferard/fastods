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

package com.github.jferard.fastods.style;

/**
 * 19.476 style:family
 *
 * @author Julien Férard
 */
public enum ObjectStyleFamily {
    /**
     * "paragraph: family name of styles for paragraphs"
     */
    PARAGRAPH("paragraph"),

    /**
     * "table: family name of styles for tables."
     */
    TABLE("table"),

    /**
     * "table-cell: family name of styles for table cells."
     */
    TABLE_CELL("table-cell"),

    /**
     * "table-column: family name of styles for table columns."
     */
    TABLE_COLUMN("table-column"),

    /**
     * "table-row: family name of styles for table rows."
     */
    TABLE_ROW("table-row"),

    /**
     * "text: family name of styles for text."
     */
    TEXT("text"),

    /**
     * "graphic: family name of styles for graphics."
     */
    GRAPHIC("graphic"),

    /**
     * "draw:fill-bitmap: family name of styles for fill images."
     */
    DRAW_FILL_BITMAP("draw:fill-bitmap"),

    /**
     * "draw:gradient: family name of styles for gradients."
     */
    DRAW_FILL_GRADIENT("draw:fill-gradient"),

    /**
     * "draw:hatch: family name of styles for hatch."
     */
    DRAW_FILL_HATCH("draw:fill-hatch");

    private final String attributeName;

    /**
     * Create a new family
     *
     * @param attributeName the attribute
     */
    ObjectStyleFamily(final String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * @return the name of the family
     */
    public String getName() {
        return this.attributeName;
    }
}
