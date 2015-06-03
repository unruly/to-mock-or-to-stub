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

        assertThat(new Fork("junit-team", "junit", stubClient).forks(), emptyIterable());
    }

    @Test
    public void shouldReturnASingleForkWhenARepositoryHasOneFork() throws Exception {
        Repository expectedRepository = new Repository()
                .setForks(0)
                .setName("some-repo")
                .setOwner(new User().setLogin("some-user"));
        Client stubClient = clientThatReturns(asList(expectedRepository));

        assertThat(new Fork("junit-team", "junit", stubClient).forks(), contains(new Fork("some-user", "some-repo", stubClient)));
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
