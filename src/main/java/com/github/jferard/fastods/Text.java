package com.github.jferard.fastods;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.util.XMLUtil;

public class Text {
	private List<Paragraph> paragraphs;
	private Set<FHTextStyle> textStyles;

	public Text(List<Paragraph> paragraphs, Set<FHTextStyle> textStyles) {
		this.paragraphs = paragraphs;
		this.textStyles = textStyles;
	}

	public boolean isEmpty() {
		return this.paragraphs.isEmpty();
	}

	public void appendXMLToMasterStyle(XMLUtil util, Appendable appendable) throws IOException {
		for (final Paragraph paragraph : this.paragraphs) {
			if (paragraph == null)
				appendable.append("<text:p/>");
			else {
				paragraph.appendXMLToRegionBody(util, appendable);
			}
		}
	}
	
	public void appendTextStylesXMLToAutomaticStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		for (final FHTextStyle style : this.textStyles) {
			appendable.append("<style:style");
			util.appendEAttribute(appendable, "style:name", style.getName());
			util.appendAttribute(appendable, "style:family", "text");
			appendable.append('>');
			style.appendXMLToContentEntry(util, appendable);
			appendable.append("</style:style>");
		}
	}
}
