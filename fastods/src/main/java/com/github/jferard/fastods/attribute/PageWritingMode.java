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

package com.github.jferard.fastods.attribute;

/**
 * 20.394 style:writing-mode
 * see See §7.27.7 of [XSL] (https://www.w3.org/TR/2001/REC-xsl-20011015/slice7
 * .html#writing-mode-related)
 */
public enum PageWritingMode implements AttributeValue {
    /**
     * "Shorthand for lr-tb"
     */
    LR("lr"),
    /**
     * left to right then top to bottom
     */
    LRTB("lr-tb"),
    /**
     * page means inherit
     */
    PAGE("page"),
    /**
     * "Shorthand for rl-tb"
     */
    RL("rl"),
    /**
     * right to left then top to bottom
     */
    RLTB("rl-tb"),
    /**
     * "Shorthand for tb-rl"
     */
    TB("tb"),
    /**
     * top to bottom then left to right
     */
    TBLR("tb-lr"),
    /**
     * top to bottom then right to left
     */
    TBRL("tb-rl");

    private final String attrValue;

    /**
     * @param attrValue the value See §7.27.7 of [XSL]
     */
    PageWritingMode(final String attrValue) {
        this.attrValue = attrValue;
    }

    /**
     * @return the value See §7.27.7 of [XSL]
     */
    @Override
    public String getValue() {
        return this.attrValue;
    }

}
