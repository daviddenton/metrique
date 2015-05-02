package io.github.daviddenton.metrique.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.readytalk.metrics.StatsDReporter.forRegistry;
import static java.util.Collections.EMPTY_LIST;

public class DropWizardMetrics extends Metrics {

    private final ScheduledReporter reporter;
    private final Duration interval;

    private DropWizardMetrics(ScheduledReporter reporter, MetricsClient client, List<String> parts, Duration interval) {
        super(client, parts);
        this.reporter = reporter;
        this.interval = interval;
    }

    public void start() {
        reporter.start(interval.toMillis(), TimeUnit.MILLISECONDS);
    }

    public void stop() {
        reporter.stop();
    }

    public static DropWizardMetrics dropWizardMetrics(Host host, Port port, String prefix, Duration reportingInterval) {
        MetricRegistry registry = new MetricRegistry();
        ScheduledReporter reporter = forRegistry(registry).prefixedWith(prefix).build(host.name, port.value);
        return new DropWizardMetrics(reporter, new DropWizardMetricsClient(registry), EMPTY_LIST, reportingInterval);
    }

    private static class DropWizardMetricsClient implements MetricsClient {
        private final MetricRegistry registry;

        public DropWizardMetricsClient(MetricRegistry registry) {
            this.registry = registry;
        }

        @Override
        public void decrement(MetricName name) {
            registry.counter(name.value()).dec(1);
        }

        @Override
        public void increment(MetricName name) {
            registry.counter(name.value()).inc(1);
        }

        @Override
        public void gauge(MetricName name, Long value) {
            registry.histogram(name.value()).update(value);
        }

        @Override
        public void time(MetricName name, Long value) {
            registry.timer(name.value()).update(value, TimeUnit.MILLISECONDS);
        }

        @Override
        public void count(MetricName name, Long value) {
            registry.meter(name.value()).mark(value);
        }
    }
}