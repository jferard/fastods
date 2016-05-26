package com.github.jferard.fastods;

import java.io.IOException;

public interface StyleTag {
	void appendXMLToContentEntry(Util util, Appendable appendable)
			throws IOException;

	String getName();
}
