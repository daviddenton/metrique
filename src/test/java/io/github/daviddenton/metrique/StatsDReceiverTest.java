package io.github.daviddenton.metrique;

import io.github.daviddenton.metrique.testing.StatsDReceiver;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StatsDReceiverTest {

    @Test
    public void noOpReceivesMessage() throws Exception {
        StatsDReceiver.NoOp.accept("some message");
    }

    @Test
    public void printingReceivesMessage() throws Exception {
        StatsDReceiver.Printing.accept("some message");
    }

    @Test
    public void recordingReceivesMessage() throws Exception {
        StatsDReceiver.Recording recording = new StatsDReceiver.Recording();
        recording.accept("some message");
        assertThat(recording.receivedMessages, equalTo(singletonList("some message")));
        assertThat(recording.hasReceivedMessageContaining("some"), equalTo(true));
        assertThat(recording.messagesContaining("some"), equalTo(singletonList("some message")));
    }
}
