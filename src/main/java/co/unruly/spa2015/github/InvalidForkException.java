package co.unruly.spa2015.github;

public class InvalidForkException extends Exception {
    public final Fork badFork;

    public InvalidForkException(Fork badFork, Exception cause) {
        super("Bad fork " + badFork, cause);
        this.badFork = badFork;
    }
}
