/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

/**
 * A CellValue that contains a currency value
 *
 * @author Julien Férard
 */
public class CurrencyValue implements CellValue {
    /**
     * @param o        the object to cast
     * @param currency the currency
     * @return the currency value
     * @throws FastOdsException if the cast was not possible
     */
    public static CurrencyValue from(final Object o, final String currency)
            throws FastOdsException {
        if (o instanceof Number) {
            return new CurrencyValue((Number) o, currency);
        } else if (o instanceof CurrencyValue) {
            final CurrencyValue currencyValue = (CurrencyValue) o;
            return new CurrencyValue(currencyValue.value, currency);
        } else {
            throw new FastOdsException("Can't cast " + o + " to Currency");
        }
    }

    private final Number value;
    private final String currency;

    /**
     * @param value    the value
     * @param currency the currency value
     */
    public CurrencyValue(final Number value, final String currency) {
        this.value = value;
        this.currency = currency;
    }

    @Override
    public void setToCell(final TableCell tableCell) {
        tableCell.setCurrencyValue(this.value, this.currency);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CurrencyValue)) {
            return false;
        }

        final CurrencyValue other = (CurrencyValue) o;
        return this.value.equals(other.value) && this.currency.equals(other.currency);
    }

    @Override
    public final int hashCode() {
        return this.value.hashCode() * 31 + this.currency.hashCode();
    }
}
