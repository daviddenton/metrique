package io.github.daviddenton.metrique.testing;

import io.github.daviddenton.metrique.MetricName;
import io.github.daviddenton.metrique.Metrics;
import io.github.daviddenton.metrique.MetricsClient;

import java.util.*;
import java.util.function.Supplier;

public interface TestingMetrics {
    Metrics PrintingMetrics = new Metrics(new MetricsClient() {
        @Override
        public <T> void gauge(MetricName name, Supplier<T> supplier) {
            System.out.println("Metrics: registered gauge " + name);
        }

        @Override
        public void decrement(MetricName name) {
            System.out.println("Metrics: decrement: " + name);
        }

        @Override
        public void increment(MetricName name) {
            System.out.println("Metrics: increment: " + name);
        }

        @Override
        public void histogram(MetricName name, Long value) {
            System.out.println("Metrics: histogram: " + name + "=" + value);
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

    class RecallableMetricsClient implements MetricsClient {
        protected final Map<MetricName, Long> counters = new HashMap<>();
        protected final Map<MetricName, Supplier<?>> gauges = new HashMap<>();
        protected final Map<MetricName, List<Long>> histograms = new HashMap<>();
        protected final Map<MetricName, List<Long>> timers = new HashMap<>();

        @Override
        public void histogram(MetricName name, Long value) {
            if (!histograms.containsKey(name)) {
                histograms.put(name, new ArrayList<>());
            }
            histograms.get(name).add(value);
        }

        @Override
        public void time(MetricName name, Long value) {
            if (!timers.containsKey(name)) {
                timers.put(name, new ArrayList<>());
            }
            timers.get(name).add(value);
        }

        @Override
        public <T> void gauge(MetricName name, Supplier<T> supplier) {
            if (!gauges.containsKey(name)) {
                gauges.put(name, supplier);
            }
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

    class RecordingMetrics extends Metrics {
        public long counter(MetricName name) {
            Long val = ((RecallableMetricsClient)client).counters.get(name);
            return val == null ? 0 : val;
        }

        public <T> T gauge(MetricName name) {
            return (T) ((RecallableMetricsClient)client).gauges.get(name).get();
        }

        public List<Long> histogram(MetricName name) {
            List<Long> val = ((RecallableMetricsClient)client).histograms.get(name);
            return val == null ? Collections.<Long>emptyList() : val;
        }

        public List<Long> timer(MetricName name) {
            List<Long> val = ((RecallableMetricsClient)client).timers.get(name);
            return val == null ? Collections.<Long>emptyList() : val;
        }

        public RecordingMetrics() {
            super(new RecallableMetricsClient(), MetricName.ROOT);
        }
    }

}
