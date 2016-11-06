package com.github.jferard.fastods;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class OdsFileTest {

	private DataStyleBuilderFactory dataStyleBuilderFactory;
	private ByteArrayOutputStream os;
	
	@Before
	public final void setUp() {
		this.dataStyleBuilderFactory = new DataStyleBuilderFactory(
				new XMLUtil(new FastOdsXMLEscaper()), Locale.US);
		this.os = new ByteArrayOutputStream();
	}

	@Test
	public final void test() throws UnsupportedEncodingException {
		OdsFile f = OdsFile.create(Locale.US, "file");
		DataStyle ds = this.dataStyleBuilderFactory
						.booleanStyleBuilder("b").build();
		f.addDataStyle(ds);
		f.save(this.os);
		Assert.assertEquals("", this.os.toString("ASCII"));
	}

}
