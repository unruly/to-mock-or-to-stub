package co.unruly.spa2015.github;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class ForkTest {

    private Map<String, List<Repository>> repoMap = new HashMap<>();
    private final Client stubClient = stubClient();
    private final String projectName = "junit";
    private final String parentLogin = "junit-team";
    private final String expectedLogin = "some-user";

    @Test
    public void shouldReturnAnEmptyListOfForksWhenThereAreNoForks() throws Exception {
        assertThat(new Fork("junit-team", "junit", stubClient).children(1), emptyIterable());
    }

    @Test
    public void shouldReturnASingleForkWhenARepositoryHasOneFork() throws Exception {
        repoMap.put(getRepoId(parentLogin), asList(makeRepository(expectedLogin, projectName)));

        assertThat(new Fork(parentLogin, projectName, stubClient).children(1), contains(new Fork(expectedLogin, projectName, stubClient)));
    }

    @Test
    public void shouldFetchAChildsForks() throws Exception {
        String childLogin = "intermediate-user";
        repoMap.put(getRepoId(parentLogin), asList(makeRepository(childLogin, projectName)));
        repoMap.put(getRepoId(childLogin), asList(makeRepository(expectedLogin, projectName)));

        assertThat(new Fork(parentLogin, projectName, stubClient).children(2), hasItem(new Fork(expectedLogin, projectName, stubClient)));
    }

    @Test
    public void shouldStopWhenItReachesMaxDepth() throws Exception {
        repoMap.put(getRepoId(parentLogin), asList(makeRepository(parentLogin, projectName)));


        assertThat(new Fork(parentLogin, projectName, stubClient).children(1), contains(new Fork(parentLogin, projectName, stubClient)));
    }

    private String getRepoId(String login) {
        return login + "/" + projectName;
    }

    private Repository makeRepository(String login, String name) {
        return new Repository()
                    .setName(name)
                    .setOwner(new User().setLogin(login));
    }

    private Client stubClient() {
        return new Client() {
            @Override
            public List<Repository> getForks(IRepositoryIdProvider repo) throws IOException {
                return repoMap.getOrDefault(repo.generateId(), emptyList());
            }

        };
    }
}
