package io.github.daviddenton.metrique;

import io.github.daviddenton.metrique.testing.FakeStatsDServer;
import io.github.daviddenton.metrique.testing.StatsDReceiver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.github.daviddenton.metrique.DropWizardMetrics.dropWizardMetrics;
import static io.github.daviddenton.metrique.Host.localhost;
import static io.github.daviddenton.metrique.Port.port;
import static io.github.daviddenton.metrique.testing.StatsDReceiver.Recording.containsAMessageWhichIncludes;
import static org.hamcrest.MatcherAssert.assertThat;

public class DropWizardMetricsTest {
    private static final Port PORT = port(9999);
    private final DropWizardMetrics metrics = dropWizardMetrics(localhost, PORT, "prefix", 10);
    private final StatsDReceiver.Recording statReceiver = new StatsDReceiver.Recording();
    private final FakeStatsDServer statsD = new FakeStatsDServer(PORT, statReceiver);

    @Test
    public void sendsCountStatsToServer() throws Exception {
        metrics.metric("bob").count(10L);
        Thread.sleep(20);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob.samples:10|g"));
    }

    @Test
    public void sendsIncrementStatsToServer() throws Exception {
        metrics.metric("bob").increment();
        Thread.sleep(20);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob:1|g"));
    }

    @Test
    public void sendsDecrementStatsToServer() throws Exception {
        metrics.metric("bob").decrement();
        Thread.sleep(20);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob:-1|g"));
    }

    @Test
    public void sendsGaugeStatsToServer() throws Exception {
        metrics.metric("bob").gauge(1L);
        Thread.sleep(20);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob.samples:1|g"));
    }

    @Test
    public void sendsTimerStatsToServer() throws Exception {
        metrics.metric("bob").time(1L);
        Thread.sleep(20);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob.max:1.00|g"));
    }

    @Before
    public void setUp() throws Exception {
        statsD.start();
        metrics.start();
    }

    @After
    public void teardown() throws Exception {
        metrics.stop();
        statsD.stop();
    }
}
