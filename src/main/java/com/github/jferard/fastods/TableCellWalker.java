package com.github.jferard.fastods;

public interface TableCellWalker extends TableCell {
	boolean hasNext();

	boolean hasPrevious();

	void next();

	void nextCell();

	void previous();

	void to(final int i);
}
