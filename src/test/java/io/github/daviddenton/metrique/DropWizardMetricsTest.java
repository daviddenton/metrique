package io.github.daviddenton.metrique;

import io.github.daviddenton.metrique.testing.FakeStatsDServer;
import io.github.daviddenton.metrique.testing.StatsDReceiver;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Iterables.any;
import static io.github.daviddenton.metrique.DropWizardMetrics.dropWizardMetrics;
import static io.github.daviddenton.metrique.Host.localhost;
import static io.github.daviddenton.metrique.Port.port;
import static org.junit.Assert.assertThat;

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
    public void sendsMeterStatsToServer() throws Exception {
        metrics.metric("bob").meter();
        metrics.metric("bob").meter();
        Thread.sleep(20);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob.samples:2|g"));
    }

    @Test
    public void sendsIncrementStatsToServer() throws Exception {
        metrics.metric("bob").increment();
        metrics.metric("bob").increment();
        Thread.sleep(20);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob:2|g"));
    }

    @Test
    public void sendsDecrementStatsToServer() throws Exception {
        metrics.metric("bob").decrement();
        Thread.sleep(20);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob:-1|g"));
    }

    @Test
    public void sendsHistogramStatsToServer() throws Exception {
        metrics.metric("bob").histogram(1L);
        Thread.sleep(20);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob.samples:1|g"));
    }

    @Test
    public void sendsGaugeStatsToServer() throws Exception {
        AtomicInteger value = new AtomicInteger(1);
        metrics.metric("bob").gauge(() -> value.addAndGet(1));
        Thread.sleep(20);
        System.out.println(statReceiver.receivedMessages);
        assertThat(statReceiver.receivedMessages, containsAMessageWhichIncludes("prefix.bob:2|g"));
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

    public static Matcher<List<String>> containsAMessageWhichIncludes(final String message) {
        return new TypeSafeMatcher<List<String>>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("containing a partial message of " + message);
            }

            @Override
            protected boolean matchesSafely(List<String> allMessages) {
                return any(allMessages, input -> input.contains(message));
            }
        };
    }
}
