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

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.attribute.MessageType;

import java.io.IOException;

/**
 * 9.4.7 <table:error-message>
 */
public class ErrorMessage implements XMLConvertible {
    /**
     * Create a new default ErrorMessage.
     *
     * @return the message object
     */
    public static ErrorMessage create() {
        return new ErrorMessage(false, MessageType.STOP, null);
    }

    private final boolean display;
    private final MessageType messageType;
    private final String title;

    /**
     * @param display whether to display or not this message
     * @param messageType the type of the message
     * @param title the title of the message.
     */
    public ErrorMessage(final boolean display, final MessageType messageType, final String title) {
        this.display = display;
        this.messageType = messageType;
        this.title = title;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:error-message");
        if (this.display) {
            util.appendAttribute(appendable, "table:display", true);
        }
        if (this.messageType != MessageType.STOP) {
            util.appendAttribute(appendable, "table:display", this.display);
        }
        if (this.title != null) {
            util.appendAttribute(appendable, "table:display", this.title);
        }
        appendable.append("/>");
    }
}
