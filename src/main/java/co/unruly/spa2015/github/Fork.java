package co.unruly.spa2015.github;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Fork {
    private String user;
    private final String name;
    private final Client client;

    public Fork(String user, String name, Client client) {
        this.user = user;
        this.name = name;
        this.client = client;
    }

    public List<Fork> children() throws IOException {
        RepositoryId repoId = new RepositoryId(user, name);

        return client.getForks(repoId).stream().map(this::repoToFork).collect(Collectors.toList());
    }

    private Fork repoToFork(Repository repository) {
        return new Fork(repository.getOwner().getLogin(), repository.getName(), client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Fork other = (Fork) obj;
        return Objects.equals(this.user, other.user)
                && Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "Fork{" +
                "name='" + name + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
