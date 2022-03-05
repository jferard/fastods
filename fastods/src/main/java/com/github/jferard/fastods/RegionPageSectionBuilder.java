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

package com.github.jferard.fastods;

import com.github.jferard.fastods.util.Box;

/**
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header
 *
 * @author Julien Férard
 */
public class RegionPageSectionBuilder extends PageSectionBuilder<RegionPageSectionBuilder> {

    private final Box<Text> centerRegionBox;
    private final Box<Text> leftRegionBox;
    private final Box<Text> rightRegionBox;

    /**
     * Create a new footer object.
     */
    RegionPageSectionBuilder() {
        super();
        this.leftRegionBox = new Box<Text>();
        this.centerRegionBox = new Box<Text>();
        this.rightRegionBox = new Box<Text>();
    }

    @Override
    public PageSection build() {
        final PageSectionStyle style =
                new PageSectionStyle(this.marginsBuilder.build(), this.minHeight);
        final PageSectionContent header =
                new RegionFooterHeader(this.centerRegionBox.get(), this.leftRegionBox.get(),
                        this.rightRegionBox.get());
        return new PageSection(header, style);
    }

    /**
     * Switch to a new region
     *
     * @param region the region that will be set
     * @return this for fluent style
     */
    public RegionPageSectionBuilder region(final PageSectionContent.Region region) {
        switch (region) {
            case LEFT: // Use left region
                this.curRegionBox = this.leftRegionBox;
                break;
            case CENTER: // Use center region
                this.curRegionBox = this.centerRegionBox;
                break;
            case RIGHT: // Use right region
                this.curRegionBox = this.rightRegionBox;
                break;
            default: // Invalid footerRegionValue, use center region as default
                ThisShouldNotHappen.illegalState();
        }
        return this;
    }

}
