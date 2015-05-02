package io.github.daviddenton.metrique.metrics;

public interface MetricsClient {
    void decrement(MetricName name);

    void increment(MetricName name);

    void gauge(MetricName name, Long value);

    void time(MetricName name, Long value);

    void count(MetricName name, Long value);
}
