package io.github.daviddenton.metrique.testing;

import io.github.daviddenton.metrique.MetricName;
import io.github.daviddenton.metrique.Metrics;
import io.github.daviddenton.metrique.MetricsClient;

import java.util.*;

public interface TestingMetrics {
    static Metrics PrintingMetrics = new Metrics<>(new MetricsClient() {
        @Override
        public void decrement(MetricName name) {
            System.out.println("Metrics: decrement: " + name);
        }

        @Override
        public void increment(MetricName name) {
            System.out.println("Metrics: increment: " + name);
        }

        @Override
        public void gauge(MetricName name, Long value) {
            System.out.println("Metrics: gauge: " + name + "=" + value);
        }

        @Override
        public void time(MetricName name, Long value) {
            System.out.println("Metrics: time: " + name + "=" + value);
        }

        @Override
        public void count(MetricName name, Long value) {
            System.out.println("Metrics: count: " + name + "=" + value);
        }
    }, MetricName.ROOT);

    static class RecallableMetricsClient implements MetricsClient {
        protected final Map<MetricName, Long> counters = new HashMap<>();
        protected final Map<MetricName, List<Long>> gauges = new HashMap<>();
        protected final Map<MetricName, List<Long>> timers = new HashMap<>();

        @Override
        public void gauge(MetricName name, Long value) {
            if (!gauges.containsKey(name)) {
                gauges.put(name, new ArrayList<Long>());
            }
            gauges.get(name).add(value);
        }

        @Override
        public void time(MetricName name, Long value) {
            if (!timers.containsKey(name)) {
                timers.put(name, new ArrayList<Long>());
            }
            timers.get(name).add(value);
        }

        @Override
        public void decrement(MetricName name) {
            count(name, -1L);
        }

        @Override
        public void increment(MetricName name) {
            count(name, 1L);
        }

        @Override
        public void count(MetricName name, Long value) {
            Long existing = counters.get(name);
            counters.put(name, existing == null ? value : existing + value);

        }
    }

    static class RecordingMetrics extends Metrics<RecallableMetricsClient> {
        public long counter(MetricName name) {
            Long val = client.counters.get(name);
            return val == null ? 0 : val;
        }

        public List<Long> gauge(MetricName name) {
            List<Long> val = client.gauges.get(name);
            return val == null ? Collections.<Long>emptyList() : val;
        }

        public List<Long> timer(MetricName name) {
            List<Long> val = client.timers.get(name);
            return val == null ? Collections.<Long>emptyList() : val;
        }

        public RecordingMetrics() {
            super(new RecallableMetricsClient(), MetricName.ROOT);
        }
    }

}
