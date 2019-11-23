package com.github.jferard.fastods;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 10.4.4<draw:image>
 */
public class DrawImage implements FrameContent {
    private final String href;

    /**
     * @param href the ref of the image
     */
    public DrawImage(final String href) {
        this.href = href;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<draw:image");
        util.appendAttribute(appendable, "xlink:href", this.href);
        util.appendAttribute(appendable, "xlink:type", "simple");
        util.appendAttribute(appendable, "xlink:show", "embed");
        util.appendAttribute(appendable, "xlink:actuate", "onLoad");
        appendable.append("/>");
    }
}
