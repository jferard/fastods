package com.github.jferard.fastods;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

public class Text {
	public static final String TEXT_DATE = "<text:date/>";
	public static final String TEXT_FILE_NAME = "<text:file-name/>";
	public static final String TEXT_PAGE_COUNT = "<text:page-count>99</text:page-count>";
	public static final String TEXT_PAGE_NUMBER = "<text:page-number>1</text:page-number>";
	public static final String TEXT_SHEET_NAME = "<text:sheet-name/>";
	public static final String TEXT_TIME = "<text:time/>";
	
	private List<Paragraph> paragraphs;
	private Set<TextStyle> textStyles;

	public Text(List<Paragraph> paragraphs, Set<TextStyle> textStyles) {
		this.paragraphs = paragraphs;
		this.textStyles = textStyles;
	}
	
	

	public boolean isEmpty() {
		return this.paragraphs.isEmpty();
	}

	public void appendXMLContent(XMLUtil util, Appendable appendable)
			throws IOException {
		for (final Paragraph paragraph : this.paragraphs) {
			if (paragraph == null)
				appendable.append("<text:p/>");
			else {
				paragraph.appendXMLContent(util, appendable);
			}
		}
	}

	public void appendTextStylesXMLToAutomaticStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		for (final TextStyle style : this.textStyles) {
			appendable.append("<style:style");
			util.appendEAttribute(appendable, "style:name", style.getName());
			util.appendAttribute(appendable, "style:family", "text");
			appendable.append('>');
			style.appendXMLToContentEntry(util, appendable);
			appendable.append("</style:style>");
		}
	}

	public static Text styledContent(final String text,
			final TextStyle ts) {
		return Text.builder().parStyledContent(text, ts).build();
	}

	public static Text content(final String text) {
		return Text.builder().parContent(text).build();
	}
	
	public static TextBuilder builder() {
		return new TextBuilder();
	}
}
