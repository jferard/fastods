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

package com.github.jferard.fastods.odselement.config;

/**
 * An element of the configuration.
 * See https://github.com/LibreOffice/core/blob/master/oox/source/token/properties.txt
 */
public enum ConfigElement {
    /**
     * com.sun.star.sheet.DocumentSettings and
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * controls whether formulas are displayed instead of their results
     */
    SHOW_FORMULAS("ShowFormulas", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables display of zero-values
     */
    SHOW_ZERO_VALUES("ShowZeroValues", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * controls whether strings, values, and formulas are displayed in different colors
     */
    IS_VALUE_HIGHLIGHTING_ENABLED("IsValueHighlightingEnabled", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.DocumentSettings and
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * controls whether a marker is shown for notes in cells
     */
    SHOW_NOTES("ShowNotes", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables the vertical scroll bar of the view
     */
    HAS_VERTICAL_SCROLL_BAR("HasVerticalScrollBar", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables the horizontal scroll bar of the view
     */
    HAS_HORIZONTAL_SCROLL_BAR("HasHorizontalScrollBar", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.DocumentSettings and
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables the sheet tabs of the view
     */
    HAS_SHEET_TABS("HasSheetTabs", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.DocumentSettings and
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables the display of outline symbols
     */
    IS_OUTLINE_SYMBOLS_SET("IsOutlineSymbolsSet", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.DocumentSettings and
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables the column and row headers of the view
     */
    HAS_COLUMN_ROW_HEADERS("HasColumnRowHeaders", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.DocumentSettings and
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables the display of the cell grid
     */
    SHOW_GRID("ShowGrid", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.DocumentSettings and
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * specifies the color in which the cell grid is displayed
     */
    GRID_COLOR("GridColor", ConfigElementType.LONG),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables display of help lines when moving drawing objects
     */
    SHOW_HELP_LINES("ShowHelpLines", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables display of anchor symbols when drawing objects are selected
     */
    SHOW_ANCHOR("ShowAnchor", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.DocumentSettings and
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables display of page breaks
     */
    SHOW_PAGE_BREAKS("ShowPageBreaks", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables display of embedded objects in the view
     */
    SHOW_OBJECTS("ShowObjects", ConfigElementType.SHORT),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables the display of charts in the view
     */
    SHOW_CHARTS("ShowCharts", ConfigElementType.SHORT),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * enables the display of drawing objects in the view
     */
    SHOW_DRAWING("ShowDrawing", ConfigElementType.SHORT),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * disables the display of marks from online spelling
     */
    HIDE_SPELL_MARKS("HideSpellMarks", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * This property defines the zoom type for the document
     */
    ZOOM_TYPE("ZoomType", ConfigElementType.SHORT),


    /**
     * com.sun.star.sheet.SpreadsheetViewSettings
     * <p>
     * Defines the zoom value to us
     */
    ZOOM_VALUE("ZoomValue", ConfigElementType.SHORT),

    /**
     * com.sun.star.document.Settings
     * <p>
     * gives access to the set of forbidden characters
     */
    FORBIDDEN_CHARACTERS("ForbiddenCharacters", ConfigElementType.LONG),


    /**
     * com.sun.star.document.Settings
     * <p>
     * specifies the update mode for links when loading text documents
     */
    LINK_UPDATE_MODE("LinkUpdateMode", ConfigElementType.SHORT ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * printer used by the document
     */
    PRINTER_NAME("PrinterName", ConfigElementType.STRING ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * platform and driver dependent printer setup data
     */
    PRINTER_SETUP("PrinterSetup", ConfigElementType.BASE64_BINARY ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * specifies if kerning is applied to Asian punctuation
     */
    IS_KERN_ASIAN_PUNCTUATION("IsKernAsianPunctuation", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * specifies the compression (character spacing) type used for Asian characters
     */
    CHARACTER_COMPRESSION_TYPE("CharacterCompressionType", ConfigElementType.SHORT ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * specifies if the user-specific settings saved within a document should be loaded with the document
     */
    APPLY_USER_DATA("ApplyUserData", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * specifies if a new version is created if a document has been modified and you want to close it
     */
    SAVE_VERSION_ON_CLOSE("SaveVersionOnClose", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * specifies if the document should be updated when the template it was created from changes
     */
    UPDATE_FROM_TEMPLATE("UpdateFromTemplate", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * specifies if fields in text documents are updated automatically
     */
    FIELD_AUTO_UPDATE("FieldAutoUpdate", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * The name of the globally registered com::sun::star::sdb::DataSource from which the current data is taken
     */
    CURRENT_DATABASE_DATA_SOURCE("CurrentDatabaseDataSource", ConfigElementType.STRING ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * a string value, specifying the name of the object displayed currently (or the SQL statement used)
     */
    CURRENT_DATABASE_COMMAND("CurrentDatabaseCommand", ConfigElementType.STRING ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * determines the interpretation of the property DataTableNam
     */
    CURRENT_DATABASE_COMMAND_TYPE("CurrentDatabaseCommandType", ConfigElementType.LONG ),


    /**
     * com.sun.star.document.Settings
     */
    DEFAULT_TAB_STOP("DefaultTabStop", ConfigElementType.LONG ),

    /**
     * com.sun.star.document.Settings
     * <p>
     * determines if the document will be printed as a booklet (brochure), i.e., two document pages are put together on one physical page, such that you can fold the print result and get a booklet
     */
    IS_PRINT_BOOKLET("IsPrintBooklet", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * is only effective, if IsPrintBooklet is TRUE
     */
    IS_PRINT_BOOKLET_FRONT("IsPrintBookletFront", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * is only effective, if IsPrintBooklet is TRUE
     */
    IS_PRINT_BOOKLET_BACK("IsPrintBookletBack", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * how printing uses col
     */
    PRINT_QUALITY("PrintQuality", ConfigElementType.LONG ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * contains the URL that points to a color table (file extension .soc) that will be used for showing a palette in dialogs using colors
     */
    COLOR_TABLE_URL("ColorTableURL", ConfigElementType.STRING ),


    /**
     * com.sun.star.document.Settings
     */
    DASH_TABLE_URL("DashTableURL", ConfigElementType.STRING ),

    /**
     * com.sun.star.document.Settings
     */
    LINE_END_TABLE_URL("LineEndTableURL", ConfigElementType.STRING ),

    /**
     * com.sun.star.document.Settings
     */
    HATCH_TABLE_URL("HatchTableURL", ConfigElementType.STRING ),

    /**
     * com.sun.star.document.Settings
     */
    GRADIENT_TABLE_URL("GradientTableURL", ConfigElementType.STRING ),

    /**
     * com.sun.star.document.Settings
     */
    BITMAP_TABLE_URL("BitmapTableURL", ConfigElementType.STRING ),

    /**
     * com.sun.star.document.Settings
     */
    AUTO_CALCULATE("AutoCalculate", ConfigElementType.BOOLEAN ),

    /**
     * com.sun.star.document.Settings
     * <p>
     * forbid use of printer metrics for layout
     */
    PRINTER_INDEPENDENT_LAYOUT("PrinterIndependentLayout", ConfigElementType.SHORT ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * layout engine should add value of a Font's "external leading" attribute to the line spacing
     */
    ADD_EXTERNAL_LEADING("AddExternalLeading", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * Whether to embed fonts used by the document (see e.g
     */
    EMBED_FONTS("EmbedFonts", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * Whether to embed also system fonts used by the document
     */
    EMBED_SYSTEM_FONTS("EmbedSystemFonts", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * Whether to embed only the fonts that are used in the document
     */
    EMBED_ONLY_USED_FONTS("EmbedOnlyUsedFonts", ConfigElementType.BOOLEAN ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * Whether to embed Latin script fonts
     */
    EMBED_LATIN_SCRIPT_FONTS("EmbedLatinScriptFonts", ConfigElementType.SHORT ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * Whether to embed Asian script fonts
     */
    EMBED_ASIAN_SCRIPT_FONTS("EmbedAsianScriptFonts", ConfigElementType.SHORT ),


    /**
     * com.sun.star.document.Settings
     * <p>
     * Whether to embed Complex script fonts
     */
    EMBED_COMPLEX_SCRIPT_FONTS("EmbedComplexScriptFonts", ConfigElementType.SHORT ),

    /**
     * com.sun.star.sheet.DocumentSettings
     * <p>
     * enables the restriction of object movement and resizing of drawing objects to the rast
     */
    IS_SNAP_TO_RASTER("IsSnapToRaster", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.DocumentSettings
     * <p>
     * enables the display of the drawing object rast
     */
    RASTER_IS_VISIBLE("RasterIsVisible", ConfigElementType.BOOLEAN),


    /**
     * com.sun.star.sheet.DocumentSettings
     * <p>
     * specifies the distance between horizontal grid elements in 1/100 mm
     */
    RASTER_RESOLUTION_X("RasterResolutionX", ConfigElementType.LONG),


    /**
     * com.sun.star.sheet.DocumentSettings
     * <p>
     * specifies the distance between vertical grid elements in 1/100 mm
     */
    RASTER_RESOLUTION_Y("RasterResolutionY", ConfigElementType.LONG),


    /**
     * com.sun.star.sheet.DocumentSettings
     * <p>
     * specifies the number of subdivisions between two horizontal grid elements
     */
    RASTER_SUBDIVISION_X("RasterSubdivisionX", ConfigElementType.LONG),


    /**
     * com.sun.star.sheet.DocumentSettings
     * <p>
     * specifies the number of subdivisions between two vertical grid elements
     */
    RASTER_SUBDIVISION_Y("RasterSubdivisionY", ConfigElementType.LONG),


    /**
     * com.sun.star.sheet.DocumentSettings
     * <p>
     * enables the synchronization of horizontal and vertical grid settings in the user interfac
     */
    IS_RASTER_AXIS_SYNCHRONIZED("IsRasterAxisSynchronized", ConfigElementType.BOOLEAN),


    /**
     * Undocumented, in case of multiple views
     */
    VIEW_ID("ViewId", ConfigElementType.STRING),

    /**
     * Undocumented
     * Set the active table.
     */
    ACTIVE_TABLE("ActiveTable", ConfigElementType.STRING),

    /**
     * Undocumented, deprecated
     */
    HORIZONTAL_SCROLLBAR_WIDTH("HorizontalScrollbarWidth", ConfigElementType.INT),

    /**
     * Undocumented, zoom for View > PageBreak
     */
    PAGE_VIEW_ZOOM_VALUE("PageViewZoomValue", ConfigElementType.INT),

    /**
     * Undocumented, equivalent to View > PageBreak
     */
    SHOW_PAGE_BREAK_PREVIEW("ShowPageBreakPreview", ConfigElementType.BOOLEAN),

    /**
     * Undocumented, show the cancel button
     */
    ALLOW_PRINT_JOB_CANCEL("AllowPrintJobCancel", ConfigElementType.BOOLEAN),

    /**
     * Undocumented, set the document readonly
     */
    LOAD_READONLY("LoadReadonly", ConfigElementType.BOOLEAN),


    /**
     * Undocumented, seems to be useful when the document is embedded
     */
    VISIBLE_AREA_TOP("VisibleAreaTop", ConfigElementType.INT),


    /**
     * Undocumented, seems to be useful when the document is embedded
     */
    VISIBLE_AREA_LEFT("VisibleAreaLeft", ConfigElementType.INT),


    /**
     * Undocumented, seems to be useful when the document is embedded
     */
    VISIBLE_AREA_WIDTH("VisibleAreaWidth", ConfigElementType.INT),


    /**
     * Undocumented, seems to be useful when the document is embedded
     */
    VISIBLE_AREA_HEIGHT("VisibleAreaHeight", ConfigElementType.INT),

    /**
     * Undocumented
     */
    HORIZONTAL_SPLIT_MODE("HorizontalSplitMode", ConfigElementType.SHORT),

    /**
     * Undocumented
     */
    VERTICAL_SPLIT_MODE("VerticalSplitMode", ConfigElementType.SHORT),

    /**
     * Undocumented
     */
    HORIZONTAL_SPLIT_POSITION("HorizontalSplitPosition", ConfigElementType.INT),

    /**
     * Undocumented
     */
    VERTICAL_SPLIT_POSITION("VerticalSplitPosition", ConfigElementType.INT),

    /**
     * Undocumented
     */
    CURSOR_POSITION_X("CursorPositionX", ConfigElementType.INT),

    /**
     * Undocumented
     */
    CURSOR_POSITION_Y("CursorPositionY", ConfigElementType.INT),

    /**
     * Undocumented
     */
    ACTIVE_SPLIT_RANGE("ActiveSplitRange", ConfigElementType.SHORT),

    /**
     * Undocumented
     */
    POSITION_LEFT("PositionLeft", ConfigElementType.INT),

    /**
     * Undocumented
     */
    POSITION_RIGHT("PositionRight", ConfigElementType.INT),

    /**
     * Undocumented
     */
    POSITION_TOP("PositionTop", ConfigElementType.INT),

    /**
     * Undocumented
     */
    POSITION_BOTTOM("PositionBottom", ConfigElementType.INT),

    ;

    private final String name;
    private final String type;

    ConfigElement(final String name, final ConfigElementType type) {
        this.name = name;
        this.type = type.toString();
    }

    /**
     * @return the name of the item
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the type of the item
     */
    public String getType() {
        return this.type;
    }
}
