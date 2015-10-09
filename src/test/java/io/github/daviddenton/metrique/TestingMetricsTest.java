package io.github.daviddenton.metrique;

import io.github.daviddenton.metrique.testing.TestingMetrics;
import org.junit.Test;

import static io.github.daviddenton.metrique.MetricName.metricName;
import static io.github.daviddenton.metrique.testing.TestingMetrics.PrintingMetrics;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TestingMetricsTest {

    private final TestingMetrics.RecordingMetrics recordingMetrics = new TestingMetrics.RecordingMetrics();
    private final Metric metric = recordingMetrics.metric("bob");

    @Test
    public void printingMetrics() throws Exception {
        Metric metric = PrintingMetrics.child("bob").metric("rita");
        metric.count(1L);
        metric.time(1L);
        metric.gauge(() -> 1L);
        metric.histogram(1L);
        metric.meter();
        metric.decrement();
        metric.increment();
    }

    @Test
    public void recordingMetricsRecordsCounter() throws Exception {
        assertThat(recordingMetrics.counter(metricName("bob")), equalTo(0L));
        metric.count(1L);
        assertThat(recordingMetrics.counter(metricName("bob")), equalTo(1L));
    }

    @Test
    public void recordingMetricsRecordsMeterMark() throws Exception {
        assertThat(recordingMetrics.meter(metricName("bob")), equalTo(0L));
        metric.meter();
        assertThat(recordingMetrics.meter(metricName("bob")), equalTo(1L));
    }

    @Test
    public void recordingMetricsRecordsIncrement() throws Exception {
        assertThat(recordingMetrics.counter(metricName("bob")), equalTo(0L));
        metric.increment();
        assertThat(recordingMetrics.counter(metricName("bob")), equalTo(1L));
    }

    @Test
    public void recordingMetricsRecordsDecrement() throws Exception {
        assertThat(recordingMetrics.counter(metricName("bob")), equalTo(0L));
        metric.decrement();
        assertThat(recordingMetrics.counter(metricName("bob")), equalTo(-1L));
    }

    @Test
    public void recordingMetricsRecordsHistogram() throws Exception {
        assertThat(recordingMetrics.histogram(metricName("bob")).isEmpty(), equalTo(true));
        metric.histogram(1L);
        assertThat(recordingMetrics.histogram(metricName("bob")), equalTo(singletonList(1L)));
    }

    @Test
    public void recordingMetricsCanQueryGauge() throws Exception {
        recordingMetrics.metric("bob").gauge(() -> 123L);
        assertThat(recordingMetrics.gauge(metricName("bob")), equalTo(123L));
    }

    @Test
    public void recordingMetricsRecordsTimer() throws Exception {
        assertThat(recordingMetrics.timer(metricName("bob")).isEmpty(), equalTo(true));
        metric.time(1L);
        assertThat(recordingMetrics.timer(metricName("bob")), equalTo(singletonList(1L)));
    }
}
