package io.github.daviddenton.metrique;

import java.net.InetSocketAddress;

public class Host {

    public final String name;

    private Host(String name) {
        this.name = name;
    }

    public static final Host localhost = new Host("localhost");

    public InetSocketAddress socketAddress(Port port) {
        return new InetSocketAddress(name, port.value);
    }

    public static Host host(String name) {
        return new Host(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Host host = (Host) o;

        if (!name.equals(host.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
