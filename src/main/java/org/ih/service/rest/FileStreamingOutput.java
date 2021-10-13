package org.ih.service.rest;

import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * @author Hector Plahar
 */
public class FileStreamingOutput implements StreamingOutput {

    private final File file;

    public FileStreamingOutput(File file) {
        this.file = file;
    }

    @Override
    public void write(OutputStream output) throws IOException {
        Files.copy(file.toPath(), output);
    }
}
