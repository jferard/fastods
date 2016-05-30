package com.github.jferard.fastods.style;

import java.io.IOException;

import com.github.jferard.fastods.NamedObject;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.XMLUtil;

public interface DataStyle extends NamedObject {
	void addToFile(OdsFile file);
	
	void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException;
}
