package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.util.FileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class OdsArchiveExplorer {
    static class OdsFile {
        private final String name;
        private byte[] bytes;
        private String mediaType;

        OdsFile(final String name) {
            this.name = name;
        }

        public void setBytes(final byte[] bytes) {
            this.bytes = bytes;
        }

        public void setMediaType(final String mediaType) {
            this.mediaType = mediaType;
        }

        public void addToDocument(final OdsDocument document, final String prefix) {
            if (this.bytes == null) {
                document.addExtraObject(prefix + this.name, this.mediaType, null);
            } else {
                document.addExtraFile(prefix + this.name, this.mediaType, this.bytes);
            }
        }
    }

    private final FileUtil fileUtil;
    private final InputStream sourceStream;
    private final Map<String, OdsFile> fileByName;

    /**
     * @param fileUtil     an util
     * @param sourceStream the source of the image
     * @throws IOException if an I/O error occurs
     */
    public OdsArchiveExplorer(final FileUtil fileUtil, final InputStream sourceStream) {
        this.fileUtil = fileUtil;
        this.sourceStream = sourceStream;
        this.fileByName = new HashMap<String, OdsFile>();
    }

    public Map<String, OdsFile> explore() throws IOException {
        final ZipInputStream zipStream = new ZipInputStream(this.sourceStream);
        ZipEntry zipEntry = zipStream.getNextEntry();
        while (zipEntry != null) {
            final String name = zipEntry.getName();
            final byte[] bytes = this.fileUtil.readStream(zipStream);
            if (name.equals("META-INF/manifest.xml")) {
                this.extractMediaTypeByName(bytes);
            }
            this.putBytes(name, bytes);
            zipEntry = zipStream.getNextEntry();
        }
        return this.fileByName;
    }

    private void putBytes(final String name, final byte[] bytes) {
        final OdsFile odsFile = this.getOrCreateOdsFile(name);
        odsFile.setBytes(bytes);
    }

    private void putMediaType(final String name, final String mediaType) {
        final OdsFile odsFile = this.getOrCreateOdsFile(name);
        odsFile.setMediaType(mediaType);
    }

    private OdsFile getOrCreateOdsFile(final String name) {
        OdsFile odsFile = this.fileByName.get(name);
        if (odsFile == null) {
            odsFile = new OdsFile(name);
            this.fileByName.put(name, odsFile);
        }
        return odsFile;
    }

    private void extractMediaTypeByName(final byte[] bytes) throws IOException {
        try {
            final Document manifest = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new ByteArrayInputStream(bytes));
            this.extractMediaTypeByName(manifest);
        } catch (final SAXException e) {
            // pass
        } catch (final ParserConfigurationException e) {
            // pass
        }
    }

    private void extractMediaTypeByName(final Document manifest) {
        final NodeList mEntries = manifest.getElementsByTagName("manifest:file-entry");
        for (int i = 0; i < mEntries.getLength(); i++) {
            final Node mEntry = mEntries.item(i);
            final NamedNodeMap attributes = mEntry.getAttributes();
            this.putMediaType(attributes.getNamedItem("manifest:full-path").getNodeValue(),
                    attributes.getNamedItem("manifest:media-type").getNodeValue());
        }
    }
}