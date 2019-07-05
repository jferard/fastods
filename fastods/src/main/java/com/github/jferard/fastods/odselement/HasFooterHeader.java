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

/**
 * A "double boolean" used by the styles.xml
 *
 * @author Julien Férard
 */
class HasFooterHeader {
    private final boolean hasFooter;
    private final boolean hasHeader;

    /**
     * Create a new "double boolean"
     *
     * @param hasHeader true if has header
     * @param hasFooter true if has footer
     */
    HasFooterHeader(final boolean hasHeader, final boolean hasFooter) {
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
    }

    /**
     * @return true if has footer
     */
    public boolean hasFooter() {
        return this.hasFooter;
    }

    /**
     * @return true if has header
     */
    public boolean hasHeader() {
        return this.hasHeader;
    }
}
