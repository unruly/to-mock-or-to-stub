package co.unruly.spa2015.github;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Client {

    private final RepositoryService repositoryService;

    public Client() {
        repositoryService = new RepositoryService();
    }

    public Client(String username, String password) {
        repositoryService = new RepositoryService(new GitHubClient().setCredentials(username, password));
    }

    public List<Repository> getForks(IRepositoryIdProvider repo) throws IOException {
        return repositoryService.getForks(repo);
    }

    public Optional<Repository> getParentFork(IRepositoryIdProvider repo) throws IOException {
        return Optional.ofNullable(repositoryService.getRepository(repo)).map(Repository::getParent);
    }
}
