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

package com.github.jferard.fastods.testlib;

/**
 * A fibonacci numbers generator
 *
 * @author Julien Férard
 */
public class Fibonacci {
    private final int[] is;
    private int index;

    /**
     * @return a new Fibonacci sequence, with 0 and 1 as first elements
     */
    public static Fibonacci create() {
        return new Fibonacci(0, 1);
    }

    /**
     * Create a new Fibonacci sequence
     *
     * @param first  the first element
     * @param second the second element
     */
    public Fibonacci(final int first, final int second) {
        // a bit hackish: we step back to gain momentum. This avoids a special treatment of the
        // first steps.
        // nextInt does alternatively :
        // is[0] += is[1], return is[0]
        // is[1] += is[0], return is[1]
        // what will happen on the first call? is[0] += is[1] : first - (second - first), second
        // - first = first
        // on second call? index=1, so is[1] += is[0] : second - first + first = second
        this.is = new int[]{first - (second - first), second - first};
        this.index = 0;
    }

    /**
     * @return the next int in the Fibonacci suite
     */
    public int nextInt() {
        // alternatively :
        // is[0] += is[1], return is[0]
        // is[1] += is[0], return is[1]
        final int m = this.index++ % 2;
        this.is[m] += this.is[this.index % 2];
        return this.is[m];
    }

    /**
     * @param max modulo when the values exceed the max
     * @return the next int in the Fibonacci suite
     */
    public int nextInt(final int max) {
        // alternatively :
        // is[0] += is[1], return is[0]
        // is[1] += is[0], return is[1]
        final int m = this.index++ % 2;
        this.is[m] += this.is[this.index % 2];
        this.is[m] %= max;
        return this.is[m];
    }
}
