package co.unruly.spa2015.github;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.List;

public class Client {

    public Client() {

    }

    public List<Repository> getForks(IRepositoryIdProvider repo) throws IOException {
        RepositoryService repositoryService = new RepositoryService();

        return repositoryService.getForks(repo);
    }
}
