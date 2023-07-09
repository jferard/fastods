/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.util.FileOpen;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jferard on 09/05/17.
 */
public class OdsFactoryTest {
    private OdsFactory odsFactory;
    private File file;
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        this.logger = PowerMock.createMock(Logger.class);
        this.odsFactory = OdsFactory.create(this.logger, Locale.US);
        this.file = File.createTempFile("factory_test", "tmp");
    }

    @After
    public void tearDown() {
        this.file.delete();
    }

    @Test
    @Deprecated
    public void createFactory() {
        final Locale locale = Locale.US;
        final String country = locale.getCountry();
        System.out.println(country.length());
        final DataStylesBuilder dataStylesBuilder = DataStylesBuilder.create(locale);
        final OdsFactory factory =
                OdsFactory.create().dataStyles(dataStylesBuilder.build())
                        .noLibreOfficeMode().metaElement(MetaElement.create()).addNamespaceByPrefix(
                                new HashMap<>());
        factory.createWriter();
    }

    @Test
    public void createFactoryBuilder() {
        final Locale locale = Locale.US;
        final String country = locale.getCountry();
        System.out.println(country.length());
        final OdsFactoryBuilder builder = OdsFactory
                .builder(Logger.getLogger(NamedOdsDocument.class.getName()), locale);
        final DataStylesBuilder dataStylesBuilder = DataStylesBuilder.create(locale);
        final DataStyles ds = dataStylesBuilder.build();
        final OdsFactory factory = builder.dataStyles(ds)
                .noLibreOfficeMode().metaElement(MetaElement.create())
                .addNamespaceByPrefix(new HashMap<String, String>()).build();
        factory.createWriter();
    }

    @Test
    public void createWriter() throws Exception {
        PowerMock.resetAll();
        this.logger.log(Level.FINE, "file saved");

        PowerMock.replayAll();
        final NamedOdsFileWriter writer = this.odsFactory.createWriter(this.file.getAbsolutePath());
        writer.save();
        writer.close();

        PowerMock.verifyAll();
        Assert.assertTrue(this.file.length() > 0);
    }

    @Test
    public void createWriter1() throws Exception {
        PowerMock.resetAll();
        PowerMock.replayAll();

        this.odsFactory.createWriter(this.file);

        PowerMock.verifyAll();
    }

    @Test
    public void createWriterAdapter() throws Exception {
        final Capture<String> msgCapture = Capture.newInstance();

        PowerMock.resetAll();
        this.logger.fine(EasyMock.capture(msgCapture));

        PowerMock.replayAll();

        this.odsFactory.createWriterAdapter(this.file);

        PowerMock.verifyAll();
        Assert.assertTrue(msgCapture.getValue().startsWith("Add new flusher"));
    }

    @Test
    public void openFile() throws Exception {
        PowerMock.resetAll();
        PowerMock.replayAll();

        FileOpen.openFile(this.file.getAbsolutePath());

        PowerMock.verifyAll();
    }

    @Test
    public void openFile1() throws Exception {
        PowerMock.resetAll();
        PowerMock.replayAll();

        FileOpen.openFile(this.file);

        PowerMock.verifyAll();
    }
}