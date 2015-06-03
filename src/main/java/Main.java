import co.unruly.spa2015.github.Client;
import co.unruly.spa2015.github.Fork;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException {
        Client client = new Client();

        Fork guardian = new Fork("guardian", "sbt-jasmine-plugin", client);

        guardian.children().forEach(System.out::println);
    }
}
