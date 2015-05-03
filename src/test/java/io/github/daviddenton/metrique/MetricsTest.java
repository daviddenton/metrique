package io.github.daviddenton.metrique;


import org.junit.Test;

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
        assertEquals(rootMetrics.child("bob").name, new MetricName("bob"));
    }

    @Test
    public void countsAreAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").count(1L);
        verify(client).count(new MetricName("bob", "rita"), 1L);
    }

    @Test
    public void incrementAreAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").increment();
        verify(client).increment(new MetricName("bob", "rita"));
    }

    @Test
    public void deccrementAreAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").decrement();
        verify(client).decrement(new MetricName("bob", "rita"));
    }

    @Test
    public void timersAreAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").time(1L);
        verify(client).time(new MetricName("bob", "rita"), 1L);
    }

    @Test
    public void gaugesAreAsExpected() throws Exception {
        rootMetrics.child("bob").metric("rita").gauge(1L);
        verify(client).gauge(new MetricName("bob", "rita"), 1L);
    }
}
