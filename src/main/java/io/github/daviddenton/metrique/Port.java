package io.github.daviddenton.metrique;

public class Port {

    public final int value;

    private Port(int value) {
        this.value = value;
    }

    public static Port port(int value) {
        return new Port(value);
    }
}
