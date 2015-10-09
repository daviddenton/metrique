package io.github.daviddenton.metrique;

import java.util.function.Supplier;

public interface Metric {
    void decrement();

    void increment();

    void histogram(Long value);

    <T> void gauge(Supplier<T> value);

    void time(Long value);

    void meter();

    void count(Long value);
}
