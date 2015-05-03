package io.github.daviddenton.metrique;

public class Host {

    public final String name;

    private Host(String name) {
        this.name = name;
    }

    public static final Host localhost = new Host("localhost");

    public static Host host(String name) {
        return new Host(name);
    }
}
