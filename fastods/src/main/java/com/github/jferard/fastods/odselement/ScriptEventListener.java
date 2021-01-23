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

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.attribute.ScriptEvent;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 14.4.2 script:event-listener
 *
 * @author J. Férard
 */
public class ScriptEventListener implements XMLConvertible {
    /**
     * For scripts in Basic
     */
    public static final String BASIC_LANG = "Basic";

    /**
     * For scripts in Python
     */
    public static final String PYTHON_LANG = "Python";

    /**
     * For scripts in Java
     */
    public static final String JAVA_LANG = "Java";

    /**
     * For scripts in JavaScript
     */
    public static final String JAVASCRIPT_LANG = "JavaScript";

    /**
     * @param event        the event, e.g dom:load
     * @param functionName the function, e.g Standard.Module1.Main
     * @return a new listener
     */
    public static ScriptEventListener create(final ScriptEvent event, final String functionName) {
        return new ScriptEventListener("ooo:script", event, functionName, BASIC_LANG);
    }

    /**
     * @param event        the event, e.g dom:load
     * @param functionName the function, e.g Standard.Module1.Main
     * @return a new listener
     */
    public static ScriptEventListenerBuilder builder(final ScriptEvent event,
                                                     final String functionName) {
        return new ScriptEventListenerBuilder(event, functionName);
    }

    private final String genericLanguage;
    private final ScriptEvent event;
    private final String functionName;
    private final String language;

    /**
     * @param genericLanguage ooo:script
     * @param event           the event, e.g dom:load
     * @param functionName    the function, e.g Standard.Module1.Main
     * @param language        Basic, Python, ...
     */
    public ScriptEventListener(final String genericLanguage, final ScriptEvent event,
                               final String functionName, final String language) {
        this.genericLanguage = genericLanguage;
        this.event = event;
        this.functionName = functionName;
        this.language = language;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<script:event-listener");
        util.appendAttribute(appendable, "script:language", this.genericLanguage);
        util.appendAttribute(appendable, "script:event-name", this.event);
        util.appendAttribute(appendable, "xlink:href",
                "vnd.sun.star.script:" + this.functionName + "?" + "language=" + this.language +
                        "&amp;location=document");
        util.appendAttribute(appendable, "xlink:type", "simple");
        appendable.append("/>");
    }
}
