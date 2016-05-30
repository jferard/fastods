package com.github.jferard.fastods.style;

import java.io.IOException;

import com.github.jferard.fastods.util.XMLUtil;

public interface StyleTag {
	void appendXMLToContentEntry(XMLUtil util, Appendable appendable)
			throws IOException;

	String getName();
}
