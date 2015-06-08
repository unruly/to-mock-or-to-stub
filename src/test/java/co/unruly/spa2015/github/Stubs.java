package co.unruly.spa2015.github;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class Stubs {
    static class StubClient extends Client {
        public final Map<String, List<Repository>> repoMap = new HashMap<>();

        @Override
        public List<Repository> getForks(IRepositoryIdProvider repo) throws IOException {
            return repoMap.getOrDefault(repo.generateId(), emptyList());
        }
    }

    static class BrokenClient extends Client {
        @Override
        public List<Repository> getForks(IRepositoryIdProvider repo) throws IOException {
            throw new IOException("Uh oh ...");
        }
    }
}
