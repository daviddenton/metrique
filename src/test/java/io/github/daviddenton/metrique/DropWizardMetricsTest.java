package io.github.daviddenton.metrique;

import io.github.daviddenton.metrique.testing.FakeStatsDServer;
import io.github.daviddenton.metrique.testing.StatsDReceiver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.github.daviddenton.metrique.DropWizardMetrics.dropWizardMetrics;
import static io.github.daviddenton.metrique.Host.localhost;
import static io.github.daviddenton.metrique.Port.port;
import static org.junit.Assert.assertTrue;

public class DropWizardMetricsTest {
    private static final Port PORT = port(9999);
    private final DropWizardMetrics metrics = dropWizardMetrics(localhost, PORT, "prefix", 10);
    private final StatsDReceiver.Recording statReceiver = new StatsDReceiver.Recording();
    private final FakeStatsDServer statsD = new FakeStatsDServer(PORT, statReceiver);

    @Test
    public void sendsStatsToServer() throws Exception {
        metrics.metric("bob").count(10L);
        Thread.sleep(10);
        assertTrue(statReceiver.receivedMessages.get(0).startsWith("prefix.bob.samples:10|g"));
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
