package com.github.jferard.fastods.entry;

import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.entry.StylesContainer.Dest;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class StylesContainerTest {
	private DataStyleBuilderFactory factory;
	private XMLUtil util;
	private Locale locale;
	private TableCellStyle st1;
	private TableCellStyle st2;
	private DataStyle ds1;
	private DataStyle ds2;
	private MasterPageStyle ps1;
	private MasterPageStyle ps2;
	private StylesContainer stylesContainer;

	@Before
	public void setUp() {
		this.stylesContainer = new StylesContainer();
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.locale = Locale.US;

		this.st1 = TableCellStyle.builder("a").fontStyleItalic().build();
		this.st2 = TableCellStyle.builder("a").fontWeightBold().build();

		final DataStyleBuilderFactory f = new DataStyleBuilderFactory(this.util,
				this.locale);
		this.ds1 = f.booleanStyleBuilder("a").country("a").build();
		this.ds2 = f.booleanStyleBuilder("a").country("b").build();

		this.ps1 = MasterPageStyle.builder("a").allMargins("1").build();
		this.ps2 = MasterPageStyle.builder("a").allMargins("2").build();
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
	}

	@Test
	public final void testDataStyleCreateTwice() {
		Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
		Assert.assertFalse(
				this.stylesContainer.addDataStyle(this.ds2, Mode.CREATE));
		Map<String, DataStyle> dataStyles = this.stylesContainer.getDataStyles();
		Assert.assertEquals(1, dataStyles.size());
		Assert.assertEquals(this.ds1, dataStyles.get("a"));
	}

	@Test
	public final void testDataStyleCreateThenUpdate() {
		Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
		Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2, Mode.UPDATE));
		Map<String, DataStyle> dataStyles = this.stylesContainer.getDataStyles();
		Assert.assertEquals(1, dataStyles.size());
		Assert.assertEquals(this.ds2, dataStyles.get("a"));
	}

	@Test
	public final void testDataStyleUpdate() {
		Assert.assertFalse(
				this.stylesContainer.addDataStyle(this.ds2, Mode.UPDATE));
		Map<String, DataStyle> dataStyles = this.stylesContainer.getDataStyles();
		Assert.assertEquals(0, dataStyles.size());
	}

	@Test
	public final void testDataStyleCreateThenUpdateIfExists() {
		Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
		Assert.assertTrue(
				this.stylesContainer.addDataStyle(this.ds2, Mode.CREATE_OR_UPDATE));
		Map<String, DataStyle> dataStyles = this.stylesContainer.getDataStyles();
		Assert.assertEquals(1, dataStyles.size());
		Assert.assertEquals(this.ds2, dataStyles.get("a"));
	}

	@Test
	public final void testDataStyleUpdateIfExists() {
		Assert.assertTrue(
				this.stylesContainer.addDataStyle(this.ds2, Mode.CREATE_OR_UPDATE));
		Map<String, DataStyle> dataStyles = this.stylesContainer.getDataStyles();
		Assert.assertEquals(1, dataStyles.size());
		Assert.assertEquals(this.ds2, dataStyles.get("a"));
	}

	@Test
	public final void testPageStyleCreateTwice() {
		Assert.assertTrue(
				this.stylesContainer.addMasterPageStyle(this.ps1, Mode.CREATE));
		Assert.assertFalse(
				this.stylesContainer.addMasterPageStyle(this.ps2, Mode.CREATE));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesContainer
				.getMasterPageStyles();
		Assert.assertEquals(1, masterPageStyles.size());
		Assert.assertEquals(this.ps1, masterPageStyles.get("a"));
	}

	@Test
	public final void testPageStyleCreateThenUpdate() {
		Assert.assertTrue(
				this.stylesContainer.addMasterPageStyle(this.ps1, Mode.CREATE));
		Assert.assertTrue(
				this.stylesContainer.addMasterPageStyle(this.ps2, Mode.UPDATE));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesContainer
				.getMasterPageStyles();
		Assert.assertEquals(1, masterPageStyles.size());
		Assert.assertEquals(this.ps2, masterPageStyles.get("a"));
	}

	@Test
	public final void testPageStyleUpdate() {
		Assert.assertFalse(
				this.stylesContainer.addMasterPageStyle(this.ps2, Mode.UPDATE));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesContainer
				.getMasterPageStyles();
		Assert.assertEquals(0, masterPageStyles.size());
	}

	@Test
	public final void testPageStyleCreateThenUpdateIfExists() {
		Assert.assertTrue(
				this.stylesContainer.addMasterPageStyle(this.ps1, Mode.CREATE));
		Assert.assertTrue(this.stylesContainer.addMasterPageStyle(this.ps2,
				Mode.CREATE_OR_UPDATE));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesContainer
				.getMasterPageStyles();
		Assert.assertEquals(1, masterPageStyles.size());
		Assert.assertEquals(this.ps2, masterPageStyles.get("a"));
	}

	@Test
	public final void testPageStyleUpdateIfExists() {
		Assert.assertTrue(this.stylesContainer.addMasterPageStyle(this.ps2,
				Mode.CREATE_OR_UPDATE));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesContainer
				.getMasterPageStyles();
		Assert.assertEquals(1, masterPageStyles.size());
		Assert.assertEquals(this.ps2, masterPageStyles.get("a"));
	}

	// CONTENT
	@Test
	public final void testAddDataStyle() {
		final DataStyle dataStyle = this.factory.booleanStyleBuilder("test")
				.build();

		this.stylesContainer.addDataStyle(dataStyle);
		PowerMock.replayAll();
//		this.oe.addDataStyle(dataStyle);
		PowerMock.verifyAll();
	}

}
