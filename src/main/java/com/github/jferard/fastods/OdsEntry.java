package com.github.jferard.fastods;

import java.io.IOException;
import java.io.Writer;
import java.util.zip.ZipOutputStream;

public interface OdsEntry {
	void write(Util util, ZipOutputStream out, Writer writer) throws IOException;
}
