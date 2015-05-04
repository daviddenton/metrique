package io.github.daviddenton.metrique.testing;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.google.common.collect.Iterables.any;

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

        public static Matcher<List<String>> containsAMessageWhichIncludes(final String message) {
            return new TypeSafeMatcher<List<String>>() {

                @Override
                public void describeTo(org.hamcrest.Description description) {
                    description.appendText("containing a partial message of " + message);
                }

                @Override
                protected boolean matchesSafely(List<String> allMessages) {
                    return any(allMessages, new Predicate<String>() {
                        @Override
                        public boolean apply(String input) {
                            return input.contains(message);
                        }
                    });
                }
            };
        }
    }
}
