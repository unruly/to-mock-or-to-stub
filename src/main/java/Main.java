import co.unruly.spa2015.github.Client;
import co.unruly.spa2015.github.Fork;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;

import java.io.IOException;
import java.util.Optional;

public class Main {

    public static void main(String... args) throws IOException {

        String username = args[0];
        String password = args[1];

        Client client = new Client(username, password);

        Fork guardian = new Fork("guardian", "sbt-jasmine-plugin", client);

        guardian.children(3).forEach(System.out::println);

        Fork aChild = guardian.children(1).get(0);
        Optional<Repository> parentFork = client.getParentFork(new RepositoryId(aChild.user, aChild.name));

        System.out.println(parentFork.map(Repository::getGitUrl));
    }
}
