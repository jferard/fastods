package com.github.jferard.fastods;

import com.github.jferard.fastods.ref.RangeRef;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.List;

/**
 * 10.4.6.2<draw:object>
 */
public class DrawObject implements FrameContent {
    private final String href;
    private final List<RangeRef> updateRanges;

    /**
     * @param href         the href
     * @param updateRanges the ranges
     */
    public DrawObject(final String href, final List<RangeRef> updateRanges) {
        this.href = href;
        this.updateRanges = updateRanges;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<draw:object");
        util.appendAttribute(appendable, "xlink:href", this.href);
        util.appendAttribute(appendable, "draw:notify-on-update-of-ranges", this.updateRanges, " ");
        util.appendAttribute(appendable, "xlink:type", "simple");
        util.appendAttribute(appendable, "xlink:show", "embed");
        util.appendAttribute(appendable, "xlink:actuate", "onLoad");
        appendable.append("/>");
    }
}
