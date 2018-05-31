/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * Represents the footer of a print page.
 *
 * @author Julien Férard
 */
public class Footer implements HeaderOrFooter {
    private final PageSection pageSection;

    /**
     * Create a new footer from a page section
     *
     * @param pageSection the page section
     */
    public Footer(final PageSection pageSection) {
        this.pageSection = pageSection;
    }


    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        this.pageSection.addEmbeddedStyles(stylesContainer);
    }

    @Override
    public void appendXMLToMasterStyle(final XMLUtil util, final Appendable appendable)
            throws IOException {
        this.pageSection.appendXMLToMasterStyle(util, appendable);
    }

    @Override
    public void appendPageSectionStyleXMLToAutomaticStyle(final XMLUtil util,
                                                          final Appendable appendable)
            throws IOException {
        this.pageSection.appendPageSectionStyleXMLToAutomaticStyle(util, appendable,
                PageSection.Type.FOOTER);
    }
}
