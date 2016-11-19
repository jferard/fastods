package com.github.jferard.fastods;

import java.util.ArrayList;
import java.util.List;

import com.github.jferard.fastods.style.TextStyle;

public class ParagraphBuilder {

	private List<Span> spans;
	private TextStyle style;

	ParagraphBuilder() {
		this.spans = new ArrayList<Span>();
	}
	
	public Paragraph build() {
		return new Paragraph(this.spans, this.style);
	}

	public ParagraphBuilder styledSpan(TextStyle ts, String text) {
		final Span span = new Span(text, ts);
		this.spans.add(span);
		return this;
	}

	public ParagraphBuilder span(String text) {
		final Span span = new Span(text);
		this.spans.add(span);
		return this;
	}

	public ParagraphBuilder style(TextStyle ts) {
		this.style = ts;
		return this;
	}
}
