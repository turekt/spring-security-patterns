package hr.foi.thesis.security.securelogger;

import java.io.*;
import java.time.LocalDateTime;

public class LogOutputStream extends FileOutputStream {

    private static final long MAX_SIZE = 6144;
    private static final String LOG_NAME_BASE = "logs/patterns-";
    private static final String LOG_EXTENSION = ".log";
    private static final String LOG_NAME = LOG_NAME_BASE + LocalDateTime.now() + LOG_EXTENSION;

    private long currentSize;

    public LogOutputStream() throws FileNotFoundException {
        super(LOG_NAME);
        this.currentSize = 0;
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        checkSize(1);
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        checkSize(b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        checkSize(len);
    }

    private void checkSize(int len) throws LogSizeException {
        currentSize += len;

        if(currentSize > MAX_SIZE) {
            throw new LogSizeException("Log maximum size is reached!");
        }
    }
}
