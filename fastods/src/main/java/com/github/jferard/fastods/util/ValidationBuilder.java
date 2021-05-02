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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.attribute.DisplayList;
import com.github.jferard.fastods.ref.CellRef;

import java.util.Collection;
import java.util.List;

/**
 * A builder for cell content validation.
 */
public class ValidationBuilder {
    private final String name;
    private String condition;
    private boolean allowEmptyCells;
    private CellRef baseCellAddress;
    private DisplayList displayList;
    private ErrorMessage errorMessage;

    public ValidationBuilder(final String name) {
        this.name = name;
        this.allowEmptyCells = true;
        this.displayList = DisplayList.UNSORTED;
        this.errorMessage = ErrorMessage.create();
    }

    /**
     * Do not allow empty cell (allow is the default)
     *
     * @return this for fluent style
     */
    public ValidationBuilder dontAllowEmptyCells() {
        this.allowEmptyCells = false;
        return this;
    }

    /**
     * Change base cell address for formulas
     *
     * @param baseCellAddress the base cell address
     * @return this for fluent style
     */
    public ValidationBuilder baseCellAddress(final CellRef baseCellAddress) {
        this.baseCellAddress = baseCellAddress;
        return this;
    }

    /**
     * Create a condition on a list of allowed values
     *
     * @param allowedValues the list of allowed values
     * @return this for fluent style
     */
    public ValidationBuilder listCondition(final Collection<String> allowedValues) {
        this.condition = "of:cell-content-is-in-list(\"" +
                StringUtil.join("\";\"", allowedValues) + "\")";
        this.baseCellAddress = null;
        return this;
    }

    /**
     * @param displayList the display list mode: none, sort-ascending or unsorted
     * @return this for fluent style
     */
    public ValidationBuilder displayList(final DisplayList displayList) {
        this.displayList = displayList;
        return this;
    }

    /**
     * Set an error message.
     *
     * @param errorMessage the error message
     * @return this for fluent style
     */
    public ValidationBuilder errorMessage(final ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * @return the validation
     */
    public Validation build() {
        return new Validation(this.name, this.condition, this.allowEmptyCells, this.baseCellAddress,
                this.displayList, this.errorMessage);
    }
}
