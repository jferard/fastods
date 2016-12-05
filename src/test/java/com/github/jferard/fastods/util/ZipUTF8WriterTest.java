package com.github.jferard.fastods.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class ZipUTF8WriterTest {

	private ZipUTF8Writer zipUTF8Writer;
	private StringWriter writer;
	private ByteArrayOutputStream out;

	@Before
	public void setUp() throws Exception {
		this.writer = new StringWriter();
		this.out = new ByteArrayOutputStream();
		this.zipUTF8Writer = new ZipUTF8Writer(new ZipOutputStream(this.out), this.writer);
	}

	@Test
	public final void test() throws IOException {
		PowerMock.replayAll();
		this.zipUTF8Writer.setComment("comment");
		this.zipUTF8Writer.putNextEntry(new ZipEntry("a"));
		this.zipUTF8Writer.append("text", 0, 2);
		Assert.assertEquals(this.writer.toString(), "te");
		Assert.assertEquals(31, this.out.toByteArray().length);
		this.zipUTF8Writer.closeEntry();
		this.zipUTF8Writer.finish();
		
		PowerMock.verifyAll();
	}

}
