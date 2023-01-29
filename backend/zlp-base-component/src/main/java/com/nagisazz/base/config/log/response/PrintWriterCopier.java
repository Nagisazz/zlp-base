package com.nagisazz.base.config.log.response;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class PrintWriterCopier {

    private CharArrayWriter charArrayWriter = new CharArrayWriter();

    private PrintWriter writer = new PrintWriter(this.charArrayWriter);

    public PrintWriter getWriter() {
        return this.writer;
    }

    public char[] getCopy() {
        return this.charArrayWriter.toCharArray();
    }

    public byte[] getBytes() {
        return this.charArrayWriter.toString().getBytes(StandardCharsets.UTF_8);
    }
}
