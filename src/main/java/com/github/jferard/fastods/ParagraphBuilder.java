package com.github.jferard.fastods;

import java.util.ArrayList;
import java.util.List;

import com.github.jferard.fastods.style.TextStyle;

public class ParagraphBuilder {

	private final List<Span> spans;
	private TextStyle style;

	ParagraphBuilder() {
		this.spans = new ArrayList<Span>();
	}

	public Paragraph build() {
		return new Paragraph(this.spans, this.style);
	}

	public ParagraphBuilder span(final String text) {
		final Span span = new Span(text);
		this.spans.add(span);
		return this;
	}

	public ParagraphBuilder style(final TextStyle ts) {
		this.style = ts;
		return this;
	}

	public ParagraphBuilder styledSpan(final TextStyle ts, final String text) {
		final Span span = new Span(text, ts);
		this.spans.add(span);
		return this;
	}
}
