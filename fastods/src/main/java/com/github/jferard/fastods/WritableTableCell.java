package com.github.jferard.fastods;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

public interface WritableTableCell extends TableCell {
    /**
     * Generate the XML for the table cell.
     *
     * @param util       an util.
     * @param appendable the appendable to fill
     * @throws IOException if an error occurs
     */
    void appendXMLToTableRow(XMLUtil util, Appendable appendable)
            throws IOException;
}
