/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods.it;

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.NamedOdsDocument;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.PageSection;
import com.github.jferard.fastods.PageSectionContent.Region;
import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableRowImpl;
import com.github.jferard.fastods.Text;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testlib.OdfToolkitUtil;
import com.github.jferard.fastods.testlib.Util;
import com.github.jferard.fastods.util.ColorHelper;
import com.github.jferard.fastods.util.SimpleLength;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.OdfStylesDom;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.text.Paragraph;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 */
public class OdsFileWithHeaderAndFooterCreationIT {
    public static final String GENERATED_FILES = "generated_files";
    public static final String FASTODS_FOOTER_HEADER_ODS = "fastods_footer_header.ods";
    public static final String FASTODS_FOOTER_HEADER_WITH_FLUSH_ODS =
            "fastods_footer_header_with_flush.ods";
    public static final String LEFT_HEADER_PATH = "//style:master-page[@style:name='test-master" +
            "-page']//style:header//style:region-left";
    public static final String CENTER_HEADER_PATH =
            "//style:master-page[@style:name='test-master-page']//style:header//style:region" +
                    "-center";
    public static final String LEFT_FOOTER_PATH = "//style:master-page[@style:name='test-master" +
            "-page']//style:footer//style:region-left";
    public static final String CENTER_FOOTER_PATH =
            "//style:master-page[@style:name='test-master-page']//style:footer//style:region" +
                    "-center";
    private static final String RIGHT_HEADER_PATH = "//style:master-page[@style:name='test-master" +
            "-page']//style:header//style:region-right";
    private static final String RIGHT_FOOTER_PATH = "//style:master-page[@style:name='test-master" +
            "-page']//style:footer//style:region-right";

    @BeforeClass
    public static void beforeClass() {
        Util.mkdir(GENERATED_FILES);
    }

    private Logger logger;
    private OdsFactory odsFactory;
    private TextStyle lts;
    private TextStyle cts;
    private TextStyle rts;
    private TextStyle boldStyle;
    private TextStyle italicStyle;
    private Text leftHeader;
    private Text centerHeader;
    private Text rightHeader;
    private Header header;
    private Text leftFooter;
    private Text centerFooter;
    private Text rightFooter;
    private PageStyle ps;
    private TableStyle ttts;
    private PageStyle ps2;
    private TableStyle ttts2;
    private TableRowStyle trs;
    private TableCellStyle tcls;
    private TableColumnStyle tcns;
    private Footer footer;

    @Before
    public void setUp() {
        this.logger = Logger.getLogger("footer and header");
        this.odsFactory = OdsFactory.create(this.logger, Locale.US);
        this.logger.info("Creating a file with footer and header");
    }

