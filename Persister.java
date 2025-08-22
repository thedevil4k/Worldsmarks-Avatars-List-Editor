import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The format is specific and requires precise byte-level operations.
 */
public class Persister {

    // Represents a single entry in the bookmark list.
    public static class BookmarkEntry {
        public String name;
        public String value;
        public BookmarkEntry(String name, String value) { this.name = name; this.value = value; }
    }

    // Represents the entire content of a bookmark file.
    public static class BookmarkData {
        public String type;
        public int version;
        public List<BookmarkEntry> entries;
        public BookmarkData(String type, int version, List<BookmarkEntry> entries) {
            this.type = type;
            this.version = version;
            this.entries = entries;
        }
    }

    // Holds metadata from a reference file for writing.
    private static class ReferenceMetadata {
        int version, entryCount, extraInt, objectID;
        String typeText;
    }

    /**
     * Loads and decodes a .worldsmarks or .avatars file.
     * @param sourceFile The binary file to read.
     * @return A BookmarkData object containing the file's content.
     * @throws IOException If the file format is invalid or a read error occurs.
     */
    public static BookmarkData loadFromFile(File sourceFile) throws IOException {
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(sourceFile))) {
            String magicHeader = readString(inputStream);
            if (!"PERSISTER Worlds, Inc.".equals(magicHeader)) {
                throw new IOException("Invalid format: header=" + magicHeader);
            }
            int fileVersion = inputStream.readInt();
            int entryCount = inputStream.readInt();
            inputStream.readInt(); // Skip 'extra'
            inputStream.readInt(); // Skip 'oID'
            String typeIdentifier = readString(inputStream);

            List<BookmarkEntry> entryList = new ArrayList<>();
            int i = 0;
            while (i < entryCount) {
                if (i == 0) {
                    inputStream.readInt();
                } else {
                    inputStream.readInt();
                    inputStream.readInt();
                }
                String name = readString(inputStream);
                String value = readString(inputStream);
                entryList.add(new BookmarkEntry(name, value));
                i++;
            }
            readString(inputStream); // Read "END PERSISTER"

            return new BookmarkData(typeIdentifier, fileVersion, entryList);
        }
    }

    /**
     * Encodes and writes data to a binary .worldsmarks or .avatars file.
     * @param destinationFile The file to be created/overwritten.
     * @param content The data to write.
     * @param referenceFile An optional original file to preserve metadata from.
     * @throws IOException If a write error occurs.
     */
    public static void saveToFile(File destinationFile, BookmarkData content, File referenceFile) throws IOException {
        ReferenceMetadata metadata = getMetadataToWrite(content, referenceFile);

        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(destinationFile))) {
            writeHeader(outputStream, metadata, content.entries.size());
            writeAllEntries(outputStream, content.entries, metadata.objectID);
            writeFooter(outputStream);
        }
    }

    // --- Private Helper Methods for Writing ---

    private static void writeHeader(DataOutputStream dos, ReferenceMetadata meta, int entryCount) throws IOException {
        writeString(dos, "PERSISTER Worlds, Inc.");
        dos.writeInt(meta.version);
        dos.writeInt(entryCount);
        dos.writeInt(meta.extraInt);
        dos.writeInt(meta.objectID);
        writeString(dos, meta.typeText == null ? "" : meta.typeText);
    }

    private static void writeAllEntries(DataOutputStream dos, List<BookmarkEntry> entries, int objectID) throws IOException {
        int i = 0;
        while (i < entries.size()) {
            if (i == 0) {
                dos.writeInt(1);
            } else {
                dos.writeInt(460 + i);
                dos.writeInt(objectID);
            }
            BookmarkEntry currentEntry = entries.get(i);
            writeString(dos, currentEntry.name);
            writeString(dos, currentEntry.value);
            i++;
        }
    }
    
    private static void writeFooter(DataOutputStream dos) throws IOException {
        writeString(dos, "END PERSISTER");
    }

    private static ReferenceMetadata getMetadataToWrite(BookmarkData data, File referenceFile) throws IOException {
        ReferenceMetadata meta = new ReferenceMetadata();
        meta.version = data.version;
        meta.typeText = data.type;
        meta.extraInt = 459; // Default
        meta.objectID = 8782; // Default

        if (referenceFile != null && referenceFile.exists()) {
            ReferenceMetadata refMeta = readReferenceMetadata(referenceFile);
            meta.version = refMeta.version;
            meta.extraInt = refMeta.extraInt;
            meta.objectID = refMeta.objectID;
            if (meta.typeText == null || meta.typeText.isEmpty()) {
                meta.typeText = refMeta.typeText;
            }
        }
        return meta;
    }

    // --- Low-level Binary I/O ---
    
    private static String readString(DataInputStream dis) throws IOException {
        boolean isEmpty = dis.readBoolean();
        return isEmpty ? "" : dis.readUTF();
    }

    private static void writeString(DataOutputStream dos, String s) throws IOException {
        if (s == null) s = "";
        if (!s.isEmpty()) {
            dos.writeBoolean(false);
            dos.writeUTF(s);
        } else {
            dos.writeBoolean(true);
        }
    }
    
    private static ReferenceMetadata readReferenceMetadata(File refFile) throws IOException {
        ReferenceMetadata meta = new ReferenceMetadata();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(refFile))) {
            readString(dis); // Header
            meta.version = dis.readInt();
            meta.entryCount = dis.readInt();
            meta.extraInt = dis.readInt();
            meta.objectID = dis.readInt();
            meta.typeText = readString(dis);
        } catch (IOException e) { /* Fails silently if no reference */ }
        return meta;
    }
}