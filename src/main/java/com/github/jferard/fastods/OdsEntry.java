package com.github.jferard.fastods;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

public interface OdsEntry {

	// TODO : Writer (buffered)
	void write(Util util, ZipOutputStream out) throws IOException;
}
