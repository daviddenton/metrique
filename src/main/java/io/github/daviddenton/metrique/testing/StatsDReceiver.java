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

    class Recording implements StatsDReceiver {
        public final List<String> receivedMessages = new CopyOnWriteArrayList<>();

        @Override
        public void receive(String message) {
            receivedMessages.add(message);
        }
//
//        public void assertReceivedMessageContaining(String message) {
//            assertThat(receivedMessages, containsAMessageWhichIncludes(message));
//        }
//
//        private static Matcher<List<String>> containsAMessageWhichIncludes(String message) {
//            return new TypeSafeMatcher<List<String>>() {
//
//                @Override
//                protected boolean matchesSafely(List<String> allMessages) {
//                    return allMessages.stream().anyMatch(s -> s.contains(message));
//                }
//
//                @Override
//                public void describeTo(Description description) {
//                    description.appendText("containing a partial message of " + message);
//                }
//            };
//        }
    }

    class NoOp implements StatsDReceiver {
        @Override
        public void receive(String message) {
        }
    }
}
