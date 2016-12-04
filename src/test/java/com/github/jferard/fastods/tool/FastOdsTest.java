package com.github.jferard.fastods.tool;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Desktop.class, FastOds.class })
public class FastOdsTest {

	@Test
	public final void testOpenDir() {
		PowerMock.replayAll();
		Assert.assertFalse(FastOds.openFile(new File(".")));
		PowerMock.verifyAll();
	}

	@Test
	public final void testOpenFile() throws IOException {
		PowerMock.mockStatic(Desktop.class);
		final Desktop d = PowerMock.createMock(Desktop.class);
		final File f = new File(".\\pom.xml");

		EasyMock.expect(Desktop.getDesktop()).andReturn(d);
		d.open(f);
		PowerMock.replayAll();
		Assert.assertTrue(FastOds.openFile(f));
		PowerMock.verifyAll();
	}

	@Test
	public final void testOpenFileError() throws IOException {
		// let's hide logging infos
		final Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.OFF);
		for (final Handler h : rootLogger.getHandlers())
			h.setLevel(Level.OFF);

		PowerMock.mockStatic(Desktop.class);
		final Desktop d = PowerMock.createMock(Desktop.class);
		final File f = new File(".\\pom.xml");

		EasyMock.expect(Desktop.getDesktop()).andReturn(d);
		d.open(f);
		EasyMock.expectLastCall().andThrow(new IOException());
		PowerMock.replayAll();
		Assert.assertFalse(FastOds.openFile(f));
		PowerMock.verifyAll();
	}

	@Test
	public final void testOpenNonExisting() {
		PowerMock.replayAll();
		Assert.assertFalse(FastOds.openFile(new File(".\\@")));
		PowerMock.verifyAll();
	}

	@Test
	public final void testXMLUtil() {
		PowerMock.replayAll();
		FastOds.getXMLUtil();
		FastOds.getXMLUtil();
		PowerMock.verifyAll();
	}
}
