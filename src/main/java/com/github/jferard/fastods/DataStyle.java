package com.github.jferard.fastods;

import java.io.IOException;

import com.github.jferard.fastods.util.XMLUtil;

public interface DataStyle extends NamedObject {
	void addToFile(OdsFile file);
	
	void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException;
}
