package io.github.daviddenton.metrique;

import java.util.function.Supplier;

public interface MetricsClient {

    <T> void gauge(MetricName name, Supplier<T> supplier);

    void decrement(MetricName name);

    void increment(MetricName name);

    void histogram(MetricName name, Long value);

    void meter(MetricName name);

    void time(MetricName name, Long value);

    void count(MetricName name, Long value);
}
