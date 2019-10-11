package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileUtilTest {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    public void testCopy() throws IOException {
        this.testCopyAux("FastODS", 2, 4);
    }

    @Test
    public void testCopy2() throws IOException {
        this.testCopyAux("FastODS", 8, 2);
    }

    @Test
    public void testCopy3() throws IOException {
        this.testCopyAux("FastODSFastODSFastODSFastODSFastODSFastODSFastODS", 4, 4);
    }

    private void testCopyAux(final String text, final int bufferSize, final int startSize)
            throws IOException {
        final byte[] expectedBytes = text.getBytes(UTF_8);
        final FileUtil fu = new FileUtil(bufferSize, startSize);
        final byte[] actualBytes = fu.readStream(new ByteArrayInputStream(expectedBytes));
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }
}