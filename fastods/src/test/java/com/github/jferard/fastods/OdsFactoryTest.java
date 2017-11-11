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

package com.github.jferard.fastods;

import com.github.jferard.fastods.OdsFactory.FileState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Locale;
import java.util.logging.Logger;
import java.io.File;

/**
 * Created by jferard on 09/05/17.
 */
public class OdsFactoryTest {
    private OdsFactory odsFactory;
    private File file;

    @Before
    public void setUp() throws Exception {
        final Logger logger = PowerMock.createMock(Logger.class);
        this.odsFactory = OdsFactory.create(logger, Locale.US);
        this.file = File.createTempFile("factory_test", "tmp");
    }

    @Test
    public void checkFile() throws Exception {
        Assert.assertEquals(FileState.IS_DIRECTORY, this.odsFactory.checkFile("."));
        Assert.assertEquals(FileState.FILE_EXISTS, this.odsFactory.checkFile("pom.xml"));
        Assert.assertEquals(FileState.OK, this.odsFactory.checkFile(this.file.getAbsolutePath()));
    }

    @Test
    public void createWriter() throws Exception {
        this.odsFactory.createWriter(this.file.getAbsolutePath());
    }

    @Test
    public void createWriter1() throws Exception {
        this.odsFactory.createWriter(this.file);
    }

    @Test
    public void createWriterAdapter() throws Exception {
        this.odsFactory.createWriterAdapter(this.file);
    }

    @Test
    public void openFile() throws Exception {
    }

    @Test
    public void openFile1() throws Exception {
    }

    @Before
    public void tearDown() throws Exception {
        this.file.delete();
    }

}