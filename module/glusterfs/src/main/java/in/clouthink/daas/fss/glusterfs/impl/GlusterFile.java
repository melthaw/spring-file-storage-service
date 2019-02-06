package in.clouthink.daas.fss.glusterfs.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class GlusterFile {

    private Path filePath;

    public GlusterFile(Path filePath) {
        this.filePath = filePath;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        Files.copy(filePath, outputStream);
    }
}
