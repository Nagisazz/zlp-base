package com.nagisazz.base.config.log.response;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class LogResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStreamCopier streamCopier;

    private PrintWriterCopier writerCopier;

    private boolean useWriter;

    private int statusCode;

    public LogResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        this.useWriter = true;
    }

    public String writeResponse(HttpServletResponse response) throws IOException {
        String rest = "";
        if (isUseWriter()) {
            rest = new String(getWriterCopy());
            response.setContentLength(-1);
            PrintWriter out = response.getWriter();
            out.write(getWriterCopy());
            out.flush();
            out.close();
        } else {
            // 只输出json返回类型的数据
            if ("application/json".equals(response.getContentType())) {
                rest = new String(getStreamCopy(), StandardCharsets.UTF_8);
            }
            response.setContentLength(-1);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            servletOutputStream.write(getStreamCopy());
            servletOutputStream.flush();
            servletOutputStream.close();
        }
        return rest;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (this.writerCopier != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (this.streamCopier == null) {
            this.useWriter = false;
            this.streamCopier = new ServletOutputStreamCopier();
        }

        return this.streamCopier;
    }


    public PrintWriter getWriter() throws IOException {
        if (this.streamCopier != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (this.writerCopier == null) {
            this.useWriter = true;
            this.writerCopier = new PrintWriterCopier();
        }

        return this.writerCopier.getWriter();
    }

    public byte[] getBytes() {
        if (this.streamCopier == null && this.writerCopier == null) {
            return new byte[0];
        }

        return this.useWriter ? this.writerCopier.getBytes() : this.streamCopier.getCopy();
    }

    public byte[] getStreamCopy() {
        if (this.useWriter) {
            throw new IllegalStateException("already use writer, please call getWriterCopy()");
        }

        return (this.streamCopier == null) ? new byte[0] : this.streamCopier.getCopy();
    }

    public char[] getWriterCopy() {
        if (!this.useWriter) {
            throw new IllegalStateException("already use outputStream, please call getStreamCopy()");
        }

        return (this.writerCopier == null) ? new char[0] : this.writerCopier.getCopy();
    }

    public void sendError(int sc, String msg) throws IOException {
        ((HttpServletResponse) getResponse()).sendError(sc, msg);
        this.statusCode = sc;
    }

    public void sendError(int sc) throws IOException {
        ((HttpServletResponse) getResponse()).sendError(sc);
        this.statusCode = sc;
    }

    public void setStatus(int sc, String sm) {
        super.setStatus(sc, sm);
        this.statusCode = sc;
    }

    public int getStatus() {
        return this.statusCode;
    }

    public void setStatus(int sc) {
        super.setStatus(sc);
        this.statusCode = sc;
    }

    public boolean isUseWriter() {
        return this.useWriter;
    }
}
