package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.style.DrawStyle;
import com.github.jferard.fastods.style.TextStyle;

public class DrawFrameBuilder {
    private final FrameContent content;
    private final Length x;
    private final Length y;
    private final Length width;
    private final Length height;
    private TextStyle textStyle;
    private DrawStyle drawStyle;
    private int zIndex;

    DrawFrameBuilder(final FrameContent content, final Length x, final Length y, final Length width,
                     final Length height) {
        this.content = content;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zIndex = 0;
        this.drawStyle = null;
        this.textStyle = null;
    }

    public DrawFrameBuilder zIndex(final int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    public DrawFrameBuilder style(final DrawStyle drawStyle) {
        this.drawStyle = drawStyle;
        return this;
    }

    public DrawFrameBuilder textStyle(final TextStyle textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    public DrawFrame build() {
        return new DrawFrame(this.content, this.x, this.y, this.width, this.height,
                this.zIndex, this.drawStyle, this.textStyle);
    }
}
