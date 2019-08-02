package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.ScriptEvent;
import org.junit.Test;

import java.io.IOException;

public class ScriptEventListenerTest {
    @Test
    public void testCreate() throws IOException {
        final ScriptEventListener sel = ScriptEventListener.create(ScriptEvent.ON_LOAD, "func");
        TestHelper.assertXMLEquals("<script:event-listener script:language=\"ooo:script\" " +
                "script:event-name=\"dom:load\" xlink:href=\"vnd.sun.star" +
                ".script:func?language=Basic&amp;location=document\" " + "xlink:type" +
                "=\"simple\"/>", sel);
    }

    @Test
    public void testBuilder() throws IOException {
        final ScriptEventListener sel =
                ScriptEventListener.builder(ScriptEvent.ON_LOAD, "func").language("l")
                        .genericLanguage("gl").build();
        TestHelper.assertXMLEquals("<script:event-listener script:language=\"gl\" " +
                "script:event-name=\"dom:load\" xlink:href=\"vnd.sun.star" +
                ".script:func?language=l&amp;location=document\" xlink:type=\"simple\"/>", sel);
    }
}