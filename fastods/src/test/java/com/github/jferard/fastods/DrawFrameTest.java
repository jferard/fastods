package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.SVGRectangle;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class DrawFrameTest {
    private XMLUtil util;
    private FrameContent content;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
        this.content = PowerMock.createMock(FrameContent.class);
    }


    @Test
    public void testContent() throws IOException {
        final StringBuilder sb = new StringBuilder();
        final DrawFrame frame =
                DrawFrame.builder("Frame 1", this.content, SVGRectangle.cm(1, 2, 3, 4)).build();

        PowerMock.resetAll();
        this.content.appendXMLContent(this.util, sb);

        PowerMock.replayAll();
        frame.appendXMLContent(this.util, sb);

        PowerMock.verifyAll();
        DomTester.assertEquals("<draw:frame draw:name=\"Frame 1\" draw:z-index=\"0\" " +
                "svg:width=\"3cm\" svg:height=\"4cm\" svg:x=\"1cm\" " +
                "svg:y=\"2cm\"></draw:frame>", sb.toString());
    }

    @Test
    public void testStyles() {
        final StylesContainer container = PowerMock.createMock(StylesContainer.class);
        final GraphicStyle gs = GraphicStyle.builder("gs").build();
        final DrawFrame frame =
                DrawFrame.builder("Frame 1", this.content, SVGRectangle.cm(1, 2, 3, 4)).style(gs)
                        .textStyle(TextStyle.DEFAULT_TEXT_STYLE).build();

        PowerMock.resetAll();
        EasyMock.expect(container.addContentStyle(gs)).andReturn(true);
        EasyMock.expect(container.addContentStyle(TextStyle.DEFAULT_TEXT_STYLE)).andReturn(true);

        PowerMock.replayAll();
        frame.addEmbeddedStyles(container);

        PowerMock.verifyAll();
    }
}