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

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 14.4.2<script:event-listener>
 */
public class ScriptEventListener implements XMLConvertible {
    /**
     * @param eventName the event, e.g dom:load
     * @param function  the function, e.g Standard.Module1.Main
     * @return a new listener
     */
    // TODO: create a builder
    public static ScriptEventListener create(final String eventName, final String function) {
        return new ScriptEventListener("ooo:script", eventName, function, "Basic");
    }

    private final String genericLanguage;
    private final String eventName;
    private final String function;
    private final String language;

    /**
     * @param genericLanguage ooo:script
     * @param eventName       the event, e.g dom:load
     * @param function        the function, e.g Standard.Module1.Main
     * @param language        Basic, Python, ...
     */
    public ScriptEventListener(final String genericLanguage, final String eventName,
                               final String function, final String language) {
        this.genericLanguage = genericLanguage;
        this.eventName = eventName;
        this.function = function;
        this.language = language;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<script:event-listener");
        util.appendAttribute(appendable, "script:language", this.genericLanguage);
        util.appendAttribute(appendable, "script:event-name", this.eventName);
        util.appendAttribute(appendable, "xlink:href",
                "vnd.sun.star.script:" + this.function + "?" + "language=" + this.language +
                        "&amp;location=document");
        util.appendAttribute(appendable, "xlink:type", "simple");
        appendable.append("/>");
    }
}
