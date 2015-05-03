package io.github.daviddenton.metrique;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MetricNameTest {

    @Test
    public void toStringIsJustName() throws Exception {
        assertEquals(new MetricName("bob").toString(), "bob");
    }

    @Test
    public void hashcodeIsEqual() throws Exception {
        assertEquals(new MetricName("bob").hashCode(), new MetricName("bob").hashCode());
    }

    @Test
    public void equalIsEqual() throws Exception {
        assertEquals(new MetricName("bob"), new MetricName("bob"));
    }

    @Test
    public void rootNameIsEmpty() throws Exception {
        assertEquals(new MetricName().value, "");
    }

    @Test
    public void itConcatsChildNames() throws Exception {
        assertEquals(new MetricName("bob").child("rita", "sue").value, "bob.rita.sue");
    }
}
