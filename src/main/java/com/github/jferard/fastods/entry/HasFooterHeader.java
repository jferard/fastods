package com.github.jferard.fastods.entry;

class HasFooterHeader {
	private final boolean hasHeader;
	private final boolean hasFooter;

	HasFooterHeader(boolean hasHeader, boolean hasFooter) {
		this.hasHeader = hasHeader;
		this.hasFooter = hasFooter;
	}

	public boolean hasHeader() {
		return this.hasHeader;
	}

	public boolean hasFooter() {
		return this.hasFooter;
	}
}
