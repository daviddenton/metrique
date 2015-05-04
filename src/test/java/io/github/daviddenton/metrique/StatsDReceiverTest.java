package io.github.daviddenton.metrique;

import io.github.daviddenton.metrique.testing.StatsDReceiver;
import org.junit.Test;

public class StatsDReceiverTest {

    @Test
    public void noOpReceivesMessage() throws Exception {
        StatsDReceiver.NoOp.receive("some message");
    }

    @Test
    public void printingReceivesMessage() throws Exception {
        StatsDReceiver.Printing.receive("some message");
    }
}
