package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.DrawFrame;
import com.github.jferard.fastods.DrawImage;
import com.github.jferard.fastods.DrawObject;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.ref.RangeRef;
import com.github.jferard.fastods.style.DrawFillBitmap;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.util.FileUtil;
import com.github.jferard.fastods.util.SVGRectangle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * A helper class for inserting objects into a document
 */
public class InsertHelper {
    /**
     * @return a new InsertHelper
     */
    public static InsertHelper create() {
        return new InsertHelper(FileUtil.create());
    }

    private final FileUtil fileUtil;

    /**
     * @param fileUtil a file util
     */
    public InsertHelper(final FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    /**
     * Insert a new image into the document
     *
     * @param document  the destination document
     * @param table     the destination table
     * @param source    the source of the image
     * @param destName  the name of the image embedded in the document
     * @param rectangle the frame coordinates
     * @throws IOException if an I/O error occurs
     */
    public void insertImage(final OdsDocument document, final Table table, final String frameName,
                            final File source, final String destName, final SVGRectangle rectangle)
            throws IOException {

        final String sourceName = source.getName();
        final String mediaType = this.getMediaType(sourceName);
        document.addExtraFile("Pictures/" + destName, mediaType, this.fileUtil.readFile(source));
        table.addShape(
                DrawFrame.builder(frameName, new DrawImage("Pictures/" + destName), rectangle)
                        .build());
    }

    /**
     * Insert a new image into the document
     *
     * @param document     the destination document
     * @param table        the destination table
     * @param sourceStream the source of the image
     * @param destName     the name of the image embedded in the document
     * @param rectangle    the frame coordinates
     * @throws IOException if an I/O error occurs
     */
    public void insertImage(final OdsDocument document, final Table table, final String frameName,
                            final InputStream sourceStream, final String destName,
                            final SVGRectangle rectangle) throws IOException {

        final String mediaType = this.getMediaType(destName);
        document.addExtraFile("Pictures/" + destName, mediaType,
                this.fileUtil.readStream(sourceStream));
        table.addShape(
                DrawFrame.builder(frameName, new DrawImage("Pictures/" + destName), rectangle)
                        .build());
    }

    private String getMediaType(final String sourceName) {
        final String extension =
                sourceName.substring(sourceName.lastIndexOf(".") + 1).toLowerCase(Locale.US);
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            return "image/jpeg";
        }

        return "image/" + extension;
    }

    public DrawFillBitmap createDrawFillImage(final OdsDocument document,
                                              final InputStream sourceStream, final String name,
                                              final String href) throws IOException {
        final byte[] bytes = FileUtil.create().readStream(sourceStream);
        document.addExtraFile(href, this.getMediaType(href), bytes);
        return new DrawFillBitmap(name, href);
    }

    /**
     * Insert a new object into the document
     *
     * @param document        the destination document
     * @param table           the destination table
     * @param frameName       the name of the frame
     * @param objectName      the name of the object embedded in the document
     * @param objectMediaType the media-type of the object embedded in the document
     * @param objectVersion   the version of the object embedded in the document
     * @param sourceStream    the source of the image
     * @param rectangle       the frame coordinates
     * @param gs              the graphic style
     * @throws IOException if an I/O error occurs
     */
    public void insertObject(final OdsDocument document, final Table table, final String frameName,
                             final String objectName, final String objectMediaType,
                             final String objectVersion, final InputStream sourceStream,
                             final SVGRectangle rectangle, final GraphicStyle gs)
            throws IOException {
        document.addExtraObject(objectName, objectMediaType, objectVersion);
        final Map<String, OdsArchiveExplorer.OdsFile> fileByName =
                new OdsArchiveExplorer(this.fileUtil, sourceStream).explore();
        for (final Map.Entry<String, OdsArchiveExplorer.OdsFile> entry : fileByName.entrySet()) {
            final String name = entry.getKey();
            if (name.equals("META-INF/manifest.xml") || name.equals("mimetype") ||
                    name.startsWith("Thumbnails")) {
                continue; // do nothing
            }
            entry.getValue().addToDocument(document, objectName + "/");
        }
        table.addShape(DrawFrame.builder(frameName,
                new DrawObject("./" + objectName, Collections.<RangeRef>emptyList()), rectangle)
                .style(gs).build());
    }
}