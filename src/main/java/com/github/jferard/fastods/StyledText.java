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
 * @author Martin Schulz<br>
 * 
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net><br>
 * 
 * This file StyledText.java is part of SimpleODS.
 *
 */
public class StyledText {
	
	private Util u = Util.getInstance();
	
	private TextStyle ts = null;
	private String sText = null;
	
	public StyledText(TextStyle t,String s) {
		this.ts = t;
		this.setText(s);
	}

	public TextStyle getTextStyle() {
		return ts;
	}

	public void setTextStyle(TextStyle t) {
		this.ts = t;
	}

	public String getText() {
		return sText;
	}

	public void setText(String sText) {
		this.sText = u.toXmlString(sText);
	}
	
	/**
	 * Used in file styles.xml, in <office:master-styles>,<style:master-page />
	 * @return
	 */
	protected String toMasterStyleXML() {
		StringBuffer sbTemp = new StringBuffer(60);

		sbTemp.append("<text:span text:style-name=\""
				+ ts.getName() + "\">");
		sbTemp.append(sText);
		sbTemp.append("</text:span>");
		
		return sbTemp.toString();
	}
	
	

}
