package co.unruly.spa2015.report;

import co.unruly.spa2015.github.Fork;
import co.unruly.spa2015.github.InvalidForkException;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.emptyList;

public class Stubs {

    static class StubFork extends Fork {
        public StubFork(String user, String name) {
            super(user, name, null);
        }

        @Override
        public List<Fork> children(int maxDepth) throws InvalidForkException {
            return emptyList();
        }
    }

    static class BrokenFork extends Fork {

        public BrokenFork(String user, String name) {
            super(user, name, null);
        }

        @Override
        public List<Fork> children(int maxDepth) throws InvalidForkException {
            throw new InvalidForkException(this, new IOException("It's all gone a bit wrong ..."));
        }
    }

}
