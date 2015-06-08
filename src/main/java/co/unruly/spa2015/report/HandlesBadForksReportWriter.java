package co.unruly.spa2015.report;

import co.unruly.spa2015.github.Fork;
import co.unruly.spa2015.github.InvalidForkException;

import java.io.OutputStream;
import java.util.List;

public class HandlesBadForksReportWriter extends ForksReportWriter {

    public HandlesBadForksReportWriter(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void generateForkReport(Fork parent, int maxDepth) {
        if (maxDepth <= 0) return;

        try {
            List<Fork> children = parent.children(1);
            printFork(parent);
            children.forEach(fork -> generateForkReport(fork, maxDepth-1));
        } catch (InvalidForkException e) {
            printMessage("*** INVALID: " + e.badFork.getUser() + "/" + e.badFork.getName() + "\n");
        }
    }

    private void printFork(Fork fork) {
        printMessage(fork.getUser() + "/" + fork.getName() + "\n");
    }
}
