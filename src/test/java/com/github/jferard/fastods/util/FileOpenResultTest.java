package com.github.jferard.fastods.util;

import com.github.jferard.fastods.OdsFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 */
public class FileOpenResultTest {
	private OdsFactory odsFactory;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		this.odsFactory = new OdsFactory();
	}

	@Test
	public void testIsDir() throws Exception {
		FileOpenResult fileOpenResult = odsFactory.openFile(".");

		Assert.assertTrue(FileOpenResult.FILE_IS_DIR == fileOpenResult);
		thrown.expect(IllegalStateException.class);
		fileOpenResult.getStream();
	}

	@Test
	public void testOpen() throws Exception {
		File aTest = new File("atest");

		try {
			FileOpenResult fileOpenResult = odsFactory.openFile(aTest.getAbsolutePath());
			Assert.assertTrue(fileOpenResult instanceof FileOpen);
			Assert.assertEquals(0, aTest.length());
			// the file has been touched
			OutputStream s = fileOpenResult.getStream();
			s.write(new byte[]{'a', 'b','c'});
			s.close();
			Assert.assertEquals(3, aTest.length());
		} finally {
			aTest.delete();
		}
	}

	@Test
	public void testExists() throws Exception {
		File aTest = new File("atest");
		odsFactory = new OdsFactory();

		// create a file
		OutputStream s = new FileOutputStream(aTest);
		s.write(new byte[]{'a', 'b','c'});
		s.close();
		Assert.assertEquals(3, aTest.length());
		try {
			FileOpenResult fileOpenResult = odsFactory.openFile(aTest.getAbsolutePath());
			Assert.assertTrue(fileOpenResult instanceof FileExists);
			// the file has not been touched
			Assert.assertEquals(3, aTest.length());
			// the file has been touched
			s = fileOpenResult.getStream();
			s.close();
			Assert.assertEquals(0, aTest.length());
		} finally {
			aTest.delete();
		}
	}
}