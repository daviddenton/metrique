package io.github.daviddenton.metrique;

import java.util.function.Supplier;

public class Metrics {

    protected final MetricsClient client;
    public final MetricName name;

    public Metrics(MetricsClient client, MetricName rootName) {
        this.client = client;
        this.name = rootName;
    }

    public Metrics childFor(Object o) {
        return childFor(o.getClass());
    }

    public Metrics childFor(Class<?> clazz) {
        return child(clazz.getName().split("\\."));
    }

    public Metrics child(String... newParts) {
        return new Metrics(client, name.child(newParts));
    }

    public Metric metric(String... newParts) {
        final MetricName finalName = child(newParts).name;

        return new Metric() {
            @Override
            public void count(Long value) {
                client.count(finalName, value);
            }

            @Override
            public void increment() {
                client.increment(finalName);
            }

            @Override
            public void decrement() {
                client.decrement(finalName);
            }

            @Override
            public void histogram(Long value) {
                client.histogram(finalName, value);
            }

            @Override
            public <T> void gauge(Supplier<T> value) {
                client.gauge(finalName, value);
            }

            @Override
            public void time(Long value) {
                client.time(finalName, value);
            }

            @Override
            public void meter() {
                client.meter(finalName);
            }
        };
    }
}
