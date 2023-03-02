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

package com.github.jferard.fastods.util;

import java.util.ArrayList;
import java.util.List;

public class PilotTableBuilder {
    private final String name;
    private final String sourceCellRange;
    private final boolean showFilterButton;
    private final boolean drillDownOnDoubleClick;
    private final String targetRange;
    private final List<String> buttons;
    private final List<PilotTableField> fields;

    public PilotTableBuilder(final String name, final String sourceCellRange,
                             final String targetRange, final List<String> buttons) {
        this.name = name;
        this.sourceCellRange = sourceCellRange;
        this.targetRange = targetRange;
        this.buttons = buttons;
        this.showFilterButton = true;
        this.drillDownOnDoubleClick = false;
        this.fields = new ArrayList<PilotTableField>();
    }

    public PilotTable build() {
        return new PilotTable(this.name, this.sourceCellRange, this.targetRange, this.buttons,
                this.fields, this.showFilterButton, this.drillDownOnDoubleClick);
    }

    public PilotTableBuilder field(final PilotTableField field) {
        this.fields.add(field);
        return this;
    }
}
