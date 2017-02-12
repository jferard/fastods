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
 *
 *         mvn -Dtest=BenchIT#test test
 */
public class Benchmark {
	private static final int COL_COUNT = 20;
	private static final int ROW_COUNT = 5000;
	private static final int TIMES = 10;

	@Rule
	public TestName name = new TestName();

	private Logger logger;
	private Random random;

	@BeforeClass
	public static final void beforeClass() {
		File generated_files = new File("generated_files");
		if (generated_files.exists())
			return;

		generated_files.mkdir();
	}

	@Before
	public final void setUp() {
		this.random = new Random();
		this.logger = Logger.getLogger("Benchmark");
	}

	@Test
	public void test1() throws IOException {
		this.test(Benchmark.ROW_COUNT, Benchmark.COL_COUNT, Benchmark.TIMES);
	}

	private void test(int rowCount, int colCount, int times) throws IOException {
		Bench bench1 = new BenchFast(this.logger, rowCount, colCount);
		Bench bench1b = new BenchFastFlush(this.logger, rowCount, colCount);
		Bench bench2 = new BenchSimple(this.logger, rowCount, colCount);
		Bench bench3 = new BenchJOpen(this.logger, rowCount, colCount);
		for (int i = 0; i < times; i++) {
			bench1.iteration();
			bench1b.iteration();
			bench2.iteration();
			bench3.iteration();
		}

		this.logger.info(bench1.getWithoutWarmup().toString());
		this.logger.info(bench1b.getWithoutWarmup().toString());
		this.logger.info(bench2.getWithoutWarmup().toString());
		this.logger.info(bench3.getWithoutWarmup().toString());
	}

	@Test
	public void test2() throws IOException {
		this.test(2*Benchmark.ROW_COUNT, 2*Benchmark.COL_COUNT, Benchmark.TIMES);
	}

	@Test
	public void test3() throws IOException {
		this.test(3*Benchmark.ROW_COUNT, 3*Benchmark.COL_COUNT, Benchmark.TIMES);
	}
}
