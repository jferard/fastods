package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.DrawStyle;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

public class DrawFrame implements Shape {
    public static DrawFrameBuilder builder(final FrameContent content, final Length x,
                                           final Length y, final Length width,
                                           final Length height) {
        return new DrawFrameBuilder(content, x, y, width, height);
    }

    private final FrameContent content;
    private final Length x;
    private final Length y;
    private final Length width;
    private final Length height;
    private final int zIndex;
    private final DrawStyle drawStyle;
    private final TextStyle textStyle;

    public DrawFrame(final FrameContent content, final Length x, final Length y, final Length width,
                     final Length height, final int zIndex, final DrawStyle drawStyle,
                     final TextStyle textStyle) {
        this.content = content;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
        this.drawStyle = drawStyle;
        this.textStyle = textStyle;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<draw:frame");
        this.appendAttributes(util, appendable);
        appendable.append(">");
        this.content.appendXMLContent(util, appendable);
        appendable.append("</draw:frame>");
    }

    public void appendAttributes(final XMLUtil util, final Appendable appendable)
            throws IOException {
        util.appendAttribute(appendable, "draw:name", "Frame 1");
        util.appendAttribute(appendable, "draw:z-index", this.zIndex);
        if (this.drawStyle != null) {
            util.appendAttribute(appendable, "draw:style-name", this.drawStyle.getName());
        }
        if (this.textStyle != null) {
            util.appendAttribute(appendable, "draw:text-style-name", this.textStyle.getName());
        }
        util.appendAttribute(appendable, "svg:width", this.width);
        util.appendAttribute(appendable, "svg:height", this.height);
        util.appendAttribute(appendable, "svg:x", this.x);
        util.appendAttribute(appendable, "svg:y", this.y);
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        if (this.drawStyle != null) {
            stylesContainer.addContentStyle(this.drawStyle);
        }
        if (this.textStyle != null) {
            stylesContainer.addContentStyle(this.textStyle);
        }
    }
}
