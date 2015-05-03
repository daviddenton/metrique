package io.github.daviddenton.metrique;

import com.google.common.base.Joiner;

import static com.google.common.collect.Iterables.concat;
import static java.util.Arrays.asList;

public class MetricName {

    private final Iterable<String> parts;

    public static final MetricName ROOT = new MetricName();

    public final String value;

    public MetricName(String... parts) {
        this(asList(parts));
    }

    public MetricName(Iterable<String> parts) {
        this.parts = parts;
        value = Joiner.on(".").join(parts);
    }

    @Override
    public String toString() {
        return value;
    }

    public MetricName child(String... newParts) {
        return new MetricName(concat(parts, asList(newParts)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetricName that = (MetricName) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}