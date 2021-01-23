/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.attribute.ScriptEvent;

public class ScriptEventListenerBuilder {
    private final ScriptEvent event;
    private final String function;
    private String genericLanguage;
    private String language;

    /**
     * @param event    an event
     * @param function a function
     */
    public ScriptEventListenerBuilder(final ScriptEvent event, final String function) {
        this.event = event;
        this.function = function;
        this.genericLanguage = "ooo:script";
        this.language = ScriptEventListener.BASIC_LANG;
    }

    /**
     * @return the listener
     */
    public ScriptEventListener build() {
        return new ScriptEventListener(this.genericLanguage, this.event, this.function,
                this.language);
    }

    /**
     * Do not confuse with language.
     *
     * @param genericLanguage "ooo:script" for LibreOffice, could be text/javascript in other
     *                        consumers.
     * @return this for fluent style
     */
    public ScriptEventListenerBuilder genericLanguage(final String genericLanguage) {
        this.genericLanguage = genericLanguage;
        return this;
    }

    /**
     * @param language "Basic", "Python", "Java" or "JavaScript" for LO.
     * @return this for fluent style
     */
    public ScriptEventListenerBuilder language(final String language) {
        this.language = language;
        return this;
    }
}
