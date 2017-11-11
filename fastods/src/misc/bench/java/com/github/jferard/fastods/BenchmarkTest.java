/* *****************************************************************************
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
 * ****************************************************************************/
package com.github.jferard.fastods;

import com.github.jferard.fastods.testlib.Bench;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 *         <p>
 *         mvn -Dtest=BenchIT#test test
 */
public class BenchmarkTest {
    private static final int COL_COUNT = 20;
    private static final int ROW_COUNT = 5000;
    private static final int TIMES = 10;

    @BeforeClass
    public static void beforeClass() {
        final File generated_files = new File("generated_files");
        if (generated_files.exists())
            return;

        generated_files.mkdir();
    }
    @Rule
    public TestName name = new TestName();
    private Logger logger;
    private Random random;

    @Before
    public final void setUp() {
        this.random = new Random();
        this.logger = Logger.getLogger("Benchmark");
    }

    @Test
    public void test1() throws IOException {
        this.test(BenchmarkTest.ROW_COUNT, BenchmarkTest.COL_COUNT, BenchmarkTest.TIMES);
    }

    private void test(final int rowCount, final int colCount, final int times) throws IOException {
        final Bench bench1 = new BenchFast(this.logger, rowCount, colCount);
        final Bench bench1b = new BenchFastFlush(this.logger, rowCount, colCount);
        final Bench bench1c = new BenchFastFlushWithThreads(this.logger, rowCount, colCount);
        final Bench bench2 = new BenchSimple(this.logger, rowCount, colCount);
        final Bench bench3 = new BenchJOpen(this.logger, rowCount, colCount);
        for (int i = 0; i < times; i++) {
            bench1.iteration();
            bench1b.iteration();
            bench1c.iteration();
            bench2.iteration();
            bench3.iteration();
        }

        this.logger.info(bench1.getWithoutWarmup().toString());
        this.logger.info(bench1b.getWithoutWarmup().toString());
        this.logger.info(bench1c.getWithoutWarmup().toString());
        this.logger.info(bench2.getWithoutWarmup().toString());
        this.logger.info(bench3.getWithoutWarmup().toString());
    }

    @Test
    public void test2() throws IOException {
        this.test(2 * BenchmarkTest.ROW_COUNT, 2 * BenchmarkTest.COL_COUNT, BenchmarkTest.TIMES);
    }

    @Test
    public void test3() throws IOException {
        this.test(3 * BenchmarkTest.ROW_COUNT, 3 * BenchmarkTest.COL_COUNT, BenchmarkTest.TIMES);
    }

    //	@Test
    public void test4() throws IOException {
        this.test(6 * BenchmarkTest.ROW_COUNT, 6 * BenchmarkTest.COL_COUNT, BenchmarkTest.TIMES);
    }

    //	@Test
    public void checkThreads() throws IOException {
        final Bench bench1c = new BenchFastFlushWithThreads(this.logger, 15, 20);
        for (int i = 0; i < 1000; i++) {
            bench1c.iteration();
        }
        this.logger.info(bench1c.getWithoutWarmup().toString());
    }
}
