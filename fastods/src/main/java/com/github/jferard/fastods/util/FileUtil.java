package com.github.jferard.fastods.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A helper class to copy files
 */
public class FileUtil {
    private static final int BUFFER_SIZE = 16 * 4096;
    private static final int START_SIZE = 16 * BUFFER_SIZE;

    /**
     * @return a new FileUtil
     */
    public static FileUtil create() {
        return new FileUtil(BUFFER_SIZE, START_SIZE);
    }

    private final int bufferSize;
    private final int startSize;

    /**
     * @param bufferSize the size of the cop)y buffer
     * @param startSize the initial size of the byte array
     */
    public FileUtil(final int bufferSize, final int startSize) {
        this.bufferSize = bufferSize;
        this.startSize = startSize;
    }

    /**
     * Fills a byte array with the content of a file.
     *
     * @param file the file to read
     * @return the byte array
     * @throws IOException if an I/O occurs
     */
    public byte[] readFile(final File file) throws IOException {
        final InputStream is = new FileInputStream(file);
        try {
            return this.readStream(is, (int) file.length());
        } finally {
            is.close();
        }
    }

    /**
     * Fills a byte array with the content of a stream.
     *
     * @param is the stream to read
     * @return the byte array
     * @throws IOException if an I/O occurs
     */
    public byte[] readStream(final InputStream is) throws IOException {
        return this.readStream(is, this.startSize);
    }

    /**
     * Fills a byte array with the content of a stream.
     *
     * @param is the stream to read
     * @param customStartSize the initial size of the byte array
     * @return the byte array
     * @throws IOException if an I/O occurs
     */
    public byte[] readStream(final InputStream is, final int customStartSize) throws IOException {
        byte[] bytes = new byte[customStartSize];
        int total_count = 0;
        while (true) {
            while (total_count + this.bufferSize >= bytes.length) {
                final byte[] new_bytes = new byte[2 * bytes.length];
                System.arraycopy(bytes, 0, new_bytes, 0, total_count);
                bytes = new_bytes;
            }
            final int count = is.read(bytes, total_count, this.bufferSize);
            if (count == -1) {
                break;
            }
            total_count += count;
        }
        final byte[] new_bytes = new byte[total_count];
        System.arraycopy(bytes, 0, new_bytes, 0, total_count);
        return new_bytes;
    }
}
