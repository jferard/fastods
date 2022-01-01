/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.style;

/**
 * All the information is taken from the 2019-04-19 version of: https://wiki.documentfoundation
 * .org/Fonts (under
 * Creative Commons Attribution-ShareAlike 3.0 Unported License,
 * https://creativecommons.org/licenses/by-sa/3.0/).
 *
 * @author J. Férard
 */
public final class LOFonts {
    /**
     * Font Caladea
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin</li>
     * <li>Reasons for inclusion: Metrically compatible with Cambria</li>
     * <li>Added in: LO 4.4</li>
     * </ul>
     */
    public static final String CALADEA = "Caladea";

    /**
     * Font Carlito
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Sans</li>
     * <li>Coverage: Latin, Cyrillic, Greek</li>
     * <li>Reasons for inclusion: Metrically compatible with Calibri</li>
     * <li>Added in: LO 4.4</li>
     * </ul>
     */
    public static final String CARLITO = "Carlito";

    /**
     * Font DejaVu Sans
     * <ul>
     * <li>Variants/styles/subfamilies: Book, Bold, Italic, Bold Italic, Extralight</li>
     * <li>Type: Sans</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew, Arabic, Armenian, Lao, Georgian, Lisu</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String DEJAVU_SANS = "DejaVu Sans";

    /**
     * Font DejaVu Sans Condensed
     * <ul>
     * <li>Variants/styles/subfamilies: Book, Bold, Italic, Bold Italic</li>
     * <li>Type: Sans</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew, Arabic, Armenian, Lao, Georgian, Lisu</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String DEJAVU_SANS_CONDENSED = "DejaVu Sans Condensed";

    /**
     * Font DejaVu Sans Mono
     * <ul>
     * <li>Variants/styles/subfamilies: Book, Bold, Italic, Bold Italic</li>
     * <li>Type: Mono</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew, Arabic, Armenian, Lao, Georgian, Lisu</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String DEJAVU_SANS_MONO = "DejaVu Sans Mono";

    /**
     * Font DejaVu Serif
     * <ul>
     * <li>Variants/styles/subfamilies: Book, Bold, Italic, Bold Italic</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew, Armenian, Lao, Georgian, Lisu</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String DEJAVU_SERIF = "DejaVu Serif";

    /**
     * Font DejaVu Serif Condensed
     * <ul>
     * <li>Variants/styles/subfamilies: Book, Bold, Italic, Bold Italic</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew, Armenian, Lao, Georgian, Lisu</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String DEJAVU_SERIF_CONDENSED = "DejaVu Serif Condensed";

    /**
     * Font Gentium Basic
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin, Cyrillic, Greek</li>
     * <li>Added in: OOo 3.2</li>
     * </ul>
     */
    public static final String GENTIUM_BASIC = "Gentium Basic";

    /**
     * Font Gentium Book Basic
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin, Cyrillic, Greek</li>
     * <li>Added in: OOo 3.2</li>
     * </ul>
     */
    public static final String GENTIUM_BOOK_BASIC = "Gentium Book Basic";

    /**
     * Font Liberation Mono
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Mono</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew</li>
     * <li>Reasons for inclusion: Metrically compatible with Courier New</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String LIBERATION_MONO = "Liberation Mono";

    /**
     * Font Liberation Sans
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Sans</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew</li>
     * <li>Reasons for inclusion: Metrically compatible with Arial</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String LIBERATION_SANS = "Liberation Sans";

    /**
     * Font Liberation Sans Narrow
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Sans</li>
     * <li>Coverage: Latin, Cyrillic, Greek</li>
     * <li>Reasons for inclusion: Metrically compatible with Arial Narrow</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String LIBERATION_SANS_NARROW = "Liberation Sans Narrow";

    /**
     * Font Liberation Serif
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew</li>
     * <li>Reasons for inclusion: Metrically compatible with Times New Roman</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String LIBERATION_SERIF = "Liberation Serif";

    /**
     * Font Linux Biolinum G
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew</li>
     * <li>Added in: LO 3.3</li>
     * </ul>
     */
    public static final String LINUX_BIOLINUM_G = "Linux Biolinum G";

    /**
     * Font Linux Libertine Display G
     * <ul>
     * <li>Variants/styles/subfamilies: Regular</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew</li>
     * <li>Added in: LO 3.3</li>
     * </ul>
     */
    public static final String LINUX_LIBERTINE_DISPLAY_G = "Linux Libertine Display G";

    /**
     * Font Linux Libertine G
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic, Semibold, Semibold
     * Italic</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Hebrew</li>
     * <li>Added in: LO 3.3</li>
     * </ul>
     */
    public static final String LINUX_LIBERTINE_G = "Linux Libertine G";

    /**
     * Font OpenSymbol
     * <ul>
     * <li>Variants/styles/subfamilies: Regular</li>
     * <li>Type: Symbol</li>
     * <li>Reasons for inclusion: Contains LO-specific PUA symbols used in Math</li>
     * <li>Added in: OOo 2.4</li>
     * </ul>
     */
    public static final String OPENSYMBOL = "OpenSymbol";

    /**
     * Font Noto Sans
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Sans</li>
     * <li>Coverage: Latin, Cyrillic, Greek, Arabic, Armenian, Georgian, Hebrew, Lao, Lisu</li>
     * <li>Added in: LO 6.0</li>
     * </ul>
     */
    public static final String NOTO_SANS = "Noto Sans";

    /**
     * Font Source Code Pro
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold</li>
     * <li>Type: Mono</li>
     * <li>Coverage: Latin</li>
     * <li>Added in: LO 4.0</li>
     * </ul>
     */
    public static final String SOURCE_CODE_PRO = "Source Code Pro";

    /**
     * Font Source Sans Pro
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic, Black, Black Italic,
     * Extralight, Extralight Italic, Light, Light Italic, Semibold, Semibold Italic</li>
     * <li>Type: Sans</li>
     * <li>Coverage: Latin, Cyrillic, Greek</li>
     * <li>Added in: LO 4.0</li>
     * </ul>
     */
    public static final String SOURCE_SANS_PRO = "Source Sans Pro";

    /**
     * Font Source Serif Pro
     * <ul>
     * <li>Variants/styles/subfamilies: Regular, Bold, Italic, Bold Italic</li>
     * <li>Type: Serif</li>
     * <li>Coverage: Latin, Cyrillic</li>
     * <li>Added in: LO 6.2</li>
     * </ul>
     */
    public static final String SOURCE_SERIF_PRO = "Source Serif Pro";
}
