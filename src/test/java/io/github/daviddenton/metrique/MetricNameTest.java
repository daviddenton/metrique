package io.github.daviddenton.metrique;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MetricNameTest {

    @Test
    public void rootNameIsEmpty() throws Exception {
        assertEquals(new MetricName().value, "");
    }

    @Test
    public void itConcatsChildNames() throws Exception {
        assertEquals(new MetricName("bob").child("rita", "sue").value, "bob.rita.sue");
    }
}
