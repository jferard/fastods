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

/**
 * /**
 * 
 * @author Julien Férard 
 * 
 * Copyright (C) 2016 J. Férard
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file TableFamilyStyleBuilder.java is part of FastODS. SimpleODS 0.5.1
 *         Changed all 'throw Exception' to 'throw FastOdsException' SimpleODS
 *         0.5.2 Replaced all text properties with a TextStyle object
 */
class TableColumnStyleBuilder {
	private String sColumnWidth;
	private String sName;

	/**
	 * Create a new table style and add it to contentEntry.<br>
	 * Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param nFamily
	 *            The type of this style, either
	 *            STYLE_TABLECOLUMN,STYLE_TABLEROW,STYLE_TABLE or
	 *            STYLE_TABLECELL
	 * @param sStyleName
	 *            A unique name for this style
	 * @param odsFile
	 *            The OdsFile to add this style to
	 */
	public TableColumnStyleBuilder() {
		this.sColumnWidth = "2.5cm"; // 0.5.0 changed from 2,500cm to 2.5cm
	}

	/**
	 * Set the column width of a table column.<br>
	 * sWidth is a length value expressed as a number followed by a unit of
	 * measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 * 
	 * @param sWidth
	 *            - The width of a column, e.g. '10cm'
	 * @return true - The width was set, false - this object is no table column,
	 *         you can not set the default cell to it
	 */
	public TableColumnStyleBuilder columnWidth(final String sWidth) {
		this.sColumnWidth = sWidth;
		return this;
	}
	
	public TableColumnStyleBuilder name(String sName) {
		this.sName = sName;
		return this;
	}

	public TableColumnStyle build() {
		return new TableColumnStyle(this.sName, this.sColumnWidth);
		
	}
}
