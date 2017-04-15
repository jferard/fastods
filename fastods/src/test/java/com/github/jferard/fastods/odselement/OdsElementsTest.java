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
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Locale;
import java.util.logging.Logger;

public class OdsElementsTest {
	private ContentElement contentElement;
	private DataStyleBuilderFactory factory;
	private Locale locale;
	private ManifestElement manifestElement;
	private MetaElement metaElement;
	private MimetypeElement mimetypeElement;
	private OdsElements oe;
	private SettingsElement settingsElement;
	private StylesContainer stylesContainer;
	private StylesElement stylesElement;
	private XMLUtil util;

	@Before
	public void setUp() {
		final Logger logger = PowerMock.createNiceMock(Logger.class);
		this.mimetypeElement = PowerMock.createMock(MimetypeElement.class);
		this.manifestElement = PowerMock.createMock(ManifestElement.class);
		this.settingsElement = PowerMock.createMock(SettingsElement.class);
		this.metaElement = PowerMock.createMock(MetaElement.class);
		this.contentElement = PowerMock.createMock(ContentElement.class);
		this.stylesElement = PowerMock.createMock(StylesElement.class);
		this.stylesContainer = new StylesContainer();

		this.oe = new OdsElements(logger, this.mimetypeElement, this.manifestElement,
				this.settingsElement, this.metaElement, this.contentElement,
				this.stylesElement, this.stylesContainer);
		this.util = XMLUtil.create();
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
	}

	/*
	@Test
	public final void testAddTextStyle() {
		final TextStyle ts = TextStyle.builder("test").build();
	
		this.stylesElement.addTextStyle(ts);
		PowerMock.replayAll();
		this.oe.addTextStyle(ts);
		PowerMock.verifyAll();
	}*/

	@Test
	public final void testGetTable() {
		final Table t = PowerMock.createMock(Table.class);
		EasyMock.expect(this.contentElement.getTable(9)).andReturn(t);
		EasyMock.expect(this.contentElement.getTable("nine")).andReturn(t);
		EasyMock.expect(this.contentElement.getTableCount()).andReturn(8);
		PowerMock.replayAll();
		Assert.assertEquals(t, this.oe.getTable(9));
		Assert.assertEquals(t, this.oe.getTable("nine"));
		Assert.assertEquals(8, this.oe.getTableCount());
		PowerMock.verifyAll();
	}

}
