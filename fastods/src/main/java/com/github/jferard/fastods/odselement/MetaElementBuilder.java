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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MetaElementBuilder {
    private final List<String> keyWords;
    private final List<UserDefined> userDefineds;
    private String creator;
    private String dateTime;
    private String description;
    private String language;
    private String subject;
    private String title;
    private String editingCycles;
    private String editingDuration;
    private String initialCreator;

    public MetaElementBuilder() {
        final Date dt = new Date();
        this.creator = System.getProperty("user.name");
        this.dateTime = MetaElement.DF_DATE.format(dt) + "T" + MetaElement.DF_TIME.format(dt);
        this.description = null;
        this.language = Locale.getDefault().getLanguage();
        this.title = null;
        this.editingCycles = "1";
        this.editingDuration = "PT1M00S";
        this.initialCreator = null;
        this.keyWords = new ArrayList<String>();
        this.userDefineds = new ArrayList<UserDefined>();
    }

    public MetaElementBuilder creator(final String creator) {
        this.creator = creator;
        return this;
    }

    public MetaElementBuilder date(final String dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public MetaElementBuilder description(final String description) {
        this.description = description;
        return this;
    }

    public MetaElementBuilder language(final String language) {
        this.language = language;
        return this;
    }

    public MetaElementBuilder subject(final String subject) {
        this.subject = subject;
        return this;
    }

    public MetaElementBuilder title(final String title) {
        this.title = title;
        return this;
    }

    public MetaElementBuilder editingCycles(final String editingCycles) {
        this.editingCycles = editingCycles;
        return this;
    }

    public MetaElementBuilder editingDuration(final String editingDuration) {
        this.editingDuration = editingDuration;
        return this;
    }

    public MetaElementBuilder initialCreator(final String initialCreator) {
        this.initialCreator = initialCreator;
        return this;
    }

    public MetaElementBuilder keyWord(final String... keyWords) {
        this.keyWords.addAll(Arrays.asList(keyWords));
        return this;
    }

    public MetaElementBuilder userDefinedBoolean(final String name, final boolean value) {
        this.userDefineds.add(UserDefined.fromBoolean(name, value));
        return this;
    }

    public MetaElementBuilder userDefinedDate(final String name, final Date value) {
        this.userDefineds.add(UserDefined.fromDate(name, value));
        return this;
    }

    public MetaElementBuilder userDefinedFloat(final String name, final Number value) {
        this.userDefineds.add(UserDefined.fromFloat(name, value));
        return this;
    }

    public MetaElementBuilder userDefinedTime(final String name, final Date value) {
        this.userDefineds.add(UserDefined.fromTime(name, value));
        return this;
    }

    public MetaElementBuilder userDefinedString(final String name, final String value) {
        this.userDefineds.add(UserDefined.fromString(name, value));
        return this;
    }

    /**
     * @return the element
     */
    public MetaElement build() {
        return new MetaElement(this.creator, this.dateTime, this.description, this.language,
                this.subject, this.title, this.editingCycles, this.editingDuration,
                this.initialCreator, this.keyWords, this.userDefineds);
    }

}
