package io.github.daviddenton.metrique;

import org.junit.Test;

import static io.github.daviddenton.metrique.MetricName.metricName;
import static org.junit.Assert.assertEquals;

public class MetricNameTest {

    @Test
    public void toStringIsJustName() throws Exception {
        assertEquals(metricName("bob").toString(), "bob");
    }

    @Test
    public void hashcodeIsEqual() throws Exception {
        assertEquals(metricName("bob").hashCode(), metricName("bob").hashCode());
    }

    @Test
    public void equalIsEqual() throws Exception {
        assertEquals(metricName("bob"), metricName("bob"));
    }

    @Test
    public void rootNameIsEmpty() throws Exception {
        assertEquals(metricName().value, "");
    }

    @Test
    public void itConcatsChildNames() throws Exception {
        assertEquals(metricName("bob").child("rita", "sue").value, "bob.rita.sue");
    }
}
