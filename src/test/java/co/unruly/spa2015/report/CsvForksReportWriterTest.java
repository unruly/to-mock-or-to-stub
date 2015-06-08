package co.unruly.spa2015.report;

import co.unruly.spa2015.github.Fork;
import co.unruly.spa2015.github.InvalidForkException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CsvForksReportWriterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ByteArrayOutputStream outputStream;

    @Mock
    private Fork mockFork1, mockFork2, mockFork3;

    @InjectMocks
    private CsvForksReportWriter csvForksReportWriter;

    @Before
    public void setup() throws Exception {
        when(mockFork1.getName()).thenReturn("name");
        when(mockFork1.getUser()).thenReturn("user");

        when(mockFork2.getName()).thenReturn("child");
        when(mockFork2.getUser()).thenReturn("first");

        when(mockFork3.getName()).thenReturn("child");
        when(mockFork3.getUser()).thenReturn("second");
    }

    @Test
    public void shouldPrintCSVListOfChildRepos_parentOnly() throws Exception {
        when(mockFork1.children(anyInt())).thenReturn(Collections.emptyList());

        csvForksReportWriter.generateForkReport(mockFork1,1);

        verify(outputStream).write(matchBytes("user/name"));
    }

    @Test
    public void shouldPrintTheChildrenOut() throws Exception {
        when(mockFork1.children(anyInt())).thenReturn(Arrays.asList(mockFork2, mockFork3));
        csvForksReportWriter.generateForkReport(mockFork1,2);

        verify(outputStream).write(matchBytes("user/name,first/child,second/child"));
    }

    @Test
    public void shouldAllowTheForksToManageRecursion() throws Exception {
        csvForksReportWriter.generateForkReport(mockFork1,10);

        verify(mockFork1).children(10);
    }

    @Test
    public void shouldDieIfThereAreAnyProblemsWithTheForks() throws Exception {
        thrown.expect(InvalidForkException.class);

        when(mockFork1.children(anyInt())).thenThrow(InvalidForkException.class);

        csvForksReportWriter.generateForkReport(mockFork1, 1);
    }

    public static byte[] matchBytes(String text) {
        return argThat(new TypeSafeMatcher<byte[]>(){

            @Override
            public void describeTo(Description description) {
                description.appendText("a byte array containing the equivalent to")
                        .appendValue(text);
            }

            @Override
            protected boolean matchesSafely(byte[] item) {
                return Arrays.equals(text.getBytes(), item);
            }

        });
    }

}

