package com.github.jferard.fastods.util;

import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.testlib.DomTester;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class MacroLibraryTest {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private MacroModule module;
    private XMLUtil util;

    @Before public void setUp() {
        this.module = PowerMock.createMock(MacroModule.class);
        this.util = XMLUtil.create();
    }

    @Test public void testIndexLine() throws IOException {
        final MacroLibrary lib = MacroLibrary.builder().name("ml").modules(this.module).readOnly().build();
        final StringBuilder sb = new StringBuilder();
        lib.appendIndexLine(XMLUtil.create(), sb);
        DomTester.assertEquals("<library:library library:name=\"ml\" library:link=\"false\"/>",
                sb.toString());
    }

    @Test public void testAdd() throws IOException {
        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final MacroLibrary lib = new MacroLibrary("ml", false, Arrays.asList(this.module));
        final Capture<StringBuilder> sb1 = Capture.newInstance();
        final Capture<byte[]> bs = Capture.newInstance();

        PowerMock.resetAll();
        document.addExtraDir("Basic/ml/");
        this.module.appendIndexLine(EasyMock.eq(this.util), EasyMock.capture(sb1));
        document.addExtraFile(EasyMock.eq("Basic/ml/script-lb.xml"), EasyMock.eq("text/xml"),
                EasyMock.capture(bs));
        this.module.add(this.util, document, "Basic/ml/");

        PowerMock.replayAll();
        lib.add(this.util, document);

        PowerMock.verifyAll();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE library:library " +
                "PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"library" +
                ".dtd\"><library:library xmlns:library=\"http://openoffice.org/2000/library\" " +
                "library:name=\"ml\" library:readonly=\"false\" " +
                "library:passwordprotected=\"false\"></library:library>", sb1.toString());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE library:library " +
                "PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"library" +
                ".dtd\"><library:library xmlns:library=\"http://openoffice.org/2000/library\" " +
                "library:name=\"ml\" library:readonly=\"false\" " +
                "library:passwordprotected=\"false\"></library:library>", new String(bs.getValue(), UTF_8));
    }
}