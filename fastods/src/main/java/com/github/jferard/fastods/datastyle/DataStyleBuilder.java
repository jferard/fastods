/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.util.StyleBuilder;

import java.util.Locale;

/**
 * A data style builder
 *
 * @author Julien Férard
 *
 * @param <S> the destination style
 * @param <T> the destination style builder
 */
public interface DataStyleBuilder<S extends DataStyle, T> extends StyleBuilder<S> {
    /**
     * Set the country code
     * @param countryCode The two letter country code, e.g. 'US'
     * @return this for fluent style
     */
    T country(final String countryCode);

    /**
     * Set the language code
     * @param languageCode The two letter language code, e.g. 'en'. See http://www.ietf.org/rfc/rfc3066.txt
     * @return this for fluent style
     */
    T language(final String languageCode);

    /**
     * Set the locale
     * @param locale the locale
     * @return this for fluent style
     */
    T locale(final Locale locale);

    /**
     * 19.517 style:volatile
     * "The style:volatile attribute specifies whether unused style in a document are retained or discarded by consumers."
     * @param volatileStyle true if "consumers should keep unused styles", false otherwise.
     * @return this for fluent style
     */
    T volatileStyle(final boolean volatileStyle);
}
