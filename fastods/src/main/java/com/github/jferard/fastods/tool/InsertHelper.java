package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.DrawFrame;
import com.github.jferard.fastods.DrawImage;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.style.DrawFillImage;
import com.github.jferard.fastods.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

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
     * @param document the destination document
     * @param table    the destination table
     * @param source   the source of the image
     * @param destName the name of the image embedded in the document
     * @param x        the x coordinate
     * @param y        the y coordinate
     * @param width    the width
     * @param height   the height
     * @throws IOException if an I/O error occurs
     */
    public void insertImage(final OdsDocument document, final Table table, final File source,
                            final String destName, final Length x, final Length y,
                            final Length width, final Length height) throws IOException {

        final String sourceName = source.getName();
        final String mediaType = this.getMediaType(sourceName);
        document.addExtraFile("Pictures/" + destName, mediaType, this.fileUtil.readFile(source));
        table.addShape(DrawFrame.builder(new DrawImage("Pictures/" + destName), x, y, width, height)
                .build());
    }

    /**
     * Insert a new image into the document
     *
     * @param document     the destination document
     * @param table        the destination table
     * @param sourceStream the source of the image
     * @param destName     the name of the image embedded in the document
     * @param x            the x coordinate
     * @param y            the y coordinate
     * @param width        the width
     * @param height       the height
     * @throws IOException if an I/O error occurs
     */
    public void insertImage(final OdsDocument document, final Table table,
                            final InputStream sourceStream, final String destName, final Length x,
                            final Length y, final Length width, final Length height)
            throws IOException {

        final String mediaType = this.getMediaType(destName);
        document.addExtraFile("Pictures/" + destName, mediaType,
                this.fileUtil.readStream(sourceStream));
        table.addShape(DrawFrame.builder(new DrawImage("Pictures/" + destName), x, y, width, height)
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

    public DrawFillImage createDrawFillImage(final OdsDocument document,
                                             final InputStream sourceStream, final String name,
                                             final String href) throws IOException {
        final byte[] bytes = FileUtil.create().readStream(sourceStream);
        document.addExtraFile(href, this.getMediaType(href), bytes);
        final DrawFillImage drawFillImage = new DrawFillImage(name, href);
        return drawFillImage;
    }
}
