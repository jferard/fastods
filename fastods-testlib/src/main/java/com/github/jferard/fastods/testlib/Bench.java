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
package com.github.jferard.fastods.testlib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * A base class for benchmarks
 *
 * @author Julien Férard
 */
public abstract class Bench {
    private final int colCount;
    private final Logger logger;
    private final Random random;
    private final int rowCount;
    private final List<Long> times;
    private final String name;

    /**
     * Create a new bench
     *
     * @param logger   the logger
     * @param name     the name of the bench
     * @param rowCount the row count of the dest table
     * @param colCount the column count of the dest table
     */
    public Bench(final Logger logger, final String name, final int rowCount, final int colCount) {
        this.logger = logger;
        this.name = name;
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.times = new ArrayList<Long>();
        this.random = new Random(); // don't use a fixed seed, since the bench needs a new
        // sequence each time
    }

    /**
     * @return the computations without the warm up included
     */
    public Computations getWithWarmUp() {
        return new Computations(this.name, this.times.subList(2, this.times.size()));
    }

    /**
     * @return the computations with the warmup
     */
    public Computations getWithoutWarmUp() {
        return new Computations(this.name, this.times);
    }

    /**
     * Execute the test once more
     *
     * @throws IOException if an I/O exception occurs in the test
     */
    public void iteration() throws IOException {
        this.times.add(this.test());
    }

    /**
     * The test. Override this method and use col count and row count and random
     *
     * @return the time
     * @throws IOException if an I/O error occurs
     */
    public abstract long test() throws IOException;

    /**
     * @return the col count
     */
    protected int getColCount() {
        return this.colCount;
    }

    /**
     * @return a Random instance
     */
    protected Random getRandom() {
        return this.random;
    }

    /**
     * @return the row count
     */
    protected int getRowCount() {
        return this.rowCount;
    }
}
