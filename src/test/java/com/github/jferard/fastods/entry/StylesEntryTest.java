package com.github.jferard.fastods.entry;

import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.entry.StylesContainer.Dest;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Container.Mode;
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
	private StylesContainer stylesContainer;

	@Before
	public void setUp() {
		this.stylesContainer = new StylesContainer();
		this.stylesEntry = new StylesEntry(this.stylesContainer);
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
	}
}
