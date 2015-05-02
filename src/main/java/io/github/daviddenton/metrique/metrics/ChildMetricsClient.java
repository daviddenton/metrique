package io.github.daviddenton.metrique.metrics;

public class ChildMetricsClient implements MetricsClient {

    private final MetricsClient delegate;
    private final String prefix;

    public ChildMetricsClient(MetricsClient delegate, String prefix) {
        this.delegate = delegate;
        this.prefix = prefix;
    }

    private MetricName childMetric(MetricName name) {
        return new MetricName(prefix, name.toString());
    }

    public void decrement(MetricName name) {
        delegate.decrement(childMetric(name));
    }

    public void count(MetricName name, Long value) {
        delegate.count(childMetric(name), value);
    }

    public void increment(MetricName name) {
        delegate.increment(childMetric(name));
    }

    public void gauge(MetricName name, Long value) {
        delegate.gauge(childMetric(name), value);
    }

    public void time(MetricName name, Long value) {
        delegate.time(childMetric(name), value);
    }
}
