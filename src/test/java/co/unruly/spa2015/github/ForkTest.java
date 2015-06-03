package co.unruly.spa2015.github;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ForkTest {

    @Mock
    private List<String> list;

    @Test
    public void shouldHaveAWorkingTestFramework() throws Exception {
        verify(list).add("foo");
    }
}
