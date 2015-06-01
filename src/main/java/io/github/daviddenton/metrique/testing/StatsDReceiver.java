package io.github.daviddenton.metrique.testing;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface StatsDReceiver extends Consumer<String> {

    StatsDReceiver Printing = message -> System.out.println("StatsD received: " + message);

    StatsDReceiver NoOp = message -> { };

    class Recording implements StatsDReceiver {
        public final List<String> receivedMessages = new CopyOnWriteArrayList<>();

        @Override
        public void accept(String message) {
            receivedMessages.add(message);
        }

        public boolean hasReceivedMessageContaining(String part) {
            return !messagesContaining(part).isEmpty();
        }

        public List<String> messagesContaining(String part) {
            return receivedMessages.stream().filter(in -> in.contains(part)).collect(Collectors.toList());
        }
    }
}
