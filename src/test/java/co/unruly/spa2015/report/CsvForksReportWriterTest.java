package co.unruly.spa2015.report;

import co.unruly.spa2015.github.Fork;
import co.unruly.spa2015.github.InvalidForkException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CsvForksReportWriterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final CsvForksReportWriter csvForksReportWriter = new CsvForksReportWriter(outputStream);

    @Test
    public void shouldPrintCSVListOfChildRepos_parentOnly() throws Exception {
        csvForksReportWriter.generateForkReport(new Stubs.StubFork("user","name"),1);

        assertThat(HandlesBadForksReportWriterTest.streamToString(outputStream), is("user/name"));
    }

    @Test
    public void shouldPrintTheChildrenOut() throws Exception {
        Stubs.StubFork fork = new Stubs.StubFork("user", "name") {
            @Override
            public List<Fork> children(int maxDepth) throws InvalidForkException {
                return Arrays.asList(new Stubs.StubFork("first", "child"), new Stubs.StubFork("second", "child"));
            }
        };
        csvForksReportWriter.generateForkReport(fork,1);

        assertThat(HandlesBadForksReportWriterTest.streamToString(outputStream), is("user/name,first/child,second/child"));
    }

    @Test
    public void shouldAllowTheForksToManageRecursion() throws Exception {
        Stubs.StubFork fork = new Stubs.StubFork("user", "name") {
            @Override
            public List<Fork> children(int maxDepth) throws InvalidForkException {
                assertThat(maxDepth, is(10));
                return Arrays.asList(new Stubs.StubFork("first", "child"), new Stubs.StubFork("second", "child"));
            }
        };
        csvForksReportWriter.generateForkReport(fork,10);
    }

    @Test
    public void shouldDieIfThereAreAnyProblemsWithTheForks() throws Exception {
        thrown.expect(InvalidForkException.class);

        csvForksReportWriter.generateForkReport(new Stubs.BrokenFork("bad", "fork"), 1);
    }

}
