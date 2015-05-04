package io.github.daviddenton.metrique;

public class Metrics<T extends MetricsClient> {

    protected final T client;
    public final MetricName name;

    public Metrics(T client, MetricName rootName) {
        this.client = client;
        this.name = rootName;
    }

    public Metrics childFor(Object o) {
        return child(o.getClass().getName().split("\\."));
    }

    public Metrics child(String... newParts) {
        return new Metrics<>(client, name.child(newParts));
    }

    public Metric metric(String... newParts) {
        final MetricName finalName = child(newParts).name;

        return new Metric() {
            public void count(Long value) {
                client.count(finalName, value);
            }

            public void increment() {
                client.increment(finalName);
            }

            public void decrement() {
                client.decrement(finalName);
            }

            public void gauge(Long value) {
                client.gauge(finalName, value);
            }

            public void time(Long value) {
                client.time(finalName, value);
            }
        };
    }
}
