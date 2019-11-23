package com.github.jferard.fastods.util;

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.FieldOrientation;
import com.github.jferard.fastods.attribute.PilotStandardFunction;
import org.junit.Test;

import java.io.IOException;

public class PilotTableFieldTest {
    @Test
    public void test() throws IOException {
        final PilotTableField field = new PilotTableField("sfn", FieldOrientation.COLUMN, 1, true,
                PilotStandardFunction.AVERAGE, new PilotTableLevel(false));

        TestHelper.assertXMLEquals("<table:data-pilot-field table:source-field-name=\"sfn\" " +
                "table:orientation=\"column\" table:used-hierarchy=\"1\" " +
                "table:is-data-layout-field=\"true\" table:function=\"average\">" +
                "<table:data-pilot-level table:show-empty=\"false\">" +
                "<table:data-pilot-display-info table:enabled=\"false\" " +
                "table:display-member-mode=\"from-top\" table:member-count=\"0\" " +
                "table:data-field=\"\"/>" +
                "<table:data-pilot-sort-info table:order=\"ascending\" " +
                "table:sort-mode=\"name\"/>" +
                "<table:data-pilot-layout-info table:add-empty-lines=\"false\" " +
                "table:layout-mode=\"tabular-layout\"/>" + "</table:data-pilot-level>" +
                "</table:data-pilot-field>", field);
    }
}