package io.github.daviddenton.metrique;


import org.junit.Test;

import java.util.function.Supplier;

import static io.github.daviddenton.metrique.MetricName.metricName;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MetricsTest {

    private final MetricsClient client = mock(MetricsClient.class);

    private final Metrics rootMetrics = new Metrics(client, MetricName.ROOT);

    @Test
    public void rootNameIsEmpty() throws Exception {
        assertEquals(rootMetrics.name, MetricName.ROOT);
    }

    @Test
    public void canCreateNamedChildMetrics() throws Exception {
        assertEquals(metricName("bob"), rootMetrics.child("bob").name);
    }

    @Test
    public void canCreateChildMetricsForObject() throws Exception {
        assertEquals(metricName(this.getClass().getName()), rootMetrics.childFor(this).name);
    }

    @Test
    public void canCreateChildMetricsFoClass() throws Exception {
        assertEquals(metricName(this.getClass().getName()), rootMetrics.childFor(this.getClass()).name);
    }

    @Test
    public void countsRecordsAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").count(1L);
        verify(client).count(metricName("bob", "rita"), 1L);
    }

    @Test
    public void incrementRecordsAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").increment();
        verify(client).increment(metricName("bob", "rita"));
    }

    @Test
    public void decrementRecordsAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").decrement();
        verify(client).decrement(metricName("bob", "rita"));
    }

    @Test
    public void timersRecordsAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").time(1L);
        verify(client).time(metricName("bob", "rita"), 1L);
    }

    @Test
    public void histogramsRecordsAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").histogram(1L);
        verify(client).histogram(metricName("bob", "rita"), 1L);
    }

    @Test
    public void gaugesRecordsAsExpected() throws Exception {
        Supplier<Long> longSupplier = () -> 1L;
        rootMetrics.child("bob").metric("rita").gauge(longSupplier);
        verify(client).gauge(metricName("bob", "rita"), longSupplier);
    }
}
