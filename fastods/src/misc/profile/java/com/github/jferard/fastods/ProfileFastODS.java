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

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 * <p>
 * Usage : launch jvisualvm. mvn
 * -Dmaven.surefire.debug="-agentpath:\"C:/Program
 * Files/Java/visualvm_138/profiler/lib/deployed/jdk16/windows-amd64/profilerinterface
 * .dll\"=\"C:\Program
 * Files\Java\visualvm_138\profiler\lib\",5140"
 * -Dtest=ProfileFastOds#testFast test
 */
public class ProfileFastODS {
    private static final int COL_COUNT = 600;
    private static final int ROW_COUNT = 3200000;

    @BeforeClass
    public static final void beforeClass() {
        final File generated_files = new File("generated_files");
        if (generated_files.exists()) return;

        generated_files.mkdir();
    }

    static public final void main(String[] args) throws IOException {
        ProfileFastODS profileFastODS = new ProfileFastODS();
        profileFastODS.setUp();
        profileFastODS.testFast();
        profileFastODS.tearDown();
    }
    @Rule
    public TestName name = new TestName();
    private Logger logger;
    private OdsFactory odsFactory;
    private Random random;
    private long t1;

    @Test
    public final void mediumTestFast() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document
                .addTable("test", ProfileFastODS.ROW_COUNT / 2, ProfileFastODS.COL_COUNT / 2);

        for (int y = 0; y < ProfileFastODS.ROW_COUNT / 2; y++) {
            final TableRow row = table.nextRow();
            final TableCellWalker walker = row.getWalker();
            for (int x = 0; x < ProfileFastODS.COL_COUNT / 2; x++) {
                walker.setFloatValue(this.random.nextInt(1000));
                walker.next();
            }
            if (y % (ProfileFastODS.ROW_COUNT / 100) == 0) this.logger.info("Row " + y);
        }

        writer.saveAs(new File("generated_files", "fastods_profile.ods"));
    }

    @Before
    public final void setUp() {
        this.logger = Logger.getLogger("OdsFileCreation");
        this.odsFactory = OdsFactory.create(this.logger, Locale.US);
        this.random = new Random(); // don't use a fixed seed here, since the profile needs a new
        // sequence each time
        // to be reliable
        this.logger.info("this.name.getMethodName()" + " : filling a " + ProfileFastODS.ROW_COUNT +
                " rows, " + ProfileFastODS.COL_COUNT + " columns spreadsheet");
        this.t1 = System.currentTimeMillis();
    }

    @Test
    public final void smallTestFast() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document
                .addTable("test", ProfileFastODS.ROW_COUNT / 4, ProfileFastODS.COL_COUNT / 4);

        for (int y = 0; y < ProfileFastODS.ROW_COUNT / 4; y++) {
            final TableRow row = table.nextRow();
            final TableCellWalker walker = row.getWalker();
            for (int x = 0; x < ProfileFastODS.COL_COUNT / 4; x++) {
                walker.setFloatValue(this.random.nextInt(1000));
                walker.next();
            }
            if (y % (ProfileFastODS.ROW_COUNT / 200) == 0) this.logger.info("Row " + y);
        }

        writer.saveAs(new File("generated_files", "fastods_profile.ods"));
    }

    @After
    public final void tearDown() {
        final long t2 = System.currentTimeMillis();
        this.logger.info("Filled in " + (t2 - this.t1) + " ms");
    }

    @Test
    public final void testFast() throws IOException {
        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document
                .addTable("test", ProfileFastODS.ROW_COUNT, ProfileFastODS.COL_COUNT);

        for (int y = 0; y < ProfileFastODS.ROW_COUNT; y++) {
            final TableRow row = table.nextRow();
            final TableCellWalker walker = row.getWalker();
            for (int x = 0; x < ProfileFastODS.COL_COUNT; x++) {
                walker.setFloatValue(this.random.nextInt(1000));
                walker.next();
            }
            if (y % (ProfileFastODS.ROW_COUNT / 50) == 0) this.logger.info("Row " + y);
        }

        writer.saveAs(new File("generated_files", "fastods_profile.ods"));
    }
}