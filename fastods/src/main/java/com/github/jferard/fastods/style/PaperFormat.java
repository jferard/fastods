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

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.attribute.SimpleLength;

/**
 * Format of the paper
 *
 * @author Julien Férard
 */
public enum PaperFormat {
    /**
     * 420 mm × 297 mm
     */
    A3(Defaults.A3_H, Defaults.A3_W),
    /**
     * 297 mm × 210 mm
     */
    A4(Defaults.A3_W, Defaults.A4_W),
    /**
     * 210 mm × 148 mm
     */
    A5(Defaults.A4_W, Defaults.A5_W),
    /**
     * 14 in × 8½ in
     */
    LEGAL(Defaults.LEGAL_H, Defaults.LETTER_W),
    /**
     * 11 in × 8½ in
     */
    LETTER(Defaults.LETTER_H, Defaults.LETTER_W),
    /**
     * user defined: 0 by convention.
     */
    USER(Length.NULL_LENGTH, Length.NULL_LENGTH);

    private static class Defaults {
        static final Length A3_H = SimpleLength.cm(42.0);
        static final Length A3_W = SimpleLength.cm(29.7);
        static final Length A4_W = SimpleLength.cm(21.0);
        static final Length A5_W = SimpleLength.cm(14.8);
        static final Length LEGAL_H = SimpleLength.cm(35.57);
        static final Length LETTER_H = SimpleLength.cm(27.94);
        static final Length LETTER_W = SimpleLength.cm(21.59);
    }
    private final Length height;
    private final Length width;

    PaperFormat(final Length height, final Length width) {
        this.height = height;
        this.width = width;
    }

    /**
     * @return the height of the paper
     */
    Length getHeight() {
        return this.height;
    }

    /**
     * @return the width of the paper
     */
    Length getWidth() {
        return this.width;
    }

}
