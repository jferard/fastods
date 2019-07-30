package com.github.jferard.fastods.util;

import com.github.jferard.fastods.TestHelper;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Arrays;

public class PilotTableTest {
    private PilotTableField field1;
    private XMLUtil util;

    @Before public void setUp() {
        this.field1 = PowerMock.createMock(PilotTableField.class);
        this.util = XMLUtil.create();
    }

    @Test public void test() throws IOException {
        final PilotTable pt =
                PilotTable.builder("n", "scr", "tr", Arrays.asList("b1", "b2")).field(this.field1)
                        .build();

        PowerMock.resetAll();
        this.field1.appendXMLContent(EasyMock.isA(XMLUtil.class), EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        TestHelper.assertXMLEquals("<table:data-pilot-table table:name=\"n\" " +
                "table:application-data=\"\" table:target-range-address=\"tr\" table:buttons=\"b1" +
                " b2\" table:show-filter-button=\"true\" " +
                "table:drill-down-on-double-click=\"false\">" +
                "<table:source-cell-range table:cell-range-address=\"scr\"/>" +
                "</table:data-pilot-table>", pt);

        PowerMock.verifyAll();
    }
}