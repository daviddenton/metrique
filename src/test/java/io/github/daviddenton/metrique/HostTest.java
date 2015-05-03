package io.github.daviddenton.metrique;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HostTest {

    @Test
    public void hashcodeIsEqual() throws Exception {
        assertEquals(Host.host("bob").hashCode(), Host.host("bob").hashCode());
    }

    @Test
    public void equalIsEqual() throws Exception {
        assertEquals(Host.host("bob"), Host.host("bob"));
    }

}
