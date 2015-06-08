package co.unruly.spa2015.report;

import co.unruly.spa2015.github.Fork;
import co.unruly.spa2015.github.InvalidForkException;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvForksReportWriter extends ForksReportWriter {

    public CsvForksReportWriter(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void generateForkReport(Fork parent, int maxDepth) throws InvalidForkException {
        List<Fork> repos = new ArrayList<>();
        repos.add(parent);
        repos.addAll(parent.children(maxDepth));
        printMessage(repos.stream().map(this::toRepoId).collect(Collectors.joining(",")));
    }

    private String toRepoId(Fork fork) {
        return fork.user + "/" + fork.name;
    }
}
