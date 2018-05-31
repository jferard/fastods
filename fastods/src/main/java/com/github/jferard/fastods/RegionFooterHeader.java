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
 * 16.10<style:header>
 * 16.11<style:footer>
 * <p>
 * "The <style:header> element represents the content of a header in a <style:master-page> element."
 * "The <style:footer> element represents the content of a footer in a <style:master-page> element."
 * <p>
 * <p>
 * The RegionFooterHeader class represents a footer/header which is composed of three sections
 * (left, center, right).
 * It's an alternative to the SimpleFooterHeader that has only a center section.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class RegionFooterHeader implements PageSectionContent {
    private static void appendRegionXMLToMasterStyle(final XMLUtil util,
                                                     final Appendable appendable, final Text region,
                                                     final CharSequence regionName)
            throws IOException {
        if (region == null || region.isEmpty()) return;

        appendable.append("<style:").append(regionName).append(">");
        region.appendXMLContent(util, appendable);
        appendable.append("</style:").append(regionName).append(">");
    }

    private final Text centerRegion;
    private final Text leftRegion;
    private final Text rightRegion;

    /**
     * Create a new footer object. It is composed of three regions.
     *
     * @param centerRegion the center region
     * @param leftRegion   the left region
     * @param rightRegion  the right region
     */
    RegionFooterHeader(final Text centerRegion, final Text leftRegion, final Text rightRegion) {
        super();
        this.centerRegion = centerRegion;
        this.leftRegion = leftRegion;
        this.rightRegion = rightRegion;
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        if (this.leftRegion != null && !this.leftRegion.isEmpty())
            this.leftRegion.addEmbeddedStylesFromFooterHeader(stylesContainer);
        if (this.centerRegion != null && !this.centerRegion.isEmpty())
            this.centerRegion.addEmbeddedStylesFromFooterHeader(stylesContainer);
        if (this.rightRegion != null && !this.rightRegion.isEmpty())
            this.rightRegion.addEmbeddedStylesFromFooterHeader(stylesContainer);
    }

    /**
     * Used in file styles.xml, in <office:master-styles>,<style:master-page />.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void appendXMLToMasterStyle(final XMLUtil util, final Appendable appendable)
            throws IOException {
        RegionFooterHeader
                .appendRegionXMLToMasterStyle(util, appendable, this.leftRegion, "region-left");
        RegionFooterHeader
                .appendRegionXMLToMasterStyle(util, appendable, this.centerRegion, "region-center");
        RegionFooterHeader
                .appendRegionXMLToMasterStyle(util, appendable, this.rightRegion, "region-right");
    }
}
