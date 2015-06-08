package co.unruly.spa2015.github;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ForkTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final Stubs.StubClient stubClient = new Stubs.StubClient();
    private final String projectName = "junit";
    private final String parentLogin = "junit-team";
    private final String expectedLogin = "some-user";

    @Test
    public void shouldReturnAnEmptyListOfForksWhenThereAreNoForks() throws Exception {
        assertThat(new Fork("junit-team", "junit", stubClient).children(1), emptyIterable());
    }

    @Test
    public void shouldReturnASingleForkWhenARepositoryHasOneFork() throws Exception {
        stubClient.repoMap.put(getRepoId(parentLogin), asList(makeRepository(expectedLogin, projectName)));

        assertThat(new Fork(parentLogin, projectName, stubClient).children(1), contains(new Fork(expectedLogin, projectName, stubClient)));
    }

    @Test
    public void shouldFetchAChildsForks() throws Exception {
        String childLogin = "intermediate-user";
        stubClient.repoMap.put(getRepoId(parentLogin), asList(makeRepository(childLogin, projectName)));
        stubClient.repoMap.put(getRepoId(childLogin), asList(makeRepository(expectedLogin, projectName)));

        assertThat(new Fork(parentLogin, projectName, stubClient).children(2), hasItem(new Fork(expectedLogin, projectName, stubClient)));
    }

    @Test
    public void shouldStopWhenItReachesMaxDepth() throws Exception {
        stubClient.repoMap.put(getRepoId(parentLogin), asList(makeRepository(parentLogin, projectName)));


        assertThat(new Fork(parentLogin, projectName, stubClient).children(1), contains(new Fork(parentLogin, projectName, stubClient)));
    }

    @Test
    public void shouldThrowAnInvalidForkExceptionWhenThereIsAProblemWithTheRepo() throws Exception {
        Client brokenClient = new Stubs.BrokenClient();
        Fork fork = new Fork(parentLogin, projectName, brokenClient);

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

    private Repository makeRepository(String login, String name) {
        return new Repository()
                    .setName(name)
                    .setOwner(new User().setLogin(login));
    }

}
