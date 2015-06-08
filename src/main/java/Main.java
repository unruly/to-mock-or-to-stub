import co.unruly.spa2015.github.Client;
import co.unruly.spa2015.github.Fork;
import co.unruly.spa2015.report.HandlesBadForksReportWriter;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException {

        String username = args[0];
        String password = args[1];

        Client client = new Client(username, password);

        Fork exampleRepo = new Fork("guardian", "sbt-jasmine-plugin", client);

        new HandlesBadForksReportWriter(System.out).generateForkReport(exampleRepo, 2);
    }
}
