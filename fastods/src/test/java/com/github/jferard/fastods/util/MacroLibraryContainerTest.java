package com.github.jferard.fastods.util;

import com.github.jferard.fastods.OdsDocument;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Arrays;

public class MacroLibraryContainerTest {
    @Test public void test() throws IOException {
        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final MacroLibrary library = PowerMock.createMock(MacroLibrary.class);
        final MacroLibraryContainer container =
                new MacroLibraryContainer(XMLUtil.create(), Arrays.asList(library));
        final Capture<StringBuilder> sb1 = Capture.newInstance();
        final Capture<byte[]> bs = Capture.newInstance();

        PowerMock.resetAll();
        document.addExtraDir("Basic/");
        library.appendIndexLine(EasyMock.isA(XMLUtil.class), EasyMock.capture(sb1));
        document.addExtraFile(EasyMock.eq("Basic/script-lc.xml"), EasyMock.eq("text/xml"), EasyMock.capture(bs));
        library.add(EasyMock.isA(XMLUtil.class), EasyMock.eq(document));

        PowerMock.replayAll();
        container.add(document);

        PowerMock.verifyAll();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE library:libraries PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1" +
                ".0//EN\" \"libraries.dtd\">\n" +
                "<library:libraries xmlns:library=\"http://openoffice.org/2000/library\" " +
                "xmlns:xlink=\"http://www.w3.org/1999/xlink\"></library:libraries>", sb1.toString());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE library:libraries PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1" +
                ".0//EN\" \"libraries.dtd\">\n" +
                "<library:libraries xmlns:library=\"http://openoffice.org/2000/library\" " +
                "xmlns:xlink=\"http://www.w3.org/1999/xlink\"></library:libraries>", bs.toString());
    }
}