package io.github.daviddenton.metrique.metrics;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public class MetricName {

    private final List<String> parts;

    public MetricName(String... parts) {
        this(asList(parts));
    }

    public MetricName(List<String> parts) {
        this.parts = parts;
    }

    public String value() {
        return String.join(".", parts);
    }

    @Override
    public String toString() {
        return value();
    }

    public MetricName child(String... newParts) {
        return new MetricName(concat(parts.stream(), of(newParts)).collect(toList()));
    }
}