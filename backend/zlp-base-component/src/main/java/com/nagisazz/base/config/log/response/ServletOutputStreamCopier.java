package com.nagisazz.base.config.log.response;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServletOutputStreamCopier extends ServletOutputStream {

    private static final int INIT_BUFFER_SIZE = 1024;

    private ByteArrayOutputStream copy = new ByteArrayOutputStream(INIT_BUFFER_SIZE);

    public void write(int b) {
        this.copy.write(b);
    }

    public byte[] getCopy() {
        return this.copy.toByteArray();
    }

    public void flush() throws IOException {
        this.copy.flush();
    }

    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener arg0) {
    }
}
