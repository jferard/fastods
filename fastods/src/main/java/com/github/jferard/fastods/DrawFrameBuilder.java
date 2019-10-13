package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.style.TextStyle;

/**
 * A builder for DrawFrame class
 */
public class DrawFrameBuilder {
    private final String name;
    private final FrameContent content;
    private final Length x;
    private final Length y;
    private final Length width;
    private final Length height;
    private TextStyle textStyle;
    private GraphicStyle drawStyle;
    private int zIndex;

    /**
     * @param name    the name of the frame
     * @param content the content of the frame
     * @param x       the x position of the frame
     * @param y       the y position of the frame
     * @param width   the width of the frame
     * @param height  the height of the frame
     */
    DrawFrameBuilder(final String name, final FrameContent content, final Length x, final Length y,
                     final Length width, final Length height) {
        this.name = name;
        this.content = content;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zIndex = 0;
        this.drawStyle = null;
        this.textStyle = null;
    }

    /**
     * @param zIndex the new z index
     * @return this for fluent style
     */
    public DrawFrameBuilder zIndex(final int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    /**
     * @param drawStyle the style of the frame
     * @return this for fluent style
     */
    public DrawFrameBuilder style(final GraphicStyle drawStyle) {
        this.drawStyle = drawStyle;
        return this;
    }

    /**
     * @param textStyle the style of the text
     * @return this for fluent style
     */
    public DrawFrameBuilder textStyle(final TextStyle textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    /**
     * @return the frame
     */
    public DrawFrame build() {
        return new DrawFrame(this.name, this.content, this.x, this.y, this.width, this.height,
                this.zIndex, this.drawStyle, this.textStyle);
    }
}
