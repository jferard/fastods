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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.util.Style;

/**
 * 16.2 <style:style>
 * <p>
 * The ObjectStyle class represents a style:style element.
 * There are four possibilities:
 * * Style category: style of a content or style of a style
 * * Style visibility: hidden or visible
 * <p>
 * Three destinations:
 * * visible: styles.xml/styles
 * * hidden + content: content.xml/automatic-styles
 * * hidden + style: styles.xml/automatic-styles
 *
 * @author Julien Férard
 */
public interface ObjectStyle extends Style {
    /**
     * 19.476 style:family
     *
     * @return the family name of this style
     */
    ObjectStyleFamily getFamily();

    /**
     * The identifier of this style is built on the name (19.498 style:name) and the family
     *
     * @return a unique identifier for this style
     */
    String getKey();
}
