package io.github.daviddenton.metrique.metrics;

import java.util.List;

public class Metrics {

    private final List<String> rootName;
    private final MetricsClient client;
    public final MetricName name;

    protected Metrics(MetricsClient client, List<String> rootName) {
        this.rootName = rootName;
        this.client = client;
        this.name = new MetricName(rootName);
    }

    public Metrics child(String... newParts) {
        return new Metrics(client, name.getCollect(newParts, rootName));
    }

    public Metric metric(String... newParts) {
        MetricName finalName = child(newParts).name;

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
