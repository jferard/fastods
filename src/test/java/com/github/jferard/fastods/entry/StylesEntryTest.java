package com.github.jferard.fastods.entry;

import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.entry.OdsEntryWithStyles.Mode;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class StylesEntryTest {
	private StylesEntry stylesEntry;
	private XMLUtil util;
	private Locale locale;
	private TableCellStyle st1;
	private TableCellStyle st2;
	private DataStyle ds1;
	private DataStyle ds2;
	private MasterPageStyle ps1;
	private MasterPageStyle ps2;
	private StyleTagsContainer styleTagsContainer;

	@Before
	public void setUp() {
		this.styleTagsContainer = new StyleTagsContainer();
		this.stylesEntry = new StylesEntry(this.styleTagsContainer);
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.locale = Locale.US;

		this.st1 = TableCellStyle.builder("a").fontStyleItalic().build();
		this.st2 = TableCellStyle.builder("a").fontWeightBold().build();
		
		final DataStyleBuilderFactory f = new DataStyleBuilderFactory(this.util, this.locale);
		this.ds1 = f.booleanStyleBuilder("a").country("a").build();
		this.ds2 = f.booleanStyleBuilder("a").country("b").build();
		
		this.ps1 = MasterPageStyle.builder("a").allMargins("1").build();
		this.ps2 = MasterPageStyle.builder("a").allMargins("2").build();
	}

	@Test
	public final void testStyleTagCreateTwice() {
		Assert.assertTrue(this.stylesEntry.addStyleTag(this.st1, Mode.CREATE));
		Assert.assertFalse(this.stylesEntry.addStyleTag(this.st2, Mode.CREATE));
		final Map<String, StyleTag> styleTagByName = this.styleTagsContainer.getStyleTagByName();
		Assert.assertEquals(1,
				styleTagByName.size());
		Assert.assertEquals(this.st1,
				styleTagByName.get("table-cell@a"));
	}
	
	@Test
	public final void testStyleTagCreateThenUpdate() {
		Assert.assertTrue(this.stylesEntry.addStyleTag(this.st1, Mode.CREATE));
		Assert.assertTrue(this.stylesEntry.addStyleTag(this.st2, Mode.UPDATE));
		final Map<String, StyleTag> styleTagByName = this.styleTagsContainer.getStyleTagByName();
		Assert.assertEquals(1,
				styleTagByName.size());
		Assert.assertEquals(this.st2,
				styleTagByName.get("table-cell@a"));
	}
	
	@Test
	public final void testStyleTagUpdate() {
		Assert.assertFalse(this.stylesEntry.addStyleTag(this.st2, Mode.UPDATE));
		final Map<String, StyleTag> styleTagByName = this.styleTagsContainer.getStyleTagByName();
		Assert.assertEquals(0,
				styleTagByName.size());
	}
	
	@Test
	public final void testStyleTagCreateThenUpdateIfExists() {
		Assert.assertTrue(this.stylesEntry.addStyleTag(this.st1, Mode.CREATE));
		Assert.assertTrue(this.stylesEntry.addStyleTag(this.st2, Mode.UPDATE_IF_EXISTS));
		final Map<String, StyleTag> styleTagByName = this.styleTagsContainer.getStyleTagByName();
		Assert.assertEquals(1,
				styleTagByName.size());
		Assert.assertEquals(this.st2,
				styleTagByName.get("table-cell@a"));
	}
	
	@Test
	public final void testStyleTagUpdateIfExists() {
		Assert.assertTrue(this.stylesEntry.addStyleTag(this.st2, Mode.UPDATE_IF_EXISTS));
		final Map<String, StyleTag> styleTagByName = this.styleTagsContainer.getStyleTagByName();
		Assert.assertEquals(1,
				styleTagByName.size());
		Assert.assertEquals(this.st2,
				styleTagByName.get("table-cell@a"));
	}

	@Test
	public final void testDataStyleCreateTwice() {
		Assert.assertTrue(this.stylesEntry.addDataStyle(this.ds1, Mode.CREATE));
		Assert.assertFalse(this.stylesEntry.addDataStyle(this.ds2, Mode.CREATE));
		Map<String, DataStyle> dataStyles = this.stylesEntry.getDataStyles();
		Assert.assertEquals(1,
				dataStyles.size());
		Assert.assertEquals(this.ds1,
				dataStyles.get("a"));
	}
	
	@Test
	public final void testDataStyleCreateThenUpdate() {
		Assert.assertTrue(this.stylesEntry.addDataStyle(this.ds1, Mode.CREATE));
		Assert.assertTrue(this.stylesEntry.addDataStyle(this.ds2, Mode.UPDATE));
		Map<String, DataStyle> dataStyles = this.stylesEntry.getDataStyles();
		Assert.assertEquals(1,
				dataStyles.size());
		Assert.assertEquals(this.ds2,
				dataStyles.get("a"));
	}
	
	@Test
	public final void testDataStyleUpdate() {
		Assert.assertFalse(this.stylesEntry.addDataStyle(this.ds2, Mode.UPDATE));
		Map<String, DataStyle> dataStyles = this.stylesEntry.getDataStyles();
		Assert.assertEquals(0,
				dataStyles.size());
	}
	
	@Test
	public final void testDataStyleCreateThenUpdateIfExists() {
		Assert.assertTrue(this.stylesEntry.addDataStyle(this.ds1, Mode.CREATE));
		Assert.assertTrue(this.stylesEntry.addDataStyle(this.ds2, Mode.UPDATE_IF_EXISTS));
		Map<String, DataStyle> dataStyles = this.stylesEntry.getDataStyles();
		Assert.assertEquals(1,
				dataStyles.size());
		Assert.assertEquals(this.ds2,
				dataStyles.get("a"));
	}
	
	@Test
	public final void testDataStyleUpdateIfExists() {
		Assert.assertTrue(this.stylesEntry.addDataStyle(this.ds2, Mode.UPDATE_IF_EXISTS));
		Map<String, DataStyle> dataStyles = this.stylesEntry.getDataStyles();
		Assert.assertEquals(1,
				dataStyles.size());
		Assert.assertEquals(this.ds2,
				dataStyles.get("a"));
	}

	@Test
	public final void testPageStyleCreateTwice() {
		Assert.assertTrue(this.stylesEntry.addPageStyle(this.ps1, Mode.CREATE));
		Assert.assertFalse(this.stylesEntry.addPageStyle(this.ps2, Mode.CREATE));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesEntry.getPageStyles();
		Assert.assertEquals(1,
				masterPageStyles.size());
		Assert.assertEquals(this.ps1,
				masterPageStyles.get("a"));
	}
	
	@Test
	public final void testPageStyleCreateThenUpdate() {
		Assert.assertTrue(this.stylesEntry.addPageStyle(this.ps1, Mode.CREATE));
		Assert.assertTrue(this.stylesEntry.addPageStyle(this.ps2, Mode.UPDATE));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesEntry.getPageStyles();
		Assert.assertEquals(1,
				masterPageStyles.size());
		Assert.assertEquals(this.ps2,
				masterPageStyles.get("a"));
	}
	
	@Test
	public final void testPageStyleUpdate() {
		Assert.assertFalse(this.stylesEntry.addPageStyle(this.ps2, Mode.UPDATE));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesEntry.getPageStyles();
		Assert.assertEquals(0,
				masterPageStyles.size());
	}
	
	@Test
	public final void testPageStyleCreateThenUpdateIfExists() {
		Assert.assertTrue(this.stylesEntry.addPageStyle(this.ps1, Mode.CREATE));
		Assert.assertTrue(this.stylesEntry.addPageStyle(this.ps2, Mode.UPDATE_IF_EXISTS));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesEntry.getPageStyles();
		Assert.assertEquals(1,
				masterPageStyles.size());
		Assert.assertEquals(this.ps2,
				masterPageStyles.get("a"));
	}
	
	@Test
	public final void testPageStyleUpdateIfExists() {
		Assert.assertTrue(this.stylesEntry.addPageStyle(this.ps2, Mode.UPDATE_IF_EXISTS));
		Map<String, MasterPageStyle> masterPageStyles = this.stylesEntry.getPageStyles();
		Assert.assertEquals(1,
				masterPageStyles.size());
		Assert.assertEquals(this.ps2,
				masterPageStyles.get("a"));
	}
}
