package co.unruly.spa2015.report;


import co.unruly.spa2015.github.Fork;
import co.unruly.spa2015.github.InvalidForkException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import static co.unruly.spa2015.report.CsvForksReportWriterTest.matchBytes;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HandlesBadForksReportWriterTest {

    @Mock
    private ByteArrayOutputStream outputStream;

    @Mock
    private Fork mockFork1, mockFork2, mockFork3;

    @InjectMocks
    private HandlesBadForksReportWriter reportWriter;

    @Before
    public void setup() throws Exception {
        when(mockFork1.getUser()).thenReturn("user");
        when(mockFork1.getName()).thenReturn("name");

        when(mockFork2.getUser()).thenReturn("first");
        when(mockFork2.getName()).thenReturn("child");

        when(mockFork3.getUser()).thenReturn("second");
        when(mockFork3.getName()).thenReturn("child");
    }


    @Test
    public void shouldPrintOutTheNameOfAForkWithNoChildren() throws Exception {
        reportWriter.generateForkReport(mockFork1, 1);

        verify(outputStream).write(matchBytes("user/name\n"));
        verifyNoMoreInteractions(outputStream);
    }

    @Test
    public void shouldPrintOutParentRepoPlusChildRepoNames() throws Exception {
        when(mockFork1.children(anyInt())).thenReturn(Arrays.asList(mockFork2));

        reportWriter.generateForkReport(mockFork1, 2);

        InOrder inOrder = inOrder(outputStream);
        inOrder.verify(outputStream).write(matchBytes("user/name\n"));
        inOrder.verify(outputStream).write(matchBytes("first/child\n"));
        verifyNoMoreInteractions(outputStream);
    }

    @Test
    public void shouldObeyTheMaxDepthOfTheReport() throws Exception {
        when(mockFork1.children(anyInt())).thenReturn(Arrays.asList(mockFork2));
        when(mockFork2.children(anyInt())).thenReturn(Arrays.asList(mockFork3));

        reportWriter.generateForkReport(mockFork1, 2);

        verify(outputStream, never()).write(matchBytes("second/child\n"));
    }

    @Test
    public void shouldReportInvalidParentRepository() throws Exception {
        InvalidForkException invalidForkException = new InvalidForkException(mockFork1, null);

        when(mockFork1.children(anyInt())).thenThrow(invalidForkException);

        reportWriter.generateForkReport(mockFork1, 3);

        verify(outputStream).write(matchBytes("*** INVALID: user/name\n"));
    }

    @Test
    public void shouldReportOnValidRepositoryWhenOneFails() throws Exception {
        InvalidForkException invalidForkException = new InvalidForkException(mockFork2, null);

        when(mockFork1.children(anyInt())).thenReturn(Arrays.asList(mockFork2, mockFork3));
        when(mockFork2.children(anyInt())).thenThrow(invalidForkException);

        reportWriter.generateForkReport(mockFork1, 3);

        InOrder inOrder = inOrder(outputStream);
        inOrder.verify(outputStream).write(matchBytes("user/name\n"));
        inOrder.verify(outputStream).write(matchBytes("*** INVALID: first/child\n"));
        inOrder.verify(outputStream).write(matchBytes("second/child\n"));
        verifyNoMoreInteractions(outputStream);

    }
}