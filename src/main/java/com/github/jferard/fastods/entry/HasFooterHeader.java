package com.github.jferard.fastods.entry;

class HasFooterHeader {
	private final boolean hasFooter;
	private final boolean hasHeader;

	HasFooterHeader(final boolean hasHeader, final boolean hasFooter) {
		this.hasHeader = hasHeader;
		this.hasFooter = hasFooter;
	}

	public boolean hasFooter() {
		return this.hasFooter;
	}

	public boolean hasHeader() {
		return this.hasHeader;
	}
}
