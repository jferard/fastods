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

package com.github.jferard.fastods.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A builder for macro libraries
 */
public class MacroLibraryBuilder {
    private String name;
    private boolean readOnly;
    private List<MacroModule> modules;

    /**
     * A new builder
     */
    public MacroLibraryBuilder() {
        this.name = "Standard";
        this.readOnly = false;
        this.modules = new ArrayList<MacroModule>();
    }

    /**
     * @return the library
     */
    public MacroLibrary build() {
        return new MacroLibrary(this.name, this.readOnly, this.modules);
    }

    /**
     * @param name the name of the lib
     * @return this for fluent style
     */
    public MacroLibraryBuilder name(final String name) {
        this.name = name;
        return this;
    }

    /**
     * @return this for fluent style
     */
    public MacroLibraryBuilder readOnly() {
        this.readOnly = true;
        return this;
    }

    /**
     * @param modules the modules
     * @return this for fluent style
     */
    public MacroLibraryBuilder modules(final List<MacroModule> modules) {
        this.modules = modules;
        return this;
    }

    /**
     * Variadic version
     *
     * @param modules the modules
     * @return this for fluent style
     */
    public MacroLibraryBuilder modules(final MacroModule... modules) {
        this.modules = Arrays.asList(modules);
        return this;
    }
}
