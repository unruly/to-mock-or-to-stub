package co.unruly.spa2015.github;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ForkTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private Client mockClient;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Repository middleRepository;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Repository childRepository;


    private final String projectName = "junit";
    private final String parentUser = "junit-team";
    private final String middleUser = "middle-user";
    private final String childUser = "child-user";

    @Before
    public void setup() throws Exception {
        when(middleRepository.getOwner().getLogin()).thenReturn(middleUser);
        when(middleRepository.getName()).thenReturn(projectName);
        when(childRepository.getOwner().getLogin()).thenReturn(childUser);
        when(childRepository.getName()).thenReturn(projectName);
    }

    @Test
    public void shouldReturnAnEmptyListOfForksWhenThereAreNoForks() throws Exception {
        assertThat(new Fork("junit-team", "junit", mockClient).children(1), emptyIterable());
    }

    @Test
    public void shouldReturnASingleForkWhenARepositoryHasOneFork() throws Exception {
        when(mockClient.getForks(matchesRepository(parentUser))).thenReturn(asList(middleRepository));

        assertThat(new Fork(parentUser, projectName, mockClient).children(1), contains(new Fork(middleUser, projectName, mockClient)));
    }

    @Test
    public void shouldFetchAChildsForks() throws Exception {

        when(mockClient.getForks(matchesRepository(parentUser))).thenReturn(asList(middleRepository));
        when(mockClient.getForks(matchesRepository(middleUser))).thenReturn(asList(childRepository));

        assertThat(new Fork(parentUser, projectName, mockClient).children(2), hasItem(new Fork(childUser, projectName, mockClient)));
    }

    @Test
    public void shouldStopWhenItReachesMaxDepth() throws Exception {
        Repository mockRepository = mockRepository(parentUser, projectName);
        when(mockClient.getForks(matchesRepository(parentUser))).thenReturn(asList(mockRepository));

        assertThat(new Fork(parentUser, projectName, mockClient).children(1), contains(new Fork(parentUser, projectName, mockClient)));
    }

    @Test
    public void shouldThrowAnInvalidForkExceptionWhenThereIsAProblemWithTheRepo() throws Exception {
        when(mockClient.getForks(any(IRepositoryIdProvider.class))).thenThrow(new IOException("It's all gone wrong!"));
        Fork fork = new Fork(parentUser, projectName, mockClient);

        thrown.expect(new TypeSafeMatcher<InvalidForkException>() {
            @Override
            protected boolean matchesSafely(InvalidForkException item) {
                return fork.equals(item.badFork);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("an InvalidForkException with a bad fork equal to")
                        .appendValue(fork);
            }
        });

        fork.children(1);
    }

    private String getRepoId(String login) {
        return login + "/" + projectName;
    }

    private IRepositoryIdProvider matchesRepository(String login) {
        String repoId = getRepoId(login);
        return Mockito.argThat(new TypeSafeMatcher<IRepositoryIdProvider>() {
            @Override
            protected boolean matchesSafely(IRepositoryIdProvider item) {

                return repoId.equals(item.generateId());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a Repository with id ")
                        .appendValue(repoId);
            }
        });
    }

    private Repository mockRepository(String login, String name) {
        Repository mockRepo = mock(Repository.class, RETURNS_DEEP_STUBS);
        when(mockRepo.getOwner().getLogin()).thenReturn(login);
        when(mockRepo.getName()).thenReturn(name);
        return mockRepo;
    }
}
