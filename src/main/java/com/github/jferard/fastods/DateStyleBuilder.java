/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import com.github.jferard.fastods.DateStyle.Format;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file DateStyleBuilder.java is part of FastODS.
 */
class DateStyleBuilder {
	private boolean bAutomaticOrder;

	/**
	 * The default date format DATEFORMAT_DDMMYY.
	 */
	private Format dateFormat;

	/**
	 * The name of this style.
	 */
	private String sName;

	/**
	 * Create a new date style with the name sName.<br>
	 * Version 0.5.1 Added.
	 *
	 * @param sName
	 *            The name of the number style.
	 * @param odsFile
	 *            The odsFile to which this style belongs to.
	 */
	protected DateStyleBuilder() {
		this.dateFormat = DateStyle.DEFAULT_FORMAT;
		this.bAutomaticOrder = false;
	}

	/**
	 * The automatic-order attribute can be used to automatically order data to
	 * match the default order<br>
	 * for the language and country of the date style.
	 *
	 * @param bAutomatic
	 * @return this for fluent style
	 */
	public DateStyleBuilder automaticOrder(final boolean bAutomatic) {
		this.bAutomaticOrder = bAutomatic;
		return this;
	}

	/**
	 * @return the DateStyle
	 */
	public DateStyle build() {
		if (this.sName == null)
			throw new IllegalArgumentException();

		return new DateStyle(this.sName, this.dateFormat, this.bAutomaticOrder);
	}

	/**
	 * Set the date format.<br>
	 * Valid is one of the following:<br>
	 * DateStyle.DATEFORMAT_DDMMYYYY<br>
	 * DateStyle.DATEFORMAT_DDMMYY<br>
	 * DateStyle.DATEFORMAT_TMMMMYYYY<br>
	 * DateStyle.DATEFORMAT_MMMM<br>
	 * *
	 *
	 * @param format
	 *            The date format to be used.
	 * @return this for fluent style
	 */
	public DateStyleBuilder dateFormat(final Format format) {
		this.dateFormat = format;
		return this;
	}

	/**
	 * Set the name of this style to sName.
	 *
	 * @param name
	 *            - The name of this style.
	 * @return this for fluent style
	 */
	public DateStyleBuilder name(final String name) {
		this.sName = name;
		return this;
	}
}
