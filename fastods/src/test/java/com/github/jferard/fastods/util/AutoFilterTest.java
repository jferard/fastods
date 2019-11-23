package com.github.jferard.fastods.util;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TestHelper;
import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class AutoFilterTest {
    @Test
    public void test() throws IOException {
        final Table table = PowerMock.createMock(Table.class);
        final Filter filter = PowerMock.createMock(Filter.class);

        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("t");
        filter.appendXMLContent(EasyMock.isA(XMLUtil.class), EasyMock.isA(Appendable.class));

        PowerMock.replayAll();
        final AutoFilter af =
                AutoFilter.builder("my_range", table, 0, 1, 2, 3).filter(filter).hideButtons()
                        .build();
        TestHelper.assertXMLEquals("<table:database-range table:name=\"my_range\" " +
                "table:display-filter-buttons=\"false\" table:target-range-address=\"t" +
                ".B1:D3\"><table:filter></table:filter></table:database-range>", af);

        PowerMock.verifyAll();
    }

}