package co.unruly.spa2015.github;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

public class ForkTest {

    @Test
    public void shouldReturnAnEmptyListOfForksWhenThereAreNoForks() throws Exception {
        Client stubClient = clientThatReturns(Collections.emptyList());

        assertThat(new Fork("junit-team", "junit", stubClient).children(), emptyIterable());
    }

    @Test
    public void shouldReturnASingleForkWhenARepositoryHasOneFork() throws Exception {
        String expectedLogin = "some-user";
        String expectedName = "some-repo";
        Client stubClient = clientThatReturns(asList(makeRepository(expectedLogin, expectedName)));

        assertThat(new Fork("junit-team", "junit", stubClient).children(), contains(new Fork(expectedLogin, expectedName, stubClient)));
    }

    private Repository makeRepository(String login, String name) {
        return new Repository()
                    .setForks(0)
                    .setName(name)
                    .setOwner(new User().setLogin(login));
    }

    private Client clientThatReturns(List<Repository> result) {
        return new Client() {
            @Override
            public List<Repository> getForks(IRepositoryIdProvider repo) throws IOException {
                return result;
            }
        };
    }
}
