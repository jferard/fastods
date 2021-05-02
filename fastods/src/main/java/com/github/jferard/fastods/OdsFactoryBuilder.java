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

package com.github.jferard.fastods;

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.MetaElement;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.ref.TableNameUtil;
import com.github.jferard.fastods.util.IntegerRepresentationCache;
import com.github.jferard.fastods.util.XMLUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class OdsFactoryBuilder {
    private final Logger logger;
    private final PositionUtil positionUtil;
    private final IntegerRepresentationCache cache;
    private final XMLUtil xmlUtil;
    private final Map<String, String> additionalNamespaceByPrefix;
    private DataStyles format;
    private boolean libreOfficeMode;
    private MetaElement metaElement;

    public OdsFactoryBuilder(final Logger logger, final Locale locale) {
        this.logger = logger;
        this.positionUtil = new PositionUtil(new TableNameUtil());
        this.cache = IntegerRepresentationCache.create();
        this.xmlUtil = XMLUtil.create();
        this.additionalNamespaceByPrefix = new HashMap<String, String>();

        this.format = DataStylesBuilder.create(locale).build();
        this.libreOfficeMode = true;
        this.metaElement = MetaElement.create();
    }

    public OdsFactory build() {
        return new OdsFactory(this.logger, this.positionUtil, this.cache, this.xmlUtil,
                this.additionalNamespaceByPrefix, this.format, this.libreOfficeMode, this.metaElement
        );
    }

    /**
     * Set the data styles
     *
     * @param ds the data styles
     * @return this for fluent style
     */
    public OdsFactoryBuilder dataStyles(final DataStyles ds) {
        this.format = ds;
        return this;
    }

    /**
     * Disable the LibreOffice mode. The LibreOffice mode adds a style to every cell, to force
     * LibreOffice to render the cell styles correctly.
     * This mode is set by default, and might slow down the generation of the file.
     *
     * @return this for fluent style
     */
    public OdsFactoryBuilder noLibreOfficeMode() {
        this.libreOfficeMode = false;
        return this;
    }

    /**
     * Use a custom meta element
     *
     * @param metaElement the meta element.
     * @return this for fluent style
     */
    public OdsFactoryBuilder metaElement(final MetaElement metaElement) {
        this.metaElement = metaElement;
        return this;
    }

    /**
     * Use custom namespace prefixes in content element.
     *
     * @param additionalNamespaceByPrefix a map prefix -> namespace
     * @return this for fluent style
     */
    public OdsFactoryBuilder addNamespaceByPrefix(final Map<String, String> additionalNamespaceByPrefix) {
        this.additionalNamespaceByPrefix.putAll(additionalNamespaceByPrefix);
        return this;
    }

}
