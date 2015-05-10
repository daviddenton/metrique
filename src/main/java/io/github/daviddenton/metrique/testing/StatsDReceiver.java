package io.github.daviddenton.metrique.testing;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface StatsDReceiver {

    void receive(String message);

    StatsDReceiver Printing = new StatsDReceiver() {
        @Override
        public void receive(String message) {
            System.out.println("StatsD received: " + message);
        }
    };

    StatsDReceiver NoOp = new StatsDReceiver() {
        @Override
        public void receive(String message) {
        }
    };

    class Recording implements StatsDReceiver {
        public final List<String> receivedMessages = new CopyOnWriteArrayList<>();

        @Override
        public void receive(String message) {
            receivedMessages.add(message);
        }
    }
}