    @After
    public void tearDown() {
        // let's display logging infos
        final Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.FINEST);
        for (final Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.FINEST);
        }
    }

    @Test
    public final void createDocumentWithFooterAndHeaderIT() throws Exception {
        this.createDocumentWithFooterAndHeader();
        this.validateDocument(FASTODS_FOOTER_HEADER_ODS);
    }

    @Test
    public final void createDocumentWithFooterAndHeaderWithFlushIT() throws Exception {
        this.createDocumentWithFooterAndHeaderWithFlush();
        this.validateDocument(FASTODS_FOOTER_HEADER_WITH_FLUSH_ODS);
    }


    private void validateDocument(final String documentName) throws Exception {
        final SpreadsheetDocument document = SpreadsheetDocument
                .loadDocument(new File(GENERATED_FILES, documentName));
        Assert.assertEquals(3, document.getSheetCount());

        // STYLES
        final OdfStylesDom stylesDom = document.getStylesDom();
        Assert.assertEquals("#dddddd", stylesDom.getXPath()
                .evaluate("//style:style[@style:name='cc']//@fo:background-color",
                        stylesDom.getRootElement()));
        Assert.assertEquals("bold", stylesDom.getXPath()
                .evaluate("//style:style[@style:name='cc']//@fo:font-weight",
                        stylesDom.getRootElement()));

        Assert.assertEquals(SimpleColor.RED.hexValue(), stylesDom.getXPath()
                .evaluate("//style:style[@style:name='red-text']//@fo:color",
                        stylesDom.getRootElement()));
        Assert.assertEquals(SimpleColor.BLUE.hexValue(), stylesDom.getXPath()
                .evaluate("//style:style[@style:name='blue-text']//@fo:color",
                        stylesDom.getRootElement()));
        Assert.assertEquals(SimpleColor.GREEN.hexValue(), stylesDom.getXPath()
                .evaluate("//style:style[@style:name='green-text']//@fo:color",
                        stylesDom.getRootElement()));

        Assert.assertEquals("left header",
                stylesDom.getXPath().evaluate(LEFT_HEADER_PATH, stylesDom.getRootElement()));
        Assert.assertEquals("red-text", stylesDom.getXPath()
                .evaluate(LEFT_HEADER_PATH + "//@text:style-name", stylesDom.getRootElement()));

        Assert.assertEquals("center header1",
                stylesDom.getXPath().evaluate(CENTER_HEADER_PATH, stylesDom.getRootElement()));
        Assert.assertEquals("blue-text", stylesDom.getXPath()
                .evaluate(CENTER_HEADER_PATH + "//@text:style-name", stylesDom.getRootElement()));

        Assert.assertEquals("right header",
                stylesDom.getXPath().evaluate(RIGHT_HEADER_PATH, stylesDom.getRootElement()));
        Assert.assertEquals("green-text", stylesDom.getXPath()
                .evaluate(RIGHT_HEADER_PATH + "//@text:style-name", stylesDom.getRootElement()));

        Assert.assertEquals("left footer",
                stylesDom.getXPath().evaluate(LEFT_FOOTER_PATH, stylesDom.getRootElement()));
        Assert.assertEquals("blue-text", stylesDom.getXPath()
                .evaluate(LEFT_FOOTER_PATH + "//@text:style-name", stylesDom.getRootElement()));

        Assert.assertEquals("center footer99",
                stylesDom.getXPath().evaluate(CENTER_FOOTER_PATH, stylesDom.getRootElement()));
        Assert.assertEquals("green-text", stylesDom.getXPath()
                .evaluate(CENTER_FOOTER_PATH + "//@text:style-name", stylesDom.getRootElement()));

        Assert.assertEquals("right footer",
                stylesDom.getXPath().evaluate(RIGHT_FOOTER_PATH, stylesDom.getRootElement()));
        Assert.assertEquals("red-text", stylesDom.getXPath()
                .evaluate(RIGHT_FOOTER_PATH + "//@text:style-name", stylesDom.getRootElement()));

        final OdfContentDom contentDom = document.getContentDom();
        Assert.assertEquals("5cm", contentDom.getXPath().evaluate(
                "//style:style[@style:name='rr']//style:table-row-properties/@style:row-height",
                contentDom.getRootElement()));
        Assert.assertEquals("10cm", contentDom.getXPath().evaluate(
                "//style:style[@style:name='ccs']//style:table-column-properties/@style:column" +
                        "-width", contentDom.getRootElement()));

        Assert.assertEquals("bold", contentDom.getXPath()
                .evaluate("//style:style[@style:name='bold-text']//@fo:font-weight",
                        contentDom.getRootElement()));
        Assert.assertEquals("italic", contentDom.getXPath()
                .evaluate("//style:style[@style:name='italic-text']//@fo:font-style",
                        contentDom.getRootElement()));

        Assert.assertEquals("table", contentDom.getXPath()
                .evaluate("//style:style[@style:name='test-table-style']//@style:family",
                        contentDom.getRootElement()));
        Assert.assertEquals("test-master-page", contentDom.getXPath()
                .evaluate("//style:style[@style:name='test-table-style']//@style:master-page-name",
                        contentDom.getRootElement()));
        Assert.assertEquals("test-master-page", contentDom.getXPath()
                .evaluate("//style:style[@style:name='test2-table-style']//@style:master-page-name",
                        contentDom.getRootElement()));

        // TABLE 1
        final org.odftoolkit.simple.table.Table sheet = document.getSheetByName("test");
        Assert.assertNotNull(sheet);
        Assert.assertEquals(2, sheet.getRowCount());

        // FIRST ROW
        Row row = sheet.getRowByIndex(0);
        Cell cell = row.getCellByIndex(0);
        Assert.assertEquals("", OdfToolkitUtil.getStringValue(cell));
        final Iterator<Paragraph> it = cell.getParagraphIterator();
        Assert.assertEquals("This is a", it.next().getTextContent());
        Assert.assertEquals("multiline",
                it.next().getOdfElement().getFirstElementChild().getTextContent());
        Assert.assertEquals("cell",
                it.next().getOdfElement().getFirstElementChild().getTextContent());

        Assert.assertEquals("text2", OdfToolkitUtil.getStringValue(row.getCellByIndex(1)));
        Assert.assertEquals("text3", OdfToolkitUtil.getStringValue(row.getCellByIndex(2)));

        // SECOND ROW
        row = sheet.getRowByIndex(1);
        cell = row.getCellByIndex(0);
        Assert.assertEquals("", OdfToolkitUtil.getStringValue(cell));
        final NodeList nodes = cell.getParagraphIterator().next().getOdfElement().getChildNodes();
        Assert.assertEquals("before link to table: ", nodes.item(0).getTextContent());
        Assert.assertEquals("text:a", nodes.item(1).getNodeName());
        Assert.assertEquals("#target",
                nodes.item(1).getAttributes().getNamedItem("xlink:href").getNodeValue());
        Assert.assertEquals("simple",
                nodes.item(1).getAttributes().getNamedItem("xlink:type").getNodeValue());
        Assert.assertEquals("table", nodes.item(1).getTextContent());
        Assert.assertEquals(" after link to table", nodes.item(2).getTextContent());
    }

    private void createDocumentWithFooterAndHeader() throws IOException {
        this.createStyles();

        final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
        final OdsDocument document = writer.document();

        this.fillDocument(document);

        writer.saveAs(new File(GENERATED_FILES, FASTODS_FOOTER_HEADER_ODS));
    }

    private void fillDocument(final OdsDocument document) throws IOException {
        final Table table = document.addTable("test", 1, 5);
        table.setStyle(this.ttts);
        table.setColumnStyle(0, this.tcns);
        TableRowImpl row = table.getRow(0);
        row.setRowStyle(this.trs);
        row.setDefaultCellStyle(this.tcls);

        row = table.getRow(0);
        row.getOrCreateCell(0).setText(Text.builder().parContent("This is a")
                .parStyledContent("multiline", this.italicStyle)
                .parStyledContent("cell", this.boldStyle).build());
        row.getOrCreateCell(1).setStringValue("text2");
        row.getOrCreateCell(2).setStringValue("text3");
        row = table.getRow(1);
        row.getOrCreateCell(0).setText(
                Text.builder().par().span("before link to table: ").link("table", "#target")
                        .span(" after link to table").build());
        row.getOrCreateCell(1).setText(Text.builder().par().span("before link to url: ")
                .link("url", new URL("https://www.github.com/jferard/fastods"))
                .span(" after link to url").build());
        row.getOrCreateCell(2).setText(Text.builder().par().span("before link to file: ")
                .link("file", new File("generated_files", "readme_example.ods"))
                .span(" after link to file").build());


        final Table table2 = document.addTable("target", 1, 1);
        table2.setStyle(this.ttts2);

        final Table table3 = document.addTable("target2", 1, 1);
        table3.setStyle(this.ttts2);
    }

    private void createStyles() {
        this.trs = TableRowStyle.builder("rr").rowHeight(SimpleLength.cm(5.0)).build();
        this.tcls = TableCellStyle.builder("cc").backgroundColor(ColorHelper.fromString("#dddddd"))
                .fontWeightBold().build();
        this.tcns = TableColumnStyle.builder("ccs").columnWidth(SimpleLength.cm(10.0))
                .defaultCellStyle(this.tcls).build();

        this.lts = TextProperties.builder().fontColor(SimpleColor.RED).buildHiddenStyle("red-text");
        this.cts = TextProperties.builder().fontColor(SimpleColor.BLUE)
                .buildHiddenStyle("blue-text");
        this.rts = TextProperties.builder().fontColor(SimpleColor.GREEN)
                .buildHiddenStyle("green-text");
        this.boldStyle = TextProperties.builder().fontWeightBold().buildHiddenStyle("bold-text");
        this.italicStyle = TextProperties.builder().fontStyleItalic()
                .buildHiddenStyle("italic-text");

        this.leftHeader = Text.styledContent("left header", this.lts);
        this.centerHeader = Text.builder().par().styledSpan("center header", this.cts)
                .span(Text.TEXT_PAGE_NUMBER).build();
        this.rightHeader = Text.styledContent("right header", this.rts);
        this.header = PageSection.regionBuilder().region(Region.LEFT).text(this.leftHeader)
                .region(Region.CENTER).text(this.centerHeader).region(Region.RIGHT)
                .text(this.rightHeader).buildHeader();

        this.leftFooter = Text.styledContent("left footer", this.cts);
        this.centerFooter = Text.builder().par().styledSpan("center footer", this.rts)
                .span(Text.TEXT_PAGE_COUNT).build();
        this.rightFooter = Text.styledContent("right footer", this.lts);
        this.footer = PageSection.regionBuilder().region(Region.LEFT).text(this.leftFooter)
                .region(Region.CENTER).text(this.centerFooter).region(Region.RIGHT)
                .text(this.rightFooter).buildFooter();

        this.ps = PageStyle.builder("test-master-page").footer(this.footer).header(this.header)
                .build();
        this.ttts = TableStyle.builder("test-table-style").pageStyle(this.ps).build();

        this.ps2 = PageStyle.builder("test2-master-page")
                .masterPageStyle(this.ps.getMasterPageStyle())
                .pageLayoutStyle(this.ps.getPageLayoutStyle()).build();
        this.ttts2 = TableStyle.builder("test2-table-style").pageStyle(this.ps2).build();
    }

    private void createDocumentWithFooterAndHeaderWithFlush() throws IOException {
        this.createStyles();
        final NamedOdsFileWriter writer = this.odsFactory
                .createWriter(new File(GENERATED_FILES, FASTODS_FOOTER_HEADER_WITH_FLUSH_ODS));
        final NamedOdsDocument document = writer.document();
        document.addPageStyle(this.ps);
        document.addObjectStyle(this.ttts);
        document.addObjectStyle(this.ttts2);
        document.addObjectStyle(this.lts);
        document.addObjectStyle(this.cts);
        document.addObjectStyle(this.rts);
        document.addStyleToContentAutomaticStyles(this.boldStyle);
        document.addStyleToContentAutomaticStyles(this.italicStyle);
        document.addObjectStyle(this.trs);
        document.addObjectStyle(this.tcls);
        document.addObjectStyle(this.tcns);
        document.freezeStyles();

        this.fillDocument(document);

        document.save();
    }
}
