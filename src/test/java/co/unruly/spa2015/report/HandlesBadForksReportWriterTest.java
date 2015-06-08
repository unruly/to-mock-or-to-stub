package co.unruly.spa2015.report;


import co.unruly.spa2015.github.Fork;
import co.unruly.spa2015.github.InvalidForkException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HandlesBadForksReportWriterTest {

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private HandlesBadForksReportWriter handlesBadForksReportWriter = new HandlesBadForksReportWriter(outputStream);

    @Test
    public void shouldPrintOutTheNameOfAForkWithNoChildren() throws Exception {
        Fork stubFork = new Stubs.StubFork("user","name");

        handlesBadForksReportWriter.generateForkReport(stubFork, 1);

        assertThat(streamToString(outputStream), is("user/name\n"));
    }

    @Test
    public void shouldPrintOutParentRepoPlusChildRepoNames() throws Exception {
        Fork stubFork = new Stubs.StubFork("user","name") {
            @Override
            public List<Fork> children(int maxDepth) throws InvalidForkException {
                return Arrays.asList(new Stubs.StubFork("other-user","name"));
            }
        };

        handlesBadForksReportWriter.generateForkReport(stubFork, 2);

        assertThat(streamToString(outputStream), is("user/name\nother-user/name\n"));
    }

    @Test
    public void shouldObeyTheMaxDepthOfTheReport() throws Exception {
        Fork stubFork = new Stubs.StubFork("user","name") {
            @Override
            public List<Fork> children(int maxDepth) throws InvalidForkException {
                return Arrays.asList(new Stubs.StubFork("other-user", "name") {
                    @Override
                    public List<Fork> children(int maxDepth) throws InvalidForkException {
                        return Arrays.asList(new Stubs.StubFork("too", "deep"));
                    }
                });
            }
        };

        handlesBadForksReportWriter.generateForkReport(stubFork, 2);

        assertThat(streamToString(outputStream), is("user/name\nother-user/name\n"));
    }

    @Test
    public void shouldReportInvalidParentRepository() throws Exception {
        handlesBadForksReportWriter.generateForkReport(new Stubs.BrokenFork("bad", "data"), 3);

        assertThat(streamToString(outputStream), is("*** INVALID: bad/data\n"));
    }

    @Test
    public void shouldReportOnValidRepositoryWhenOneFails() throws Exception {
        Fork stubFork = new Stubs.StubFork("user","name") {
            @Override
            public List<Fork> children(int maxDepth) throws InvalidForkException {
                return Arrays.asList(new Stubs.BrokenFork("bad", "data"), new Stubs.StubFork("other-user","name"));
            }
        };
        handlesBadForksReportWriter.generateForkReport(stubFork, 3);

        assertThat(streamToString(outputStream), is("user/name\n*** INVALID: bad/data\nother-user/name\n"));
    }

    static String streamToString(ByteArrayOutputStream outputStream) {
        return new String(outputStream.toByteArray());
    }

}