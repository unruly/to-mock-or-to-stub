package co.unruly.spa2015.report;

import co.unruly.spa2015.github.Fork;
import co.unruly.spa2015.github.InvalidForkException;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ForksReportWriter {
    protected OutputStream outputStream;

    public ForksReportWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public abstract void generateForkReport(Fork parent, int maxDepth) throws InvalidForkException;

    protected void printMessage(String message) {
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
